package org.arceone.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.arceone.service.ViolationService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * For a complete reference see 
 * <a href="http://docs.jboss.org/hibernate/stable/annotations/reference/en/html_single/">
 * Hibernate Annotations Communit Documentations</a>
 */
//@Entity
//@Table(name = "VIOLATION")
public class ViolationEx extends Violation implements Serializable {

	private static final long serialVersionUID = -5527566248002296042L;
	
	public ViolationEx() {
		super();
	}
	
	public ViolationEx(Violation violation) {
		super();
		this.setDescription(violation.getDescription());
		this.setId(violation.getId());
		this.setLocationId(violation.getLocationId());
		this.setResolverId(violation.getViolationLevel());
		this.setStatus(violation.getStatus());
		this.setTitle(violation.getTitle());
		this.setViolationLevel(violation.getViolationLevel());
		
	}
	//String dominos = "ş";
	List<ProofEx> proofs;
	
	Location location;
	
	List<ViolationReportEx> reports;

	public List<ProofEx> getProofs() {
		return proofs;
	}

	public void setProofs(List<ProofEx> proofs) {
		this.proofs = proofs;
	}


	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public List<ViolationReportEx> getReports() {
		return reports;
	}

	public void setReports(List<ViolationReportEx> reports) {
		this.reports = reports;
	}
}
