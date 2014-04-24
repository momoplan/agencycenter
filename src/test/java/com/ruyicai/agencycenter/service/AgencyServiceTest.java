package com.ruyicai.agencycenter.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import com.ruyicai.agencycenter.domain.AgencyPercent;
import com.ruyicai.agencycenter.domain.AgencyPercentPK;
import com.ruyicai.agencycenter.domain.UserAgency;
import com.ruyicai.agencycenter.util.Lottype;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class })
@ContextConfiguration(locations = { "classpath:/META-INF/spring/applicationContext.xml",
		"classpath:/META-INF/spring/applicationContext-jms.xml",
		"classpath:/META-INF/spring/applicationContext-memcache.xml" })
public class AgencyServiceTest {

	private Logger logger = LoggerFactory.getLogger(AgencyServiceTest.class);

	@Autowired
	private AgencyService agencyService;

	@Test
	public void testCreateUserAgency() {
		UserAgency agency = UserAgency.createUserAgency("00000042", null);
		UserAgency agency2 = UserAgency.createUserAgency("00000036", agency);
		UserAgency.createUserAgency("00000196", agency2);
	}

	@Test
	public void testCreateUserAgency2() {
		UserAgency.findUserAgency("00000424").remove();
		UserAgency agency = agencyService.createUserAgency("00000424", "00000059");
	}

	@Test
	public void testFindAgencyPercent() {
		agencyService.findAgencyPercent("00000042");
		agencyService.findAgencyPercent("00000036");
		agencyService.findAgencyPercent("00000196");
	}

	@Test
	public void testSetPercent() {
		Map<String, String> map = Lottype.getMap();
		for (String lotno : map.keySet()) {
			AgencyPercent agencyPercent = AgencyPercent.findAgencyPercent(new AgencyPercentPK("00000042", lotno));
			agencyPercent.setPercent(new BigDecimal(8));
			agencyPercent.merge();
			AgencyPercent agencyPercent2 = AgencyPercent.findAgencyPercent(new AgencyPercentPK("00000036", lotno));
			agencyPercent2.setPercent(new BigDecimal(6));
			agencyPercent2.merge();
			AgencyPercent agencyPercent3 = AgencyPercent.findAgencyPercent(new AgencyPercentPK("00000196", lotno));
			agencyPercent3.setPercent(new BigDecimal(5));
			agencyPercent3.merge();
		}
	}

	@Test
	public void testDoAgencyPrize() throws InterruptedException {
		agencyService.doAgencyPrize("00000033", new Date().getTime() + "", 1, "F47104", new BigDecimal(10000));
		Thread.sleep(5 * 1000);
	}

}
