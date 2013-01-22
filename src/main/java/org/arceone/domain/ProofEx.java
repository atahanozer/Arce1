package org.arceone.domain;

import java.io.Serializable;

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

public class ProofEx extends Proof implements Serializable {

	private static final long serialVersionUID = -5527566248002296042L;
	
	private java.sql.Timestamp date;
	
	private String userName;
	//String dominos = "ş";
	private String reportType;

	public java.sql.Timestamp getDate() {
		return date;
	}

	public void setDate(java.sql.Timestamp date) {
		this.date = date;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String string) {
		this.userName = string;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	
	
}
