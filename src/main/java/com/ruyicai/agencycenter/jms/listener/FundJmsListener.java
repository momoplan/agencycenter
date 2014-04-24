package com.ruyicai.agencycenter.jms.listener;

import java.math.BigDecimal;

import org.apache.camel.Header;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruyicai.agencycenter.service.AgencyService;

@Service
public class FundJmsListener {

	private Logger logger = LoggerFactory.getLogger(FundJmsListener.class);

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
			@Header("BUSINESSTYPE") Integer businessType, @Header("ISCASELOTSTARTER") Integer isCaseLotStarter) {
		// 如果是购彩，则计算代理金额
		if (type == 2) {
			logger.info("userno:{},amt:{},type:{},lotno:{},businessId:{},businessType:{},isCaseLotStarter:{}",
					new String[] { userno, amt + "", type + "", lotno, businessId, businessType + "",
							isCaseLotStarter + "" });
			if (StringUtils.isBlank(userno)) {
				return;
			}
			if (amt <= 0) {
				return;
			}
			if (StringUtils.isBlank(businessId)) {
				return;
			}
			if (businessType != null) {
				if (businessType == 1) {// 订单投注
					agencyService.doAgencyPrize(userno, businessId, businessType, lotno, new BigDecimal(amt));
				} else if (businessType == 3) {// 合买投注
					if (isCaseLotStarter != null) {
						if (isCaseLotStarter == 1 || isCaseLotStarter == 0) { // 合买发起人  || 合买参与人
							agencyService.doAgencyPrize(userno, businessId, businessType, lotno, new BigDecimal(amt));
						} else {
							logger.error("isCaseLotStarter:" + isCaseLotStarter + " is error");
						}
					} else {
						logger.error("isCaseLotStarter is null");
					}
				} else {
					logger.error("businessType:" + businessType + " is error");
				}
			} else {
				logger.error("businessId is null");
			}
		}

	}

}
