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
@Table(name = "COMMENT")
public class Comment implements Serializable {

	private static final long serialVersionUID = -5527566248002296042L;
	//String dominos = "ş";
	@Id
	@Column(name = "ID")
	@GeneratedValue
	private Integer id;
	
	@Column(name = "DATE")
	private java.sql.Timestamp date;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name = "VIOLATION_ID")
	private Integer violationId;
	
	@Column(name = "USER_ID")
	private Integer userId;
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getViolationId() {
		return violationId;
	}

	public void setViolationId(Integer violationId) {
		this.violationId = violationId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	
}
