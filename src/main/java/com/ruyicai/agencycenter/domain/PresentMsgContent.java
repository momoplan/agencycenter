package com.ruyicai.agencycenter.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooJson
@RooToString
@RooEntity(versionField = "", table = "PRESENTMSGCONTENT")
public class PresentMsgContent {

	/** ID */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;
	
	/** 购买用户编号 */
	@Column(name = "BUYUSERNO")
	private String buyUserno;

	/** 接收人手机号 */
	@Column(name = "RECIVERMOBILE")
	private String reciverMobile;

	/** 接收人用户编号 */
	@Column(name = "RECIVERUSERNO")
	private String reciverUserno;

	/** 彩种 */
	@Column(name = "LOTNO")
	private String lotno;
	
	/** 彩票内容*/
	@Column(name = "CONTENT")
	private String content;
	
	/** 彩票金额*/
	@Column(name = "AMT")
	private BigDecimal amt;
	
	/** 每个彩票金额*/
	@Column(name = "EVERYAMT")
	private BigDecimal everyAmt;

	/** 创建时间 */
	@Column(name = "CREATETIME")
	private Date createTime;
	
	/** 倍数*/
	@Column(name = "LOTMULTI")
	private BigDecimal lotmulti;
	
	/** 赠送寄语 */
	@Column(name = "BLESSING")
	private String Blessing;
	
	/** 状态 0创建 1成功 2失败 */
	@Column(name = "STATE")
	private Integer state;
	
	/**
	 * 赠送类型
	 */
	@Column(name = "BETTYPE")
	private BigDecimal betype;
	
	/**
	 * 创建用户代理赠送彩票的内容，一注 2元
	 * @param orderRequest
	 * @return
	 */
	public static void createPresentMsgContent(OrderRequest orderRequest) {
		List<BetRequest> betRequests = orderRequest.betRequests;
		for(BetRequest bet : betRequests){
			PresentMsgContent content = new PresentMsgContent();
			content.setBuyUserno(orderRequest.getBuyuserno());
			content.setReciverUserno(orderRequest.getUserno());
			content.setLotno(orderRequest.getLotno());
			content.setContent(orderRequest.getBetRequests().get(0).getBetcode());
			content.setAmt(orderRequest.getAmt());
			content.setEveryAmt(bet.getAmt());
			content.setReciverMobile(orderRequest.getReciverMobile());
			content.setBetype(orderRequest.getBettype());
			content.setLotmulti(orderRequest.getLotmulti());
			content.setBlessing(orderRequest.getBlessing());
			content.setCreateTime(new Date());
			content.setState(0);
			content.persist();
		}
	}
	
	public static List<PresentMsgContent> isValid(String userno){
		EntityManager em = UserAgency.entityManager();
		String sql = "select o from PresentMsgContent o where o.reciverUserno = ? and o.state=0 and datediff(NOW(),o.createTime)<=7 order by o.createTime asc ";
		TypedQuery<PresentMsgContent> q = em.createQuery(sql, PresentMsgContent.class);
		q.setParameter(1, userno);
		List<PresentMsgContent> resultList = q.getResultList();
		return resultList;
	}
	
	public static Integer updateState(Integer id) {
		Query query = entityManager().createNativeQuery("UPDATE PresentMsgContent o set o.state = 1 where o.id = ?");
		query.setParameter(1, id);
		return query.executeUpdate();
	}
	
}
