package com.ruyicai.agencycenter.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.commons.lang.StringUtils;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import com.ruyicai.agencycenter.util.Page;
import com.ruyicai.agencycenter.util.Page.Sort;
import com.ruyicai.agencycenter.util.PropertyFilter;

/**
 * 用户奖金派发记录
 */
@RooJavaBean
@RooToString
@RooJson
@RooEntity(versionField = "", table = "AGENCYPRIZEDETAIL", identifierField = "id")
public class AgencyPrizeDetail {

	/** 用户编号 */
	@Column(name = "USERNO", length = 32)
	private String userno;

	/** 下级用户编号 */
	@Column(name = "CHILDUSERNO", length = 32)
	private String childUserno;

	/** 业务ID */
	@Column(name = "BUSINESSID", length = 50)
	private String businessId;

	/** 业务类型 */
	@Column(name = "BUSINESSTYPE")
	private Integer businessType;

	/** 彩种 */
	@Column(name = "LOTNO", length = 20)
	private String lotno;

	/** 奖金 */
	@Column(name = "PRIZEAMT", columnDefinition = "decimal")
	private BigDecimal prizeAmt;

	/** 方案金额 */
	@Column(name = "TOTALAMT", columnDefinition = "decimal")
	private BigDecimal totalAmt;

	/** 创建时间 */
	@Column(name = "CREATETIME")
	private Date createTime;

	/** 奖金是否派发成功。0：等待派发，1：派发成功 ，2：派发失败 */
	@Column(name = "STATE")
	private Integer state = 0;

	@Transactional
	public static AgencyPrizeDetail createAgencyPrizeDetail(String userno, String childUserno, String businessId,
			Integer businessType, String lotno, BigDecimal prizeAmt, BigDecimal totalAmt) {
		if (StringUtils.isBlank(userno)) {
			throw new IllegalArgumentException("the argument parentAgentUserno is required");
		}
		if (prizeAmt == null) {
			throw new IllegalArgumentException("the argument amt is required");
		}
		if (StringUtils.isBlank(businessId)) {
			throw new IllegalArgumentException("the argument businessId is required");
		}
		if (businessType == null) {
			throw new IllegalArgumentException("the argument businessType is required");
		}
		AgencyPrizeDetail agencyPrizeDetail = new AgencyPrizeDetail();
		agencyPrizeDetail.setUserno(userno);
		agencyPrizeDetail.setChildUserno(childUserno);
		agencyPrizeDetail.setBusinessId(businessId);
		agencyPrizeDetail.setBusinessType(businessType);
		agencyPrizeDetail.setLotno(lotno);
		agencyPrizeDetail.setPrizeAmt(prizeAmt);
		agencyPrizeDetail.setTotalAmt(totalAmt);
		agencyPrizeDetail.setCreateTime(new Date());
		agencyPrizeDetail.setState(0);
		agencyPrizeDetail.persist();
		return agencyPrizeDetail;
	}

	public static AgencyPrizeDetail findAgencyPrizeDetail(Long id, boolean lock) {
		EntityManager entityManager = AgencyPrizeDetail.entityManager();
		AgencyPrizeDetail detail = entityManager.find(AgencyPrizeDetail.class, id,
				lock ? LockModeType.PESSIMISTIC_WRITE : LockModeType.NONE);
		return detail;
	}

	public static List<AgencyPrizeDetail> findFailingAgencyPrizeDetails() {
		EntityManager em = AgencyPrizeDetail.entityManager();
		return em.createQuery("SELECT o FROM AgencyPrizeDetail o WHERE o.state != '1' ", AgencyPrizeDetail.class)
				.getResultList();
	}

	public static void findAgencyPrizeDetail(Map<String, Object> conditionMap, Page<AgencyPrizeDetail> page) {
		EntityManager em = AgencyPrizeDetail.entityManager();
		String sql = "SELECT o FROM AgencyPrizeDetail o ";
		String countSql = "SELECT count(*) FROM AgencyPrizeDetail o ";
		StringBuilder whereSql = new StringBuilder(" WHERE 1=1 ");
		List<PropertyFilter> pfList = null;
		if (conditionMap != null && conditionMap.size() > 0) {
			pfList = PropertyFilter.buildFromMap(conditionMap);
			String buildSql = PropertyFilter.transfer2Sql(pfList, "o");
			whereSql.append(buildSql);
		}
		List<Sort> sortList = page.fetchSort();
		StringBuilder orderSql = new StringBuilder(" ORDER BY ");
		if (page.isOrderBySetted()) {
			for (Sort sort : sortList) {
				orderSql.append(" " + sort.getProperty() + " " + sort.getDir() + ",");
			}
			orderSql.delete(orderSql.length() - 1, orderSql.length());
		} else {
			orderSql.append(" o.createTime desc ");
		}
		String tsql = sql + whereSql.toString() + orderSql.toString();
		String tCountSql = countSql + whereSql.toString();
		TypedQuery<AgencyPrizeDetail> q = em.createQuery(tsql, AgencyPrizeDetail.class);
		TypedQuery<Long> total = em.createQuery(tCountSql, Long.class);
		if (conditionMap != null && conditionMap.size() > 0) {
			PropertyFilter.setMatchValue2Query(q, pfList);
			PropertyFilter.setMatchValue2Query(total, pfList);
		}
		q.setFirstResult(page.getPageIndex()).setMaxResults(page.getMaxResult());
		List<AgencyPrizeDetail> resultList = q.getResultList();
		int count = total.getSingleResult().intValue();
		page.setList(resultList);
		page.setTotalResult(count);
	}

	public static BigDecimal findAgencyPrizeDetailSum(Map<String, Object> conditionMap) {
		EntityManager em = AgencyPrizeDetail.entityManager();
		String sumSql = "SELECT sum(o.prizeAmt) FROM AgencyPrizeDetail o ";
		StringBuilder whereSql = new StringBuilder(" WHERE 1=1 ");
		List<PropertyFilter> pfList = null;
		if (conditionMap != null && conditionMap.size() > 0) {
			pfList = PropertyFilter.buildFromMap(conditionMap);
			String buildSql = PropertyFilter.transfer2Sql(pfList, "o");
			whereSql.append(buildSql);
		}
		String tSumSql = sumSql + whereSql.toString();
		Query sumq = em.createQuery(tSumSql);
		if (conditionMap != null && conditionMap.size() > 0) {
			PropertyFilter.setMatchValue2Query(sumq, pfList);
		}
		Object sumprizeobj = sumq.getSingleResult();
		BigDecimal sumprize = (BigDecimal) sumprizeobj;
		return sumprize;
	}
}
