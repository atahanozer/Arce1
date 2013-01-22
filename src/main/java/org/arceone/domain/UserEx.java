package org.arceone.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * For a complete reference see 
 * <a href="http://docs.jboss.org/hibernate/stable/annotations/reference/en/html_single/">
 * Hibernate Annotations Communit Documentations</a>
 */

public class UserEx extends User implements Serializable {

	private static final long serialVersionUID = -5527566248002296042L;
	
	public UserEx() {
		super();
	}
	
	public UserEx(User user) {
		super();
		this.setConfirmPassword(user.getConfirmPassword());
		this.setEmail(user.getEmail());
		this.setIsBlocked(user.getIsBlocked());
		this.setId(user.getId());
		this.setPassword(user.getPassword());
		this.setUserName(user.getUserName());
	}

	private String handicapTypes;
	
	private int result;
	

	//String dominos = "ş";
	List<ViolationEx> followedViolations;
	List<ViolationEx> reportedViolations;
	List<ViolationEx> resolvedViolations;

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	boolean isAdmin;
	
	public List<ViolationEx> getReportedViolations() {
		return reportedViolations;
	}

	public void setReportedViolations(List<ViolationEx> reportedViolations) {
		this.reportedViolations = reportedViolations;
	}

	public List<ViolationEx> getResolvedViolations() {
		return resolvedViolations;
	}

	public void setResolvedViolations(List<ViolationEx> resolvedViolations) {
		this.resolvedViolations = resolvedViolations;
	}

	public List<ViolationEx> getFollowedViolations() {
		return followedViolations;
	}

	public void setFollowedViolations(List<ViolationEx> list) {
		this.followedViolations = list;
	}

	public String getHandicapTypes() {
		return handicapTypes;
	}

	public void setHandicapTypes(String handicapTypes) {
		this.handicapTypes = handicapTypes;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	
}
