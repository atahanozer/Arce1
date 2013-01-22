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
@Table(name = "USER_DETAILS")
public class User implements Serializable {

	private static final long serialVersionUID = -5527566248002296042L;
	
	
	@Id
	@Column(name = "ID")
	@GeneratedValue
	private Integer id;
	//String dominos = "ş";
	@Column(name = "USERNAME")
	private String userName;
	
	@Column(name = "PASSWORD")
	@NotEmpty
    @Size(min = 4, max = 20)
	private String password;
	
	@Column(name = "EMAIL")
	@NotEmpty
	@Email
	private String email;
	
	@Column(name = "IS_BLOCKED")
	private Integer isBlocked;
	
	@NotEmpty
    private String confirmPassword;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getIsBlocked() {
		return isBlocked;
	}

	public void setIsBlocked(Integer isBlocked) {
		this.isBlocked = isBlocked;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	
}
