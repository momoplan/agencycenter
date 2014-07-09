package com.ruyicai.agencycenter.service;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruyicai.agencycenter.consts.BetType;
import com.ruyicai.agencycenter.consts.Lottype;
import com.ruyicai.agencycenter.domain.OrderRequest;
import com.ruyicai.agencycenter.domain.PresentMsgContent;
import com.ruyicai.agencycenter.domain.UserAgency;
import com.ruyicai.agencycenter.exception.RuyicaiException;
import com.ruyicai.agencycenter.util.ErrorCode;
import com.ruyicai.agencycenter.util.HttpUtil;
import com.ruyicai.agencycenter.util.RandomUtil;
import com.ruyicai.lottery.domain.Tuserinfo;

@Service
public class PresentService {

	private Logger logger = LoggerFactory.getLogger(PresentService.class);

	@Autowired
	private LotteryService lotteryService;
	
	@Autowired
	private AgencyService agencyService;
	
	@Autowired
	SendMessageService sendMessageService;
	
	@Value("${lotteryurl}")
	String lotteryurl;
	
	@Value("${msgcenterurl}")
	String msgcenterurl;
	
	@Transactional
	public void savePresentMsgContent(OrderRequest orderRequest) {
		logger.info("创建代理赠送彩票内容开始");
		Tuserinfo reciverUserinfo = null ;
		reciverUserinfo = findByUserName(orderRequest.getReciverMobile());
		if (reciverUserinfo == null || StringUtils.isBlank(reciverUserinfo.getUserno())) {
			//create newUser 
			reciverUserinfo = new Tuserinfo();
			reciverUserinfo.setUserName(orderRequest.getReciverMobile());
			reciverUserinfo.setMobileid(orderRequest.getReciverMobile());
			reciverUserinfo.setPassword(RandomUtil.genRandomNum(6));
			reciverUserinfo.setState(BigDecimal.ONE);
			reciverUserinfo.setType(BigDecimal.ZERO);
			reciverUserinfo.setChannel("991");	//代理
			String param = "userName="+reciverUserinfo.getUserName()+"&mobileid="+reciverUserinfo.getMobileid()
					+"&password="+reciverUserinfo.getPassword()+"&state="+reciverUserinfo.getState()+"&type="
					+reciverUserinfo.getType()+"&channel="+reciverUserinfo.getChannel();
			try {
				HttpUtil.post(lotteryurl+ "/tuserinfoes/register",param);
			} catch (Exception e1) {
				logger.info("代理赠送新注册用户userName:{},userno:{}", new String[] { orderRequest.getReciverMobile(), reciverUserinfo.getUserno() });
				logger.error("请求" + lotteryurl+ "/tuserinfoes/register?userinfo=" + reciverUserinfo + "失败" + e1.getMessage());
			}
			reciverUserinfo = findByUserName(orderRequest.getReciverMobile());
			orderRequest.setUserno(reciverUserinfo.getUserno());
			try{
//				String blessing = new String(orderRequest.getBlessing().getBytes("ISO-8859-1"),"UTF-8");
				PresentMsgContent.createPresentMsgContent(orderRequest);
			} catch(Exception ee){
				ee.printStackTrace();
			}
			String buyuserno = orderRequest.getBuyuserno();
			String userno = orderRequest.getUserno();
			if (BetType.zengsong.value().compareTo(orderRequest.getBettype()) == 0) {
				Tuserinfo buyTuserinfo = lotteryService.findTuserinfoByUserno(buyuserno);
				Tuserinfo tuserinfo = lotteryService.findTuserinfoByUserno(userno);
				if (buyTuserinfo != null && tuserinfo!=null) {
					if(UserAgency.findUserAgencyCount(buyuserno)>0){
						if(UserAgency.findUserAgencyCount(userno) == 0){
							try{
								agencyService.createUserAgency(userno, buyuserno);
							} catch(Exception e) {
								logger.error("代理用户赠送彩票创建代理用户异常,{}", new String[] { e.getMessage() }, e);
							}
						}else{
							logger.info("下线用户已存在,userno:",userno);
						}
					}else{
						logger.info("代理用户不存在,buyuserno:",buyuserno);
					}
				}
			}
		}else{
			throw new RuyicaiException(ErrorCode.UserReg_UserExists);
		}
		//发送短信
		sendMessage(orderRequest.getBuyuserno(),orderRequest.getReciverMobile(),orderRequest.getLotno());
		logger.info("创建代理赠送彩票内容结束,赠送人userno:{},被赠送人username:{}", new String[] { orderRequest.getBuyuserno(),orderRequest.getReciverMobile() });
	}
	
	public Tuserinfo findByUserName(String userName){
		String result = "";
		Tuserinfo userinfo = null ;
		try {
			result = HttpUtil.getResultMessage(lotteryurl+ "/tuserinfoes?find=ByUserName&json&userName=" + userName);
			if (StringUtils.isNotBlank(result)) {
				JSONObject jsonObject = new JSONObject(result);
				String errorCode = jsonObject.getString("errorCode");
				if (errorCode.equals(ErrorCode.OK.value)) {
					String value = jsonObject.getString("value");
					userinfo = Tuserinfo.fromJsonToTuserinfo(value);
				}
			}
		} catch (Exception e) {
			logger.error("请求" + lotteryurl+ "/tuserinfoes?find=ByUserName&json&userName="+ userName + "失败" + e.getMessage());
			throw new RuyicaiException(ErrorCode.ERROR);
		}
		return userinfo;
	}
	
	public void sendMessage(String userno,String mobileId,String lotno){
		String caizhong = Lottype.getMap().get(lotno);
		Tuserinfo userinfo = lotteryService.findTuserinfoByUserno(userno);
		String friendMobile = userinfo.getMobileid()==null?userinfo.getUserName():userinfo.getMobileid();
		String text = "【赠彩通知】您的好友"+friendMobile+"赠送您一笔"+caizhong+"彩票，请您使用账号["+mobileId+"]登录 http://t.cn/8DDiVXw? 找回密码后登录查看,24小时客服 4006651000";
//		String text = "【赠彩通知】您的好友"+friendMobile+"赠送您一笔"+caizhong+"彩票，请您使用账号["+mobileId+"]登录 http://users.ruyicai.com:5507/rchlw/function/rules/findPwd_new.jsp? 找回密码后登录查看,24小时客服 4006651000";
		logger.info("send agencycenter message start time : " + System.nanoTime());
		if(StringUtils.isNotBlank(mobileId)) {
			try {
				String urls = msgcenterurl + "/sms/send";
				String params = "mobileIds=" + mobileId + "&text=" + text;
				sendMessageService.sendMessage(urls, params);
			} catch (Exception e) {
				logger.info("send agencycenter mobileIds " + mobileId + " message error: " + e.getMessage());
			}
		}
		logger.info("send agencycenter message end time : " + System.nanoTime());
	}
	
}
