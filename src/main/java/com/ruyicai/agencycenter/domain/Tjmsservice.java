package com.ruyicai.agencycenter.domain;

import java.util.Date;

import javax.persistence.Column;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

@RooJavaBean
@RooToString
@RooEntity(identifierType = TjmsservicePK.class, versionField = "", table = "TJMSSERVICE")
public class Tjmsservice {

	@Column(name = "memo", length = 100)
	private String memo;
	@Column(name = "processtime")
	private Date processtime;

	private static Tjmsservice createTjmsservice(String value, Integer type, Date processtime) {
		TjmsservicePK id = new TjmsservicePK(value, type);
		Tjmsservice tjmsservice = new Tjmsservice();
		tjmsservice.setId(id);
		tjmsservice.setProcesstime(processtime);
		tjmsservice.persist();
		return tjmsservice;
	}

	@Transactional
	public static boolean createTjmsservice(String value, Integer type) {
		TjmsservicePK id = new TjmsservicePK(value, type);
		Tjmsservice tjmsservice = Tjmsservice.findTjmsservice(id);
		if (tjmsservice == null) {
			Tjmsservice.createTjmsservice(value, type, new Date());
			return true;
		} else {
			return false;
		}
	}
}
