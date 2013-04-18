package com.ruyicai.agencycenter.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

/**
 * 代理用户购彩情况
 */
@RooJavaBean
@RooToString
@RooJson
@RooEntity(versionField = "", table = "AGENCYBUYDETAIL", identifierField = "id")
public class AgencyBuyDetail {

	/** 用户编号 */
	@Column(name = "USERNO", length = 32)
	private String userno;

	/** 业务ID */
	@Column(name = "BUSINESSID", length = 50)
	private String businessId;

	/** 业务类型 */
	@Column(name = "BUSINESSTYPE")
	private Integer businessType;

	/** 彩种 */
	@Column(name = "LOTNO", length = 20)
	private String lotno;

	/** 购买金额 */
	@Column(name = "BUYAMT", columnDefinition = "decimal")
	private BigDecimal buyAmt;

	/** 购彩时的返点比例 */
	@Column(name = "PERCENT")
	private BigDecimal percent;

	/** 创建时间 */
	@Column(name = "CREATETIME")
	private Date createTime;

	public static AgencyBuyDetail createAgencyBuyDetail(String userno, String businessId, Integer businessType,
			String lotno, BigDecimal buyAmt, BigDecimal percent) {
		AgencyBuyDetail agencyBuyDetail = new AgencyBuyDetail();
		agencyBuyDetail.setUserno(userno);
		agencyBuyDetail.setBusinessId(businessId);
		agencyBuyDetail.setBusinessType(businessType);
		agencyBuyDetail.setLotno(lotno);
		agencyBuyDetail.setBuyAmt(buyAmt);
		agencyBuyDetail.setPercent(percent);
		agencyBuyDetail.setCreateTime(new Date());
		agencyBuyDetail.persist();
		return agencyBuyDetail;
	}
}
