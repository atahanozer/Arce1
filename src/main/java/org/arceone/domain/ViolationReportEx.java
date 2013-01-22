package org.arceone.domain;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


public class ViolationReportEx extends ViolationReport implements Serializable {

	private static final long serialVersionUID = -5527566248002296042L;
	
	
	public ViolationReportEx() {
		super();
	}
	
	public ViolationReportEx(ViolationReport violation) {
		super();
		this.setAbuseReport(violation.isAbuseReport());
		this.setDate(violation.getDate());
		this.setDescription(violation.getDescription());
		this.setFirstReport(violation.isFirstReport());
		this.setFollower(violation.isFollower());
		this.setId(violation.getId());
		this.setReporterId(violation.getReporterId());
		this.setResolveReport(violation.isResolveReport());
		this.setViolationId(violation.getViolationId());
		
	}
	//String dominos = "ş";
	private User user;


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}
	
}
