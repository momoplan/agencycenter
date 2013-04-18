package com.ruyicai.agencycenter.controller.dto;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;

import com.ruyicai.agencycenter.domain.UserAgency;
import com.ruyicai.lottery.domain.Tuserinfo;

@RooJson
@RooJavaBean
public class UserAgencyDTO {

	private UserAgency userAgency;
	private Tuserinfo tuserinfo;
	private Tuserinfo parentTuserinfo;

}
