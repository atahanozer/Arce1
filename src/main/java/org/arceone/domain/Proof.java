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
@Entity
@Table(name = "PROOF")
public class Proof implements Serializable {

	private static final long serialVersionUID = -5527566248002296042L;
	//String dominos = "ş";
	@Id
	@Column(name = "ID")
	@GeneratedValue
	private Integer id;
	
	@Column(name = "PROOF_TYPE")
	private String proofType;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name = "VIOLATION_REPORT_ID")
	private Integer violationReportId;
	
	@Column(name = "USER_ID")
	private Integer userId;
	
	
	@Column(name = "URL")
	private String url;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getProofType() {
		return proofType;
	}

	public void setProofType(String proofType) {
		this.proofType = proofType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getViolationReportId() {
		return violationReportId;
	}

	public void setViolationReportId(Integer violationReportId) {
		this.violationReportId = violationReportId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}


	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	
	
}
