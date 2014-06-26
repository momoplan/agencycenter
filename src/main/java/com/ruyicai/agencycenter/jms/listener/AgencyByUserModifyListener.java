package com.ruyicai.agencycenter.jms.listener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Body;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruyicai.agencycenter.domain.BetRequest;
import com.ruyicai.agencycenter.domain.OrderRequest;
import com.ruyicai.agencycenter.domain.PresentMsgContent;
import com.ruyicai.agencycenter.domain.Tlotctrl;
import com.ruyicai.agencycenter.domain.UserAgency;
import com.ruyicai.agencycenter.util.ErrorCode;
import com.ruyicai.agencycenter.util.HttpUtil;
import com.ruyicai.agencycenter.util.JsonUtil;
import com.ruyicai.lottery.domain.Tuserinfo;

/**
 * 
 * @Description: 代理所赠送的用户进行信息修改
 * 
 * @author chuang   
 * @date 2014年5月22日 下午6:44:49 
 * @version V1.0   
 *
 */
@Service
public class AgencyByUserModifyListener {

	private Logger logger = LoggerFactory.getLogger(AgencyByUserModifyListener.class);
	
	@Value("${lotteryurl}")
	String lotteryurl;
	
	@Transactional
	public void agencyByUserModifyCustomer(@Body String body){
		logger.info("代理所赠送的用户进行信息修改:{}", new String[] { body });
		Tuserinfo tuserinfo = Tuserinfo.fromJsonToTuserinfo(body);
		List<PresentMsgContent> resultList = null;
		OrderRequest orderRequest = null;
		if(UserAgency.findUserAgencyCount(tuserinfo.getUserno())>0){
			resultList = PresentMsgContent.isValid(tuserinfo.getUserno());
			if(resultList!=null){
				for(PresentMsgContent msgcontent : resultList){
					orderRequest = convertObj(msgcontent);
					String bodyparam = JsonUtil.toJson(orderRequest);
					logger.info("======bodyparam======="+bodyparam);
					try {
						String result = HttpUtil.post(lotteryurl +"/present/savepresent","body="+bodyparam);
						logger.info("=========result============"+result);
						JSONObject jsonObject = new JSONObject(result);
						String errorCode = jsonObject.getString("errorCode");
						if (errorCode.equals(ErrorCode.OK.value)) {
							PresentMsgContent.updateState(msgcontent.getId());
						}
					} catch (Exception e) {
						logger.error("请求" + lotteryurl+ "/present/savepresent?body="+bodyparam+"失败", e.getMessage());
					}
				}
			}else{
				logger.info("presentmsgcontent is null or expired");
			}
		}
	}
	
	public OrderRequest convertObj(PresentMsgContent content){
		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setBettype(content.getBetype());
		orderRequest.setBuyuserno(content.getBuyUserno());
		orderRequest.setReciverMobile(content.getReciverMobile());
		orderRequest.setUserno(content.getReciverUserno());
		orderRequest.setBatchcode(currentBatchcode(content.getLotno()));
		orderRequest.setLotno(content.getLotno());
		List<BetRequest> list = new ArrayList<BetRequest>();
		String[] contentArr = content.getContent().split(";");
		String[] everyAmtArr = content.getEveryAmt().split(";");
		for(int i=0;i<contentArr.length;i++){
			BetRequest betRequest = new BetRequest();
			betRequest.setBetcode(contentArr[i]);
			betRequest.setAmt(new BigDecimal(everyAmtArr[i]));
			list.add(betRequest);
		}
		orderRequest.setBetRequests(list);
		orderRequest.setAmt(content.getAmt());
		orderRequest.setLotmulti(content.getLotmulti());
		orderRequest.setBlessing(content.getBlessing());
		return orderRequest;
	}
	
	public String currentBatchcode(String lotno){
		String result = "" ;
		Tlotctrl tlotctrl = null;
		try {
			result = HttpUtil.post(lotteryurl+"/select/getIssue", "lotno="+lotno);
			if (StringUtils.isNotBlank(result)) {
				JSONObject jsonObject = new JSONObject(result);
				String errorCode = jsonObject.getString("errorCode");
				if (errorCode.equals(ErrorCode.OK.value)) {
					String value = jsonObject.getString("value");
					tlotctrl = Tlotctrl.fromJsonToTlotctrl(value);
				}
			}
		} catch (Exception e) {
			logger.error("请求" + lotteryurl+ "/select/getIssue?lotno="+lotno+"失败", e.getMessage());
		}
		return tlotctrl.getId().getBatchcode();
	}
	
}
