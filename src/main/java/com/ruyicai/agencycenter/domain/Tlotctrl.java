package com.ruyicai.agencycenter.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.EmbeddedId;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;

/**
 * @Description: 期信息
 * 
 * @author chuang   
 * @date 2014年5月23日下午4:09:34
 * @version V1.0   
 *
 */
@RooJavaBean
@RooJson
public class Tlotctrl {
	
	@EmbeddedId
	private TlotctrlPK id;

	private Date starttime;

	private Date endtime;

	private BigDecimal state;

	private Date createtime;
	
	private Date endbettime;

	private Date hemaiendtime;
	
	private BigDecimal encashstate;
	
}




