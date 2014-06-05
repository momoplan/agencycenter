package com.ruyicai.agencycenter.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ruyicai.agencycenter.domain.OrderRequest;
import com.ruyicai.agencycenter.exception.RuyicaiException;
import com.ruyicai.agencycenter.service.PresentService;
import com.ruyicai.agencycenter.util.ErrorCode;
import com.ruyicai.agencycenter.util.JsonUtil;

/**
 * @Description: 代理相关赠送彩票
 * 
 * @author chuang   
 * @date 2014年5月22日 上午9:37:33 
 * @version V1.0   
 *
 */
@Controller
public class PresentController {

	private Logger logger = LoggerFactory.getLogger(PresentController.class);
	
	@Value("${lotteryurl}")
	String lotteryurl;
	
	@Autowired
	PresentService presentService;
	
	@RequestMapping(value = "/saveagencypresent", method = RequestMethod.POST)
	public @ResponseBody
	ResponseData savepresent(@RequestParam("body") String body) {
		ResponseData rd = new ResponseData();
		ErrorCode result = ErrorCode.OK;
		try {
			logger.info("代理赠送彩票 请求参数为：" + body);
			OrderRequest orderRequest = JsonUtil.fromJsonToObject(body, OrderRequest.class);
			presentService.savePresentMsgContent(orderRequest);
		} catch (RuyicaiException e) {
			logger.error("代理用户赠送出错,错误是{},{}", new String[] { e.getErrorCode().memo, body }, e);
			result = e.getErrorCode();
		} catch (Exception e) {
			logger.error("代理用户赠送出错,错误是,{},{}", new String[] { e.getMessage(), body }, e);
			result = ErrorCode.ERROR;
			rd.setValue(e.getMessage());
		}
		rd.setErrorCode(result.value);
		return rd;
	}
	
}
