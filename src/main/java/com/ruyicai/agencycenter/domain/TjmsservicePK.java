package com.ruyicai.agencycenter.domain;

import javax.persistence.Column;

import org.springframework.roo.addon.entity.RooIdentifier;
import org.springframework.roo.addon.tostring.RooToString;

@RooIdentifier
@RooToString
public class TjmsservicePK {

	private static final long serialVersionUID = 1L;

	@Column(name = "value")
	private String value;
	@Column(name = "type")
	private Integer type;
}
