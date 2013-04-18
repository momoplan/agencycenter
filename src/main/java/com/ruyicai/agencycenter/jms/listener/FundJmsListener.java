package com.ruyicai.agencycenter.jms.listener;

import java.math.BigDecimal;

import org.apache.camel.Header;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruyicai.agencycenter.service.AgencyService;

@Service
public class FundJmsListener {

	@Autowired
	private AgencyService agencyService;

	/**
	 * @param ttransactionid
	 * @param ladderpresentflag
	 * @param userno
	 * @param amt
	 * @param type
	 * @param businessId
	 * @param businessType
	 */
	public void fundJmsCustomer(@Header("USERNO") String userno, @Header("AMT") Long amt, @Header("TYPE") Integer type,
			@Header("LOTNO") String lotno, @Header("BUSINESSID") String businessId,
			@Header("BUSINESSTYPE") Integer businessType) {
		// 如果是购彩，则计算代理金额
		if (type == 2) {
			if (StringUtils.isBlank(userno)) {
				return;
			}
			if (amt <= 0) {
				return;
			}
			if (StringUtils.isBlank(businessId)) {
				return;
			}
			agencyService.doAgencyPrize(userno, businessId, businessType, lotno, new BigDecimal(amt));
		}

	}

}
