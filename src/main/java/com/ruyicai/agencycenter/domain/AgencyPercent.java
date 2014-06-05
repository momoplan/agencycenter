package com.ruyicai.agencycenter.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.Id;

import org.apache.commons.lang.StringUtils;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

/**
 * 用户彩种代理比率
 */
@RooJavaBean
@RooToString
@RooEntity(versionField = "", table = "AGENCYPERCENT", identifierField = "id")
public class AgencyPercent {

	@Id
	@Column(name = "USERNO", length = 32)
	private String userno;

	@Column(name = "PERCENT")
	private BigDecimal percent;

	@Column(name = "LASTMODIFYTIME")
	private Date lastmodifyTime;

	/**
	 * 如果不存在则创建用户彩种代理比率
	 * 
	 * @param userno
	 *            用户编号
	 * @return AgencyPercent
	 */
	public static AgencyPercent findOrCreateAgencyPercent(String userno) {
		if (StringUtils.isBlank(userno)) {
			throw new IllegalArgumentException("the arguments userno is require");
		}
		AgencyPercent agencyPercent = findAgencyPercent(userno);
		if (agencyPercent == null) {
			agencyPercent = new AgencyPercent();
			agencyPercent.setUserno(userno);
			agencyPercent.setPercent(BigDecimal.ZERO);
			agencyPercent.setLastmodifyTime(new Date());
			agencyPercent.persist();
		}
		return agencyPercent;
	}

	/**
	 * 按用户编号查询
	 * 
	 * @param userno
	 *            用户编号
	 * @return AgencyPercent
	 */
	public static AgencyPercent findAgencyPercentsByUserno(String userno) {
		if (StringUtils.isBlank(userno)) {
			throw new IllegalArgumentException("the arguments userno is require");
		}
		EntityManager em = AgencyPercent.entityManager();
		return em
				.createQuery(
						"SELECT o FROM AgencyPercent AS o WHERE o.userno = :userno ",
						AgencyPercent.class).setParameter("userno", userno).getResultList().get(0);
	}
}
