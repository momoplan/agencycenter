package com.ruyicai.agencycenter.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ruyicai.agencycenter.controller.dto.AgencyPrizeDetailDTO;
import com.ruyicai.agencycenter.controller.dto.UserAgencyDTO;
import com.ruyicai.agencycenter.domain.AgencyPrizeDetail;
import com.ruyicai.agencycenter.domain.UserAgency;
import com.ruyicai.agencycenter.exception.RuyicaiException;
import com.ruyicai.agencycenter.jms.listener.SendAgencyPrizeListener;
import com.ruyicai.agencycenter.service.AgencyService;
import com.ruyicai.agencycenter.service.LotteryService;
import com.ruyicai.agencycenter.util.ErrorCode;
import com.ruyicai.agencycenter.util.JsonUtil;
import com.ruyicai.agencycenter.util.Page;

@Controller
public class AgencyCenterController {

	private Logger logger = LoggerFactory.getLogger(AgencyCenterController.class);

	@Autowired
	private AgencyService agencyService;

	@Autowired
	private LotteryService lotteryService;

	@Autowired
	private SendAgencyPrizeListener sendAgencyPrizeListener;

	/**
	 * 查找代理用户
	 * 
	 * @param userno
	 *            用户编号
	 * @return
	 */
	@RequestMapping(value = "/findUserAgency")
	public @ResponseBody
	ResponseData findUserAgency(@RequestParam("userno") String userno) {
		logger.info("/findUserAgency,userno:" + userno);
		ResponseData rd = new ResponseData();
		ErrorCode result = ErrorCode.OK;
		try {
			rd.setValue(UserAgency.findUserAgency(userno));
		} catch (RuyicaiException e) {
			logger.error("查找代理用户异常,{}", new String[] { e.getMessage() }, e);
			rd.setValue(e.getMessage());
			result = ErrorCode.ERROR;
		} catch (Exception e) {
			logger.error("查找代理用户异常,{}", new String[] { e.getMessage() }, e);
			result = ErrorCode.ERROR;
			rd.setValue(e.getMessage());
		}
		rd.setErrorCode(result.value);
		return rd;
	}

	/**
	 * 查找下级代理数量
	 * 
	 * @param userno
	 *            用户编号
	 * @return
	 */
	@RequestMapping(value = "/findUnderlingCount")
	public @ResponseBody
	ResponseData findUnderlingCount(@RequestParam("userno") String userno) {
		logger.info("/findUnderlingCount,userno:" + userno);
		ResponseData rd = new ResponseData();
		ErrorCode result = ErrorCode.OK;
		try {
			rd.setValue(UserAgency.findUnderlingCount(userno));
		} catch (RuyicaiException e) {
			logger.error("查找下级代理数量异常,{}", new String[] { e.getMessage() }, e);
			rd.setValue(e.getMessage());
			result = ErrorCode.ERROR;
		} catch (Exception e) {
			logger.error("查找下级代理数量异常,{}", new String[] { e.getMessage() }, e);
			result = ErrorCode.ERROR;
			rd.setValue(e.getMessage());
		}
		rd.setErrorCode(result.value);
		return rd;
	}

	/**
	 * 创建代理用户
	 * 
	 * @param userno
	 *            用户编号
	 * @param parentUserno
	 *            上级代理用户编号
	 * @return
	 */
	@RequestMapping(value = "/createUserAgency", method = RequestMethod.POST)
	public @ResponseBody
	ResponseData createUserAgency(@RequestParam(value = "userno") String userno,
			@RequestParam(value = "parentUserno", required = false) String parentUserno) {
		logger.info("/createUserAgency,userno:" + userno + ",parentUserno:" + parentUserno);
		ResponseData rd = new ResponseData();
		ErrorCode result = ErrorCode.OK;
		try {
			rd.setValue(agencyService.createUserAgency(userno, parentUserno));
		} catch (RuyicaiException e) {
			logger.error("创建代理用户异常,{}", new String[] { e.getMessage() }, e);
			rd.setValue(e.getMessage());
			result = ErrorCode.ERROR;
		} catch (Exception e) {
			logger.error("创建代理用户异常,{}", new String[] { e.getMessage() }, e);
			result = ErrorCode.ERROR;
			rd.setValue(e.getMessage());
		}
		rd.setErrorCode(result.value);
		return rd;
	}

	/**
	 * 查询用户代理比率。如果用户不是代理用户,返回空的集合
	 * 
	 * @param userno
	 *            用户编号
	 * @return
	 */
	@RequestMapping(value = "/findAgencyPrecent")
	public @ResponseBody
	ResponseData findAgencyPrecent(@RequestParam(value = "userno") String userno) {
		logger.info("/findAgencyPrecent,userno:" + userno);
		ResponseData rd = new ResponseData();
		ErrorCode result = ErrorCode.OK;
		try {
			rd.setValue(agencyService.findAgencyPercent(userno));
		} catch (RuyicaiException e) {
			logger.error("查询用户代理比率异常,{}", new String[] { e.getMessage() }, e);
			rd.setValue(e.getMessage());
			result = ErrorCode.ERROR;
		} catch (Exception e) {
			logger.error("查询用户代理比率异常,{}", new String[] { e.getMessage() }, e);
			result = ErrorCode.ERROR;
			rd.setValue(e.getMessage());
		}
		rd.setErrorCode(result.value);
		return rd;
	}

	/**
	 * 修改用户代理比率
	 * 
	 * @param userno
	 *            用户编号
	 * @param parentUserno
	 *            上级代理用户编号,即当前登录用户,只有登录用户才可以修改下级用户代理比率
	 * @param lotno
	 *            彩种
	 * @param percent
	 *            用户比率
	 * @param type
	 *            0:普通代购，1:合买发起人,2:合买参与人
	 * @return
	 */
	@RequestMapping(value = "/modifyAgencyPrecent", method = RequestMethod.POST)
	public @ResponseBody
	ResponseData modifyAgencyPrecent(@RequestParam(value = "userno") String userno,
			@RequestParam(value = "parentUserno") String parentUserno, @RequestParam(value = "lotno") String lotno,
			@RequestParam(value = "percent") BigDecimal percent, @RequestParam(value = "type") Integer type) {
		logger.info("/modifyAgencyPrecent,userno:{},parentUserno:{},lotno:{},percent:{}", new String[] { userno,
				parentUserno, lotno, percent + "" });
		ResponseData rd = new ResponseData();
		ErrorCode result = ErrorCode.OK;
		try {
			rd.setValue(agencyService.modifyAgencyPercent(userno, parentUserno, lotno, percent, type));
		} catch (RuyicaiException e) {
			logger.error("修改用户代理比率异常,{}", new String[] { e.getMessage() }, e);
			rd.setValue(e.getMessage());
			result = ErrorCode.ERROR;
		} catch (Exception e) {
			logger.error("修改用户代理比率异常,{}", new String[] { e.getMessage() }, e);
			result = ErrorCode.ERROR;
			rd.setValue(e.getMessage());
		}
		rd.setErrorCode(result.value);
		return rd;
	}

	/**
	 * 修改用户代理比率,mgr专用,非上级用户也可以更改用户比例
	 * 
	 * @param userno
	 *            用户编号
	 * @param lotno
	 *            彩种
	 * @param percent
	 *            用户比率 * @param type 0:普通代购，1:合买发起人,2:合买参与人
	 * @return
	 */
	@RequestMapping(value = "/modifyAgencyPrecentNotValidate", method = RequestMethod.POST)
	public @ResponseBody
	ResponseData modifyAgencyPrecentNotValidate(@RequestParam(value = "userno") String userno,
			@RequestParam(value = "lotno") String lotno, @RequestParam(value = "percent") BigDecimal percent,
			@RequestParam(value = "type") Integer type) {
		logger.info("/modifyAgencyPrecentNotValidate,userno:{},lotno:{},percent:{}", new String[] { userno, lotno,
				percent + "" });
		ResponseData rd = new ResponseData();
		ErrorCode result = ErrorCode.OK;
		try {
			rd.setValue(agencyService.modifyAgencyPrecentNotValidate(userno, lotno, percent, type));
		} catch (RuyicaiException e) {
			logger.error("修改用户代理比率异常,{}", new String[] { e.getMessage() }, e);
			rd.setValue(e.getMessage());
			result = ErrorCode.ERROR;
		} catch (Exception e) {
			logger.error("修改用户代理比率异常,{}", new String[] { e.getMessage() }, e);
			result = ErrorCode.ERROR;
			rd.setValue(e.getMessage());
		}
		rd.setErrorCode(result.value);
		return rd;
	}

	/**
	 * 查询代理用户
	 * 
	 * @param condition
	 *            查询条件json串
	 * @param startLine
	 *            开始记录数
	 * @param endLine
	 *            每页显示记录数
	 * @param orderBy
	 *            排序字段
	 * @param orderDir
	 *            排序条件
	 * @return
	 */
	@RequestMapping(value = "/findAllUserAgency")
	public @ResponseBody
	ResponseData findAllUserAgency(@RequestParam(value = "condition", required = false) String condition,
			@RequestParam(value = "startLine", required = false, defaultValue = "0") int startLine,
			@RequestParam(value = "endLine", required = false, defaultValue = "20") int endLine,
			@RequestParam(value = "orderBy", required = false) String orderBy,
			@RequestParam(value = "orderDir", required = false) String orderDir) {
		logger.info("/findAllUserAgency,condition:{},startLine:{},endLine:{},orderBy:{},orderDir:{}", new String[] {
				condition, startLine + "", endLine + "", orderBy, orderDir });
		ResponseData rd = new ResponseData();
		ErrorCode result = ErrorCode.OK;
		Page<UserAgency> page = new Page<UserAgency>(startLine, endLine, orderBy, orderDir);
		try {
			Map<String, Object> conditionMap = JsonUtil.transferJson2Map(condition);
			UserAgency.findUserAgency(conditionMap, page);
			List<UserAgency> list = page.getList();
			List<UserAgencyDTO> resultList = new ArrayList<UserAgencyDTO>();
			for (UserAgency userAgency : list) {
				UserAgencyDTO dto = new UserAgencyDTO();
				dto.setUserAgency(userAgency);
				String userno = userAgency.getUserno();
				if (StringUtils.isNotBlank(userno)) {
					dto.setTuserinfo(lotteryService.findTuserinfoByUserno(userno));
				}
				String parentUserno = userAgency.getParentUserno();
				if (StringUtils.isNotBlank(parentUserno)) {
					dto.setParentTuserinfo(lotteryService.findTuserinfoByUserno(parentUserno));
				}
				resultList.add(dto);
			}
			rd.setValue(new Page<UserAgencyDTO>(startLine, endLine, page.getTotalResult(), orderBy, orderDir,
					resultList));
		} catch (RuyicaiException e) {
			logger.error("查询代理用户异常,{}", new String[] { e.getMessage() }, e);
			rd.setValue(e.getMessage());
			result = ErrorCode.ERROR;
		} catch (Exception e) {
			logger.error("查询代理用户异常,{}", new String[] { e.getMessage() }, e);
			result = ErrorCode.ERROR;
			rd.setValue(e.getMessage());
		}
		rd.setErrorCode(result.value);
		return rd;
	}

	/**
	 * 查询代理奖金明细
	 * 
	 * @param condition
	 *            查询条件json串
	 * @param startLine
	 *            开始记录数
	 * @param endLine
	 *            每页显示记录数
	 * @param orderBy
	 *            排序字段
	 * @param orderDir
	 *            排序条件
	 * @return
	 */
	@RequestMapping(value = "/findAllAgencyPrizeDetail")
	public @ResponseBody
	ResponseData findAllAgencyPrizeDetail(@RequestParam(value = "condition", required = false) String condition,
			@RequestParam(value = "startLine", required = false, defaultValue = "0") int startLine,
			@RequestParam(value = "endLine", required = false, defaultValue = "20") int endLine,
			@RequestParam(value = "orderBy", required = false) String orderBy,
			@RequestParam(value = "orderDir", required = false) String orderDir) {
		logger.info("/findAllAgencyPrizeDetail,condition:{},startLine:{},endLine:{},orderBy:{},orderDir:{}",
				new String[] { condition, startLine + "", endLine + "", orderBy, orderDir });
		ResponseData rd = new ResponseData();
		ErrorCode result = ErrorCode.OK;
		Page<AgencyPrizeDetail> page = new Page<AgencyPrizeDetail>(startLine, endLine, orderBy, orderDir);
		try {
			Map<String, Object> conditionMap = JsonUtil.transferJson2Map(condition);
			AgencyPrizeDetail.findAgencyPrizeDetail(conditionMap, page);
			List<AgencyPrizeDetail> list = page.getList();
			List<AgencyPrizeDetailDTO> resultList = new ArrayList<AgencyPrizeDetailDTO>();
			for (AgencyPrizeDetail agencyPrizeDetail : list) {
				AgencyPrizeDetailDTO dto = new AgencyPrizeDetailDTO();
				dto.setAgencyPrizeDetail(agencyPrizeDetail);
				String userno = agencyPrizeDetail.getUserno();
				if (StringUtils.isNotBlank(userno)) {
					dto.setTuserinfo(lotteryService.findTuserinfoByUserno(userno));
				}
				String childUserno = agencyPrizeDetail.getChildUserno();
				if (StringUtils.isNotBlank(childUserno)) {
					dto.setChildTuserinfo(lotteryService.findTuserinfoByUserno(childUserno));
				}
				resultList.add(dto);
			}
			Page<AgencyPrizeDetailDTO> resultValue = new Page<AgencyPrizeDetailDTO>(startLine, endLine,
					page.getTotalResult(), orderBy, orderDir, resultList);
			resultValue.setValue(AgencyPrizeDetail.findAgencyPrizeDetailSum(conditionMap));
			rd.setValue(resultValue);
		} catch (RuyicaiException e) {
			logger.error("查询代理奖金明细异常,{}", new String[] { e.getMessage() }, e);
			rd.setValue(e.getMessage());
			result = ErrorCode.ERROR;
		} catch (Exception e) {
			logger.error("查询代理奖金明细异常,{}", new String[] { e.getMessage() }, e);
			result = ErrorCode.ERROR;
			rd.setValue(e.getMessage());
		}
		rd.setErrorCode(result.value);
		return rd;
	}

	@RequestMapping(value = "/manualSendPrize", method = RequestMethod.POST)
	public @ResponseBody
	ResponseData sendPrize() {
		logger.info("/manualSendPrize");
		ResponseData rd = new ResponseData();
		ErrorCode result = ErrorCode.OK;
		try {
			List<AgencyPrizeDetail> list = AgencyPrizeDetail.findFailingAgencyPrizeDetails();
			for (AgencyPrizeDetail detail : list) {
				sendAgencyPrizeListener.sendAgencyPrizeCustomer(detail.getId());
			}
			rd.setValue(list.size() + "条数据执行成功");
		} catch (RuyicaiException e) {
			logger.error("手工派送派送失败的奖金异常,{}", new String[] { e.getMessage() }, e);
			rd.setValue(e.getMessage());
			result = ErrorCode.ERROR;
		} catch (Exception e) {
			logger.error("手工派送派送失败的奖金异常,{}", new String[] { e.getMessage() }, e);
			result = ErrorCode.ERROR;
			rd.setValue(e.getMessage());
		}
		rd.setErrorCode(result.value);
		return rd;
	}
}
