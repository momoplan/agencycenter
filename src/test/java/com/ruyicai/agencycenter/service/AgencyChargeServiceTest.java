package com.ruyicai.agencycenter.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class })
@ContextConfiguration(locations = { "classpath:/META-INF/spring/applicationContext.xml",
		"classpath:/META-INF/spring/applicationContext-jms.xml",
		"classpath:/META-INF/spring/applicationContext-memcache.xml" })
public class AgencyChargeServiceTest {

	@Produce(uri = "jms:topic:actioncenter")
	private ProducerTemplate sendActioncenterProducer;
	
	@Test
	public void testDirect() {
		for(int i=0;i<10;i++){
			sendAgencyCouponToBindingJMS("00002117",200+i*10,1,"TE2012072400229"+i*100);
		}
	}
	
	@Transactional
	public void sendAgencyCouponToBindingJMS(String userno,Integer amt,Integer type,String businessId){
		Map<String, Object> headers = new HashMap<String, Object>();
		headers.put("USERNO", userno);
		headers.put("AMT", amt);
		headers.put("TYPE", type);
		headers.put("BUSINESSID", businessId);
		sendActioncenterProducer.sendBodyAndHeaders(null, headers);
		System.out.println("-----Over-----");
	}
	
}
