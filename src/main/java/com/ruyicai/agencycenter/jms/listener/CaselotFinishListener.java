package com.ruyicai.agencycenter.jms.listener;

import java.math.BigDecimal;

import org.apache.camel.Body;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruyicai.agencycenter.service.AgencyService;
import com.ruyicai.lottery.domain.CaseLot;

@Service
public class CaselotFinishListener {

	private Logger logger = LoggerFactory.getLogger(CaselotFinishListener.class);

	@Autowired
	private AgencyService agencyService;

	@Transactional
	public void caselotFinishCustomer(@Body String caseLotJson) {
		logger.info("合买满员代理 caseLotJson:" + caseLotJson);
		CaseLot caseLot = CaseLot.fromJsonToCaseLot(caseLotJson);
		if (caseLot != null && StringUtils.isNotBlank(caseLot.getId())) {
			agencyService.doAgencyPrize(caseLot.getStarter(), caseLot.getId(), 3, caseLot.getLotno(), new BigDecimal(
					caseLot.getTotalAmt()));
		}
	}

}
