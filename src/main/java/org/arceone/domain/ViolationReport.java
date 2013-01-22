package org.arceone.domain;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

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
@Entity
@Table(name = "VIOLATION_REPORT")
public class ViolationReport implements Serializable {

	private static final long serialVersionUID = -5527566248002296042L;
	//String dominos = "ş";
	@Id
	@Column(name = "ID")
	@GeneratedValue
	private Integer id;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name = "DATE")
	private java.sql.Timestamp date;
	
	@Column(name = "REPORTER_ID")
	private Integer reporterId;
	
	@Column(name = "VIOLATION_ID")
	private Integer violationId;
	
	@Column(name = "IS_FIRST_REPORT")
	private boolean isFirstReport;
	
	@Column(name = "IS_ABUSE_REPORT")
	private boolean isAbuseReport;
	
	@Column(name = "IS_RESOLVE_REPORT")
	private boolean isResolveReport;
	
	@Column(name = "IS_FOLLOWER")
	private boolean isFollower;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public Integer getReporterId() {
		return reporterId;
	}

	public void setReporterId(Integer reporterId) {
		this.reporterId = reporterId;
	}

	public Integer getViolationId() {
		return violationId;
	}

	public void setViolationId(Integer violationId) {
		this.violationId = violationId;
	}

	public boolean isFirstReport() {
		return isFirstReport;
	}

	public void setFirstReport(boolean isFirstReport) {
		this.isFirstReport = isFirstReport;
	}

	public boolean isAbuseReport() {
		return isAbuseReport;
	}

	public void setAbuseReport(boolean isAbuseReport) {
		this.isAbuseReport = isAbuseReport;
	}

	public boolean isFollower() {
		return isFollower;
	}

	public void setFollower(boolean isFollower) {
		this.isFollower = isFollower;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public boolean isResolveReport() {
		return isResolveReport;
	}

	public void setResolveReport(boolean isResolveReport) {
		this.isResolveReport = isResolveReport;
	}

	
}
