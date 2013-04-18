package com.ruyicai.agencycenter.jms.listener;

import org.apache.camel.Body;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruyicai.agencycenter.service.AgencyService;
import com.ruyicai.lottery.domain.Torder;

@Service
public class OrderAfterBetListener {

	private Logger logger = LoggerFactory.getLogger(OrderAfterBetListener.class);

	@Autowired
	private AgencyService agencyService;

	public void orderAfterBetCustomer(@Body String orderJson) {
		Torder torder = Torder.fromJsonToTorder(orderJson);
		if (torder != null) {
			if (StringUtils.isBlank(torder.getTlotcaseid())) {
				if (StringUtils.isBlank(torder.getTsubscribeflowno())) {
					logger.info("普通投注计算代理奖金,orderJson:" + orderJson);
					agencyService.doAgencyPrize(torder.getUserno(), torder.getId(), 1, torder.getLotno(),
							torder.getAmt());
				} else {
					logger.info("追号投注计算代理奖金,orderJson:" + orderJson);
					agencyService.doAgencyPrize(torder.getUserno(), torder.getId(), 2, torder.getLotno(),
							torder.getAmt());
				}
			}
		}
	}

}
