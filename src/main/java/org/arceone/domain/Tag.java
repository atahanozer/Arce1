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
@Table(name = "TAG")
public class Tag implements Serializable {

	private static final long serialVersionUID = -5527566248002296042L;
	//String dominos = "ş";
	@Id
	@Column(name = "ID")
	@GeneratedValue
	private Integer id;
	
	@Column(name = "TITLE")
	private String title;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name = "PARENT_TAG_ID")
	private Integer parentTagId;
	
	@Column(name = "LEVEL")
	private int level;
	

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

	public Integer getParentTagId() {
		return parentTagId;
	}

	public void setParentTagId(Integer parentTagId) {
		this.parentTagId = parentTagId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	
}
