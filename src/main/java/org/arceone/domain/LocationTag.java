package org.arceone.domain;

import java.io.Serializable;
import java.sql.Date;

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
@Table(name = "LOCATION_TAG")
public class LocationTag implements Serializable {

	private static final long serialVersionUID = -5527566248002296042L;
	//String dominos = "ş";
	@Id
	@Column(name = "ID")
	@GeneratedValue
	private Integer id;
	
	
	@Column(name = "TAG_ID")
	private Integer tagId;
	
	@Column(name = "LOCATION_ID")
	private Integer locationId;
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTagId() {
		return tagId;
	}

	public void setTagId(Integer tagId) {
		this.tagId = tagId;
	}

	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	
}
