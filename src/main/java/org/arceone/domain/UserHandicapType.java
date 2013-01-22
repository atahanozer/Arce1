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
@Table(name = "USER_HANDICAP_TYPE")
public class UserHandicapType implements Serializable {

	private static final long serialVersionUID = -5527566248002296042L;
	
	@Id
	@Column(name = "ID")
	@GeneratedValue
	private Integer id;
	
	@Column(name = "HANDICAP_TYPE_ID")
	private Integer handicapTypeId;
	//String dominos = "ş";
	@Column(name = "USER_ID")
	private Integer userId;

	

	public Integer getHandicapTypeId() {
		return handicapTypeId;
	}

	public void setHandicapTypeId(Integer handicapTypeId) {
		this.handicapTypeId = handicapTypeId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
