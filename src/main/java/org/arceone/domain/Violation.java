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
@Table(name = "VIOLATION")
public class Violation implements Serializable {

	private static final long serialVersionUID = -5527566248002296042L;
	
	@Id
	@Column(name = "ID")
	@GeneratedValue
	private Integer id;
	
	@Column(name = "TITLE")
	private String title;
	//String dominos = "ş";
	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name = "LOCATION_ID")
	private Integer locationId;
	
	@Column(name = "RESOLVER_ID")
	private Integer resolverId;
	
	@Column(name = "VIOLATION_LEVEL")
	private int violationLevel;
	
	@Column(name = "STATUS")
	private String status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public Integer getResolverId() {
		return resolverId;
	}

	public void setResolverId(Integer resolverId) {
		this.resolverId = resolverId;
	}

	public int getViolationLevel() {
		return violationLevel;
	}

	public void setViolationLevel(int violationLevel) {
		this.violationLevel = violationLevel;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
	
}
