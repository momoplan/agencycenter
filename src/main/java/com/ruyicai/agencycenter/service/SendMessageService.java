package com.ruyicai.agencycenter.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ruyicai.agencycenter.util.HttpUtil;

/**
 * 异步发送短信服务类
 */
@Service
public class SendMessageService {

	private Logger logger = LoggerFactory.getLogger(SendMessageService.class);

	/**
	 * 异步发送短信.
	 * @param url 请求地址
	 * @param param  请求参数
	 */
	@Async
	public void sendMessage(String url, String param)
	{
		try {
			HttpUtil.post(url, param);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("send url:" + url + ",param:" + param +" error: " + e.getMessage());
		}
	}
}
