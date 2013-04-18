package com.ruyicai.agencycenter.controller.dto;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;

import com.ruyicai.agencycenter.domain.AgencyPrizeDetail;
import com.ruyicai.lottery.domain.Tuserinfo;

@RooJson
@RooJavaBean
public class AgencyPrizeDetailDTO {
	private AgencyPrizeDetail agencyPrizeDetail;
	private Tuserinfo tuserinfo;
	private Tuserinfo childTuserinfo;
}
