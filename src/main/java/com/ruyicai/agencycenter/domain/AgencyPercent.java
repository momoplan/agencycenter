package com.ruyicai.agencycenter.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.EntityManager;

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

	@EmbeddedId
	private AgencyPercentPK id;

	@Column(name = "PERCENT")
	private BigDecimal percent;

	@Column(name = "LASTMODIFYTIME")
	private Date lastmodifyTime;

	/**
	 * 如果不存在则创建用户彩种代理比率
	 * 
	 * @param userno
	 *            用户编号
	 * @param lotno
	 *            彩种编号
	 * @return AgencyPercent
	 */
	public static AgencyPercent findOrCreateAgencyPercent(String userno, String lotno) {
		if (StringUtils.isBlank(userno)) {
			throw new IllegalArgumentException("the arguments userno is require");
		}
		if (StringUtils.isBlank(lotno)) {
			throw new IllegalArgumentException("the arguments lotno is require");
		}
		AgencyPercentPK id = new AgencyPercentPK(userno, lotno);
		AgencyPercent agencyPercent = findAgencyPercent(id);
		if (agencyPercent == null) {
			agencyPercent = new AgencyPercent();
			agencyPercent.setId(id);
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
	 * @param lotno
	 *            彩种编号
	 * @return AgencyPercent
	 */
	public static List<AgencyPercent> findAgencyPercentsByUserno(String userno) {
		if (StringUtils.isBlank(userno)) {
			throw new IllegalArgumentException("the arguments userno is require");
		}
		EntityManager em = AgencyPercent.entityManager();
		return em
				.createQuery(
						"SELECT o FROM AgencyPercent AS o WHERE o.id.userno = :userno order by o.id.lotno asc,o.id.type asc",
						AgencyPercent.class).setParameter("userno", userno).getResultList();
	}
}
