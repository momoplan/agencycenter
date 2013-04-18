package com.ruyicai.lottery.domain;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooJson
@RooToString
public class CaseLot {

	private String id;

	private String starter;

	/** 合买最小购买金额。也代表每注多少钱oneamount */
	private Long minAmt;

	private Long totalAmt;

	private Long safeAmt;

	private Long buyAmtByStarter;
	/** 佣金比例 */
	private BigDecimal commisionRatio;

	private Date startTime;

	private Date endTime;
	/** 显示类型 */
	private Integer visibility;

	private String description;

	private String title;
	/** 交易状态 */
	private BigDecimal state;
	/** 用户看到的状态 */
	private BigDecimal displayState;
	/** 用户看到的状态描述 */
	private String displayStateMemo;

	private String content;

	private String orderid;

	private String lotno;

	private String batchcode;

	private BigDecimal winFlag;
	/** 暂时没用 */
	private Long winLittleAmt;
	/** 税后奖金 */
	private Long winBigAmt;
	/** 税前奖金 */
	private Long winPreAmt;
	/** 中奖时间 */
	private Date winStartTime;

	private Date winEndTime;

	private String winDetail;

	private String caselotinfo;
	/** 参与人购买金额 */
	private long buyAmtByFollower;
	/** 0：普通合买，3：申请置顶合买大厅，4:申请置顶合买中心，8：置顶合买大厅，9：置顶合买中心 */
	private Integer sortState;
	/** 参与人数 */
	private Long participantCount;
	/** 方案类型 */
	private BigDecimal lotsType;
	/** 是否有战绩 0：没有，1：有 */
	private BigDecimal hasachievement;
	/** 是否中奖 0：没有，1：有.(取消的合买也会有中奖状态) */
	private BigDecimal isWinner;
}
