package com.ruyicai.agencycenter.consts;

import javax.annotation.PostConstruct;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoutesConfiguration {

	private Logger logger = LoggerFactory.getLogger(RoutesConfiguration.class);

	@Autowired
	private CamelContext camelContext;

	@PostConstruct
	public void init() throws Exception {
		logger.info("init camel routes");
		camelContext.addRoutes(new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				deadLetterChannel("jms:queue:dead").maximumRedeliveries(-1).redeliveryDelay(3000);
				from("jms:queue:VirtualTopicConsumers.agencycenter.sendAgencyPrize").to(
						"bean:sendAgencyPrizeListener?method=sendAgencyPrizeCustomer").routeId("发放代理奖金");
				from("jms:queue:VirtualTopicConsumers.agencycenter.actioncenter?concurrentConsumers=20").to(
						"bean:fundJmsListener?method=fundJmsCustomer").routeId("代理购彩");
			}
		});
	}
}
