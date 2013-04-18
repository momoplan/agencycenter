package com.ruyicai.agencycenter.service;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ruyicai.agencycenter.controller.ResponseData;
import com.ruyicai.agencycenter.exception.RuyicaiException;
import com.ruyicai.agencycenter.util.ErrorCode;
import com.ruyicai.agencycenter.util.HttpUtil;
import com.ruyicai.agencycenter.util.JsonUtil;
import com.ruyicai.agencycenter.util.StringUtil;
import com.ruyicai.lottery.domain.Tuserinfo;

@Service
public class LotteryService {

	private Logger logger = LoggerFactory.getLogger(LotteryService.class);

	@Autowired
	MemcachedService<String> memcachedService;

	@Value("${lotteryurl}")
	String lotteryurl;

	/**
	 * @param userno
	 *            用户编号
	 * @return Tuserinfo
	 */
	public Tuserinfo findTuserinfoByUserno(String userno) {
		if (StringUtils.isBlank(userno)) {
			throw new IllegalArgumentException("the argument userno is required");
		}
		Tuserinfo tuserinfo = null;
		String url = lotteryurl + "/tuserinfoes?find=ByUserno&json&userno=" + userno;
		try {
			String userJson = memcachedService.get(StringUtil.join("_", "Tuserinfo", userno));
			if (StringUtils.isNotBlank(userJson)) {
				tuserinfo = Tuserinfo.fromJsonToTuserinfo(userJson);
			}
			if (tuserinfo != null) {
				logger.info("found user from cache,userno:" + userno);
				return tuserinfo;
			}
			logger.info("find user from lottery,userno:" + userno);
			String result = HttpUtil.getResultMessage(url.toString());
			if (StringUtils.isNotBlank(result)) {
				JSONObject jsonObject = new JSONObject(result);
				String errorCode = jsonObject.getString("errorCode");
				if (errorCode.equals(ErrorCode.OK.value)) {
					String value = jsonObject.getString("value");
					tuserinfo = Tuserinfo.fromJsonToTuserinfo(value);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("请求" + url + "失败" + e.getMessage());
			throw new RuyicaiException(ErrorCode.ERROR);
		}
		return tuserinfo;
	}

	/**
	 * 赠送彩金
	 * 
	 * @param userno
	 * @param amt
	 * @param subchannel
	 * @param channel
	 * @param memo
	 * @return
	 */
	public Boolean directChargeProcess(String userno, BigDecimal amt, String subchannel, String channel, String memo) {
		Boolean flag = false;
		if (StringUtils.isBlank(userno)) {
			throw new IllegalArgumentException("the argument mobileid is required");
		}
		if (amt == null) {
			throw new IllegalArgumentException("the argument mobileid is required");
		}
		logger.info("赠送彩金 user:{},amt:{}", new String[] { userno, amt.toString() });
		String subchannelStr = subchannel == null ? " " : subchannel;
		String channelStr = channel == null ? " " : channel;
		String url = lotteryurl + "/taccounts/doDirectChargeProcess";
		StringBuffer params = new StringBuffer();
		params.append("userno=" + userno).append("&amt=" + amt.toString()).append("&accesstype=2").append("&draw=1")
				.append("&subchannel=" + subchannelStr).append("&channel=" + channelStr);
		if (memo != null) {
			params.append("&memo=" + memo);
		}
		try {
			String result = HttpUtil.post(url, params.toString());
			ResponseData rd = JsonUtil.fromJsonToObject(result, ResponseData.class);
			if (rd.getErrorCode().equals("0")) {
				flag = true;
			} else {
				logger.error("赠送彩金错误信息:" + rd.getValue());
				flag = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("请求" + url + ",参数 " + params.toString() + ".失败" + e.getMessage());
			throw new RuyicaiException("请求lottery失败");
		}
		return flag;
	}

	/**
	 * 修改用户渠道
	 * 
	 * @param userno
	 *            用户编号
	 * @return channel 用户渠道
	 */
	@Async
	public void modifyTuserinfo(String userno, String channel) {
		if (StringUtils.isBlank(userno) || StringUtils.isBlank(channel)) {
			throw new IllegalArgumentException("the argument userno is required");
		}
		String url = lotteryurl + "/tuserinfoes/modify";
		StringBuffer params = new StringBuffer();
		params.append("userno=" + userno).append("&channel=" + channel);
		try {
			String result = HttpUtil.post(url, params.toString());
			if (StringUtils.isNotBlank(result)) {
				JSONObject jsonObject = new JSONObject(result);
				String errorCode = jsonObject.getString("errorCode");
				if (errorCode.equals(ErrorCode.OK.value)) {
					logger.info("用户" + userno + "渠道修改为" + channel);
				} else {
					logger.error("修改用户信息异常:" + result);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("请求" + url + ",参数 " + params.toString() + ".失败" + e.getMessage());
			throw new RuyicaiException("请求lottery失败");
		}
	}
}
