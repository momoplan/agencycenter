package com.ruyicai.agencycenter.domain;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.TypedQuery;

import org.apache.commons.lang.StringUtils;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import com.ruyicai.agencycenter.util.Page;
import com.ruyicai.agencycenter.util.Page.Sort;
import com.ruyicai.agencycenter.util.PropertyFilter;

/**
 * 用户代理
 */
@RooJavaBean
@RooToString
@RooEntity(versionField = "", table = "USERAGENCY")
public class UserAgency {

	@Id
	@Column(name = "USERNO", length = 32)
	private String userno;

	@Column(name = "PARENTUSERNO", length = 32)
	private String parentUserno;

	@Column(name = "CREATETIME")
	private Date createTime;

	@Column(name = "LEVEL")
	private Integer level;

	@Column(name = "STATE")
	private Integer state = 1;

	/**
	 * 创建用户代理
	 * 
	 * @param userno
	 *            用户编号
	 * @param parentUserno
	 *            上级用户编号
	 * @return UserAgency
	 */
	public static UserAgency createUserAgency(String userno, UserAgency parentUserAgency) {
		if (StringUtils.isBlank(userno)) {
			throw new IllegalArgumentException("the arguments userno is require");
		}
		String parentUserno = parentUserAgency == null ? null : parentUserAgency.getUserno();
		Integer level = parentUserAgency == null ? 1 : (parentUserAgency.getLevel() + 1);
		UserAgency userAgency = new UserAgency();
		userAgency.setUserno(userno);
		userAgency.setParentUserno(parentUserno);
		userAgency.setCreateTime(new Date());
		userAgency.setState(1);
		userAgency.setLevel(level);
		userAgency.persist();
		return userAgency;
	}

	public static void findUserAgency(Map<String, Object> conditionMap, Page<UserAgency> page) {
		EntityManager em = UserAgency.entityManager();
		String sql = "SELECT o FROM UserAgency o ";
		String countSql = "SELECT count(*) FROM UserAgency o ";
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
		TypedQuery<UserAgency> q = em.createQuery(tsql, UserAgency.class);
		TypedQuery<Long> total = em.createQuery(tCountSql, Long.class);
		if (conditionMap != null && conditionMap.size() > 0) {
			PropertyFilter.setMatchValue2Query(q, pfList);
			PropertyFilter.setMatchValue2Query(total, pfList);
		}
		q.setFirstResult(page.getPageIndex()).setMaxResults(page.getMaxResult());
		List<UserAgency> resultList = q.getResultList();
		int count = total.getSingleResult().intValue();
		page.setList(resultList);
		page.setTotalResult(count);
	}

	public static Integer findUnderlingCount(String userno) {
		EntityManager em = UserAgency.entityManager();
		String countSql = "SELECT count(*) FROM UserAgency o WHERE o.parentUserno = :userno";
		TypedQuery<Long> total = em.createQuery(countSql, Long.class);
		total.setParameter("userno", userno);
		return total.getSingleResult().intValue();
	}
	
	/**
	 * 查询代理用户是否存在
	 * @param parentUserno
	 * @return
	 */
	public static Integer findUserAgencyCount(String userno) {
		EntityManager em = UserAgency.entityManager();
		String countSql = "SELECT count(*) FROM UserAgency o WHERE o.userno = :userno";
		TypedQuery<Long> total = em.createQuery(countSql, Long.class);
		total.setParameter("userno", userno);
		return total.getSingleResult().intValue();
	}
}
