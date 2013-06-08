package com.ruyicai.agencycenter.domain;

import javax.persistence.Column;

import org.springframework.roo.addon.entity.RooIdentifier;
import org.springframework.roo.addon.tostring.RooToString;

@RooIdentifier
@RooToString
public class AgencyPercentPK {

	private static final long serialVersionUID = 1L;

	@Column(name = "USERNO", length = 32)
	private String userno;

	@Column(name = "LOTNO", length = 50)
	private String lotno;

	/** 0:普通代购，1:合买发起人,2:合买参与人 */
	@Column(name = "TYPE")
	private Integer type;

}
