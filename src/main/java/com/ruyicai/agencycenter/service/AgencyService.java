package com.ruyicai.agencycenter.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruyicai.agencycenter.domain.AgencyBuyDetail;
import com.ruyicai.agencycenter.domain.AgencyPercent;
import com.ruyicai.agencycenter.domain.AgencyPercentPK;
import com.ruyicai.agencycenter.domain.AgencyPrizeDetail;
import com.ruyicai.agencycenter.domain.Tjmsservice;
import com.ruyicai.agencycenter.domain.UserAgency;
import com.ruyicai.agencycenter.exception.RuyicaiException;
import com.ruyicai.agencycenter.util.ErrorCode;
import com.ruyicai.agencycenter.util.Lottype;
import com.ruyicai.lottery.domain.Tuserinfo;

@Service
public class AgencyService {

	private Logger logger = LoggerFactory.getLogger(LotteryService.class);

	@Autowired
	private LotteryService lotteryService;

	@Produce(uri = "jms:topic:sendAgencyPrize")
	private ProducerTemplate sendAgencyPrizeProducer;

	/**
	 * 创建代理用户
	 * 
	 * @param userno
	 *            代理用户编号
	 * @param parentUserno
	 *            上级代理用户编号
	 * @return UserAgency
	 */
	public UserAgency createUserAgency(String userno, String parentUserno) {
		logger.info("创建代理用户userno:{},parentUserno:{}", new String[] { userno, parentUserno });
		UserAgency userAgency = UserAgency.findUserAgency(userno);
		if (userAgency != null) {
			throw new RuyicaiException(ErrorCode.AGENCYCENTER_HAS_EXISTS);
		}
		Tuserinfo tuserinfo = lotteryService.findTuserinfoByUserno(userno);
		if (tuserinfo == null) {
			throw new RuyicaiException(ErrorCode.UserMod_UserNotExists);
		}
		Tuserinfo parenttuserinfo = null;
		UserAgency parentUserAgency = null;
		if (StringUtils.isNotBlank(parentUserno)) {
			parenttuserinfo = lotteryService.findTuserinfoByUserno(parentUserno);
			if (parenttuserinfo == null) {
				throw new RuyicaiException(ErrorCode.UserMod_UserNotExists);
			}
			parentUserAgency = UserAgency.findUserAgency(parentUserno);
			if (parentUserAgency == null) {
				throw new RuyicaiException(ErrorCode.AGENCYCENTER_PARENTUSER_NOTEXISTS);
			}
		}
		UserAgency newUserAgency = UserAgency.createUserAgency(userno, parentUserAgency);
		String channel = parenttuserinfo == null ? "991" : parenttuserinfo.getChannel();
		lotteryService.modifyTuserinfo(newUserAgency.getUserno(), channel);
		logger.info("创建代理用户结束");
		return newUserAgency;
	}

	/**
	 * 查找用户代理比率
	 * 
	 * @param userno
	 * @return List<AgencyPercent>
	 */
	public List<AgencyPercent> findAgencyPercent(String userno) {
		List<AgencyPercent> result = new ArrayList<AgencyPercent>();
		UserAgency userAgency = UserAgency.findUserAgency(userno);
		if (userAgency == null) {
			return result;
		}
		result = AgencyPercent.findAgencyPercentsByUserno(userno);
		Map<String, String> map = Lottype.getMap();
		Boolean flag = true;
//		Integer[] typeArray = new Integer[] { 0, 1, 2 };
//		for (Integer type : typeArray) {
			for (String lotno : map.keySet()) {
				flag = true;
				for (AgencyPercent agencyPercent : result) {
					if (agencyPercent.getId().getLotno().equals(lotno)) {
						flag = false;
						continue;
					}
				}
				if (flag) {
					result.add(AgencyPercent.findOrCreateAgencyPercent(userno, lotno));
				}
			}
//		}
		return result;
	}

	/**
	 * 计算代理奖金
	 * 
	 * @param userno
	 *            用户编号
	 * @param businessId
	 *            业务ID
	 * @param businessType
	 *            业务类型
	 * @param lotno
	 *            彩种
	 * @param buyAmt
	 *            购买金额
	 */
	public void doAgencyPrize(String userno, String businessId, Integer businessType, String lotno, BigDecimal buyAmt) {
		UserAgency userAgency = UserAgency.findUserAgency(userno);
		if (userAgency != null) {
			if (!Tjmsservice.createTjmsservice(userno + businessId, businessType)) {
				logger.error("代理奖金已派发userno:" + userno + ",businessId:" + businessId + ",businessType:" + businessType
						+ ",lotno:" + lotno + ",buyAmt:" + buyAmt);
				return;
			}
			// 记录代理用户购彩明细
			try {
				AgencyPercent agencyPercent = AgencyPercent.findOrCreateAgencyPercent(userAgency.getUserno(), lotno);
				AgencyBuyDetail.createAgencyBuyDetail(userno, businessId, businessType, lotno, buyAmt,
						agencyPercent.getPercent());
			} catch (Exception e) {
				logger.error("创建代理购彩记录异常", e);
			}
			while (doSendAgencyPrize(userAgency, null, businessId, businessType, lotno, buyAmt) == false) {
				break;
			}
		}
	}

	/**
	 * 递归查询所有代理，并发送奖金
	 * 
	 * @param userAgency
	 *            代理用户
	 * @param childUserAgency
	 *            下级代理用户
	 * @param businessId
	 *            业务ID
	 * @param businessType
	 *            业务类型
	 * @param lotno
	 *            彩种
	 * @param totalAmt
	 *            购买金额
	 * @return Boolean
	 */
	@Transactional
	public Boolean doSendAgencyPrize(UserAgency userAgency, UserAgency childUserAgency, String businessId,
			Integer businessType, String lotno, BigDecimal totalAmt) {
		Boolean flag = false;
		if (userAgency == null) {
			logger.info("代理账号为空，跳出");
			return flag;
		}
		String userno = userAgency.getUserno();
		String childUserno = childUserAgency == null ? null : childUserAgency.getUserno();
		AgencyPercent agencyPercent = AgencyPercent.findOrCreateAgencyPercent(userAgency.getUserno(), lotno);
		BigDecimal percent = agencyPercent.getPercent();
		if (childUserAgency != null) {
			AgencyPercent childAgencyPercent = AgencyPercent.findOrCreateAgencyPercent(childUserAgency.getUserno(),
					lotno);
			if (childAgencyPercent != null && childAgencyPercent.getPercent() != null
					&& childAgencyPercent.getPercent().compareTo(BigDecimal.ZERO) > 0) {
				percent = percent.subtract(childAgencyPercent.getPercent());
				logger.info("用户userno:{},businessId:{},percent{}:", new String[] { userno, businessId, percent + "" });
			}
		} else {
			logger.info("用户userno:{},businessId:{},percent:{}", new String[] { userno, businessId, percent + "" });
		}
		BigDecimal prizeAmt = totalAmt.multiply(percent).divide(new BigDecimal(100), 0, BigDecimal.ROUND_HALF_UP);
		if (prizeAmt.compareTo(BigDecimal.ZERO) > 0) {
			logger.info("发送代理奖金userno:{},childUserno:{},businessId:{},businessType:{},lotno:{},prize:{}", new String[] {
					userno, childUserno, businessId, businessType + "", lotno, prizeAmt + "" });
			sendPrize2UserJMS(userno, childUserno, businessId, businessType, lotno, prizeAmt, totalAmt);
		} else {
			logger.info("用户userno:{},businessId:{}代理奖金计算小于等于零，不派发奖金，", new String[] { userno, businessId });
		}
		String parentUserno = userAgency.getParentUserno();
		if (StringUtils.isNotBlank(parentUserno)) {
			logger.info("计算上级代理用户用户奖金parentUserno:{}", new String[] { parentUserno });
			UserAgency parentUserAgency = UserAgency.findUserAgency(parentUserno);
			doSendAgencyPrize(parentUserAgency, userAgency, businessId, businessType, lotno, totalAmt);
			flag = true;
		}
		return flag;
	}

	/**
	 * 创建发送代理奖金JMS消息
	 */
	@Transactional
	public void sendPrize2UserJMS(String userno, String childUserno, String businessId, Integer businessType,
			String lotno, BigDecimal prizeAmt, BigDecimal totalAmt) {
		AgencyPrizeDetail agencyPrizeDetail = AgencyPrizeDetail.createAgencyPrizeDetail(userno, childUserno,
				businessId, businessType, lotno, prizeAmt, totalAmt);
		Map<String, Object> headers = new HashMap<String, Object>();
		headers.put("prizeDetailId", agencyPrizeDetail.getId());
		sendAgencyPrizeProducer.sendBodyAndHeaders(null, headers);
	}

	public AgencyPercent modifyAgencyPercent(String userno, String parentUserno, String lotno, BigDecimal percent) {
		logger.info("修改用户代理比例userno:{},parentUserno:{},lotno:{},percent:{}", new String[] { userno, parentUserno,
				lotno, percent + "" });
		UserAgency agency = UserAgency.findUserAgency(userno);
		if (agency == null) {
			throw new RuyicaiException(ErrorCode.AGENCYCENTER_USER_NOTEXISTS);
		}
		if (agency.getLevel() != 1) {
			if (StringUtils.isBlank(parentUserno)) {
				throw new RuyicaiException(ErrorCode.ERROR);
			}
			if (!agency.getParentUserno().equals(parentUserno)) {
				throw new RuyicaiException(ErrorCode.AGENCYCENTER_MODIFY_PERCENT_NOT_PARENT);
			}
			AgencyPercent parentAgencyPercent = AgencyPercent.findAgencyPercent(new AgencyPercentPK(parentUserno,
					lotno));
			if (parentAgencyPercent == null) {
				throw new RuyicaiException(ErrorCode.AGENCYCENTER_DATA_NOTEXISTS);
			}
			if (percent.compareTo(parentAgencyPercent.getPercent()) > 0) {
				throw new RuyicaiException(ErrorCode.AGENCYCENTER_CHILD_NOT_BIGGER_PARENT);
			}
		}
		AgencyPercent agencyPercent = AgencyPercent.findAgencyPercent(new AgencyPercentPK(userno, lotno));
		if (agencyPercent == null) {
			throw new RuyicaiException(ErrorCode.ERROR);
		}
		agencyPercent.setPercent(percent);
		agencyPercent.setLastmodifyTime(new Date());
		agencyPercent.merge();
		logger.info("修改用户代理比例成功");
		return agencyPercent;
	}

	public AgencyPercent modifyAgencyPrecentNotValidate(String userno, String lotno, BigDecimal percent) {
		logger.info("修改用户代理比例userno:{},lotno:{},percent:{}", new String[] { userno, lotno, percent + "" });
		UserAgency agency = UserAgency.findUserAgency(userno);
		if (agency == null) {
			throw new RuyicaiException(ErrorCode.AGENCYCENTER_USER_NOTEXISTS);
		}
		if (StringUtils.isNotBlank(agency.getParentUserno())) {
			String parentUserno = agency.getParentUserno();
			AgencyPercent parentAgencyPercent = AgencyPercent.findAgencyPercent(new AgencyPercentPK(parentUserno,
					lotno));
			if (parentAgencyPercent == null) {
				throw new RuyicaiException(ErrorCode.AGENCYCENTER_DATA_NOTEXISTS);
			}
			if (percent.compareTo(parentAgencyPercent.getPercent()) > 0) {
				throw new RuyicaiException(ErrorCode.AGENCYCENTER_CHILD_NOT_BIGGER_PARENT);
			}
		}
		AgencyPercent agencyPercent = AgencyPercent.findAgencyPercent(new AgencyPercentPK(userno, lotno));
		if (agencyPercent == null) {
			throw new RuyicaiException(ErrorCode.ERROR);
		}
		agencyPercent.setPercent(percent);
		agencyPercent.setLastmodifyTime(new Date());
		agencyPercent.merge();
		logger.info("修改用户代理比例成功");
		return agencyPercent;
	}
}
