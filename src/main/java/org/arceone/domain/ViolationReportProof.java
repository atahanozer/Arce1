package org.arceone.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * For a complete reference see 
 * <a href="http://docs.jboss.org/hibernate/stable/annotations/reference/en/html_single/">
 * Hibernate Annotations Communit Documentations</a>
 */
//@Entity
//@Table(name = "VIOLATION")
public class ViolationReportProof implements Serializable {

	private static final long serialVersionUID = -5527566248002296042L;
	
//	@Id
	//@Column(name = "ID")
	//@GeneratedValue
	private Integer id;
	
	//@Column(name = "TITLE")
	private String title;
	
	//@Column(name = "DESCRIPTION")
	private String description;
	
	private String vrDescription;
	
	private String commentDescription;
	
	private String tagTitle;
	
	private Integer tagId;
	
	private Integer tagId2;
	
	private String tagIdString;
	
	private String tagId3String;
	
	public String getTagId3String() {
		return tagId3String;
	}

	public void setTagId3String(String tagId3String) {
		this.tagId3String = tagId3String;
	}

	public String getTagIdString() {
		return tagIdString;
	}

	public void setTagIdString(String tagIdString) {
		this.tagIdString = tagIdString;
	}

	public String getTagId2String() {
		return tagId2String;
	}

	public void setTagId2String(String tagId2String) {
		this.tagId2String = tagId2String;
	}

	private String tagId2String;
	
	private String locationTag;
	
	private String proofUrl;
	
	private Integer lastAddedViolationId;
	
	private Integer isResolved;
	
	private Integer isFollowing;
	
//	@Column(name = "LOCATION_ID")
	private Integer locationId;
	
	//@Column(name = "RESOLVER_ID")
	private Integer resolverId;
	
	//@Column(name = "VIOLATION_LEVEL")
	private int violationLevel;
	
	//@Column(name = "STATUS")
	private String status;
	
	private String handicapType;
	
	private String proofType;
	
	//@Column(name = "DESCRIPTION")
	//private String description;
	
	//@Column(name = "VIOLATION_REPORT_ID")
	private Integer violationReportId;
	
	//@Column(name = "USER_ID")
	private Integer userId;
	
	private String userName;
	
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	//@Column(name = "URL")
	private String url;
	
	//@Column(name = "LONGTITUDE")
	private String longtitude;
	
	//@Column(name = "LATTITUDE")
	private String lattitude;

	   private String filename;
       private CommonsMultipartFile fileData;

       public String getFilename() {
               return filename;
       }

       public void setFilename(String filename) {
               this.filename = filename;
       }

       public CommonsMultipartFile getFileData() {
               return fileData;
       }

       public void setFileData(CommonsMultipartFile fileData) {
               this.fileData = fileData;
       }
     //String dominos = "ş";
	public String getLocationTag() {
		return locationTag;
	}

	public void setLocationTag(String locationTag) {
		this.locationTag = locationTag;
	}

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



	public String getProofType() {
		return proofType;
	}

	public void setProofType(String proofType) {
		this.proofType = proofType;
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
	
	public String getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(String longtitude) {
		this.longtitude = longtitude;
	}

	public String getLattitude() {
		return lattitude;
	}

	public void setLattitude(String lattitude) {
		this.lattitude = lattitude;
	}

	public String getVrDescription() {
		return vrDescription;
	}

	public void setVrDescription(String vrDescription) {
		this.vrDescription = vrDescription;
	}

	public String getCommentDescription() {
		return commentDescription;
	}

	public void setCommentDescription(String commentDescription) {
		this.commentDescription = commentDescription;
	}

	public String getHandicapType() {
		return handicapType;
	}

	public void setHandicapType(String handicapType) {
		this.handicapType = handicapType;
	}

	public String getTagTitle() {
		return tagTitle;
	}

	public void setTagTitle(String tagTitle) {
		this.tagTitle = tagTitle;
	}

	public Integer getLastAddedViolationId() {
		return lastAddedViolationId;
	}

	public void setLastAddedViolationId(Integer lastAddedViolationId) {
		this.lastAddedViolationId = lastAddedViolationId;
	}

	public Integer getIsFollowing() {
		return isFollowing;
	}

	public void setIsFollowing(Integer isFollowable) {
		this.isFollowing = isFollowable;
	}

	public Integer getIsResolved() {
		return isResolved;
	}

	public void setIsResolved(Integer isResolved) {
		this.isResolved = isResolved;
	}

	public Integer getTagId() {
		return tagId;
	}

	public void setTagId(Integer tagId) {
		this.tagId = tagId;
	}

	public Integer getTagId2() {
		return tagId2;
	}

	public void setTagId2(Integer tagId2) {
		this.tagId2 = tagId2;
	}

	public String getProofUrl() {
		return proofUrl;
	}

	public void setProofUrl(String proofUrl) {
		this.proofUrl = proofUrl;
	}
}
