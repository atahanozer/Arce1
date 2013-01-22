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
@Table(name = "TAG_HANDICAP_TYPE")
public class TagHandicapType implements Serializable {

	private static final long serialVersionUID = -5527566248002296042L;
	
	@Id
	@Column(name = "ID")
	@GeneratedValue
	private Integer id;
	//String dominos = "ş";
	
	@Column(name = "TAG_ID")
	private Integer tagId;
	
	@Column(name = "HANDICAP_TYPE_ID")
	private Integer handicapTypeId;
	

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

	public Integer getHandicapTypeId() {
		return handicapTypeId;
	}

	public void setHandicapTypeId(Integer handicapTypeId) {
		this.handicapTypeId = handicapTypeId;
	}

	
}
