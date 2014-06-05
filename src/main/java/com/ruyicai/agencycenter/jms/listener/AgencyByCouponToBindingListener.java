package com.ruyicai.agencycenter.jms.listener;

import org.apache.camel.Header;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruyicai.agencycenter.domain.UserAgency;
import com.ruyicai.agencycenter.service.AgencyService;

/**
 * 
 * @Description: 代理用户赠送兑换券
 * 
 * @author chuang   
 * @date 2014年5月13日 上午9:56:34 
 * @version V1.0   
 *
 */
@Service
public class AgencyByCouponToBindingListener {

	private Logger logger = LoggerFactory.getLogger(AgencyByCouponToBindingListener.class);

	@Autowired
	private AgencyService agencyService;
	
	public void agencyByCouponToBindingCustomer(@Header("userno") String userno,
			@Header("belonguserno") String belonguserno){
		logger.info("代理用户赠送兑换券userno:{},belonguserno:{}", new String[] { userno + "" , belonguserno});
		if(!StringUtils.isEmpty(userno) && !StringUtils.isEmpty(belonguserno)){
			if(UserAgency.findUserAgencyCount(belonguserno)>0){
				if(UserAgency.findUserAgencyCount(userno) == 0){
					try{
						agencyService.createUserAgency(userno, belonguserno);
					} catch(Exception e) {
						logger.error("代理用户赠送兑换券创建代理用户异常,{}", new String[] { e.getMessage() }, e);
					}
				}else{
					logger.info("下线用户已存在,userno:",userno);
				}
			}else{
				logger.info("代理用户不存在,belonguserno:",belonguserno);
			}
		}
	}
}
