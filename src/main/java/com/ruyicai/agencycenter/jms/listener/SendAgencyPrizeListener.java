package com.ruyicai.agencycenter.jms.listener;

import org.apache.camel.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruyicai.agencycenter.domain.AgencyPrizeDetail;
import com.ruyicai.agencycenter.service.LotteryService;
import com.ruyicai.lottery.domain.Tuserinfo;

@Service
public class SendAgencyPrizeListener {

	private Logger logger = LoggerFactory.getLogger(SendAgencyPrizeListener.class);

	@Autowired
	private LotteryService lotteryService;

	@Transactional
	public void sendAgencyPrizeCustomer(@Header("prizeDetailId") Long prizeDetailId) {
		logger.info("发送奖励prizeDetailId:{}", new String[] { prizeDetailId + "" });
		String memo = "代理奖金";
		AgencyPrizeDetail detail = AgencyPrizeDetail.findAgencyPrizeDetail(prizeDetailId, true);
		if (detail != null && detail.getState() == 0) {
			Tuserinfo tuserinfo = lotteryService.findTuserinfoByUserno(detail.getUserno());
			Boolean flag = lotteryService.directChargeProcess(tuserinfo.getUserno(), detail.getPrizeAmt(),
					tuserinfo.getSubChannel(), tuserinfo.getChannel(), memo);
			if (flag) {
				detail.setState(1);
				logger.info("TuserPrizeDetail id:" + prizeDetailId + ",userno:" + detail.getUserno() + ",amt:"
						+ detail.getPrizeAmt() + ".奖励成功");
			} else {
				detail.setState(2);
				logger.error("TuserPrizeDetail id:" + prizeDetailId + ".奖励失败");
			}
			detail.merge();
		}
	}
}
