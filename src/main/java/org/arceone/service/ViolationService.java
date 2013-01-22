package org.arceone.service;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.arceone.domain.Location;
import org.arceone.domain.LocationTag;
import org.arceone.domain.Proof;
import org.arceone.domain.ProofEx;
import org.arceone.domain.Tag;
import org.arceone.domain.TagHandicapType;
import org.arceone.domain.TagViolation;
import org.arceone.domain.User;
import org.arceone.domain.Violation;
import org.arceone.domain.ViolationGeneral;
import org.arceone.domain.ViolationReport;
import org.arceone.domain.ViolationReportProof;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for processing Users
 * 
 */
@Service("violationService")
@Transactional
public class ViolationService {

	protected static Logger logger = Logger.getLogger("service");
	
	//@Autowired(required=true)
	@Resource(name="sessionFactory")
	private SessionFactory sessionFactory;
	
	@Autowired
	private TagService tagService;
	
	/**
	 * Retrieves all Users
	 * 
	 * @return a list of Users
	 */
	public List<Violation> getAll() {
		String dominos = "ş";
		logger.debug("Retrieving all Users");
		//String dominos = "ş";
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		// Create a Hibernate query (HQL)
		Query query = session.createQuery("FROM  Violation order by id desc limit 100");
		
		// Retrieve all
		return  query.list();
	}
	
	
	public List<Violation> getNews() {
		logger.debug("Retrieving all Users");
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		// Create a Hibernate query (HQL)
		Query query = session.createQuery("FROM  Violation order by id desc LIMIT 10");
		
		// Retrieve all
		return  query.list();
	}
	
	public List<Violation> getAllOrderedByDate() {
		logger.debug("Retrieving all Users");
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		// Create a Hibernate query (HQL)
		Query query = session.createQuery("FROM  Violation where id in (select distinct(vr.violationId) from ViolationReport vr order by date desc) ");
		
		
		// Retrieve all
		
		return  query.list();
	}
	
	/**
	 * Retrieves a single Violation
	 */
	public Violation get( Integer id ) {
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		// Retrieve existing Violation first
		Violation Violation = (Violation) session.get(Violation.class, id);
		
		return Violation;
	}
	
	/**
	 * Retrieves a single Violation
	 */
	public Integer getLastAdded() {
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		Query query = session.createQuery("from Violation order by id desc");
		//query.setParameter("userName", userName);
		List list = query.list();
		Violation violation = (Violation)list.get(0);
		
		// Retrieve existing Violation first
		//Violation violation = (Violation) session.get(Violation.class, id);
		
		return violation.getId();
	}
	
	/**
	 * Adds a new Violation
	 */
	public void add(ViolationReportProof vrp) {
		logger.debug("Adding new Violation");
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		Location location = new Location();
		
		location.setLattitude(vrp.getLattitude());
		location.setLongtitude(vrp.getLongtitude());
		location.setDescription(vrp.getLocationTag());
		// Save Proof
		session.save(location);
		
		Violation violation = new Violation();
		
		violation.setDescription(vrp.getDescription());
		violation.setLocationId(location.getId());
		violation.setResolverId(vrp.getResolverId());
		violation.setTitle(vrp.getTitle());
		violation.setViolationLevel(0);
		violation.setStatus(ViolationGeneral.VIOLATION_STATUS_NEW);
		
		// Save
		session.save(violation);
		
		int handicapCount = 0;
		
		List<TagHandicapType> tagHandicapTypes = tagService.getTagsByHandicapType(vrp.getHandicapType());
		Iterator<TagHandicapType> iter = tagHandicapTypes.iterator();
		while(iter.hasNext()){
			TagHandicapType tht = (TagHandicapType) iter.next();
			
			TagViolation tagViolation = new TagViolation();
			tagViolation.setTagId(tht.getTagId());
			tagViolation.setViolationId(violation.getId());
			session.save(tagViolation);
		}
		
		if(vrp.getLocationTag() != null && !"".equalsIgnoreCase(vrp.getLocationTag())){
			
			List<String> items = Arrays.asList(vrp.getLocationTag().split("\\s*,\\s*"));
			
			Iterator iterItems = items.iterator();
			
			while(iterItems.hasNext()){
				String title = (String)iterItems.next();
				Tag tag = tagService.getByTagTitle(title);
				if(tag == null){
					tag = new Tag();
					tag.setTitle(title);
					tag.setLevel(5);
					session.save(tag);
					
				}
				
				LocationTag loactionTag = new LocationTag();
				loactionTag.setTagId(tag.getId());
				loactionTag.setLocationId(location.getId());
				session.save(loactionTag);
				
				TagViolation tagViolation = new TagViolation();
				tagViolation.setTagId(tag.getId());
				tagViolation.setViolationId(violation.getId());
				session.save(tagViolation);
			}
		}
		
		if(vrp.getTagId() != null && vrp.getTagId() > 0){
			
			
			Tag tag = tagService.get(vrp.getTagId());
			String handicapTypes = tag.getDescription();
			List<Tag> tags = tagService.getTagsListByHandicapType(tagService.getTagsByHandicapType(handicapTypes));
		    handicapCount = tags.size();
			
			
			Iterator iterTag = tags.iterator();
			while(iterTag.hasNext()){
				
			Tag tempTag	=(Tag)iterTag.next();
			
			if(!tagService.hasTagViolation(tempTag.getId(), violation.getId())){
				TagViolation tv = new TagViolation();
				tv.setTagId(tempTag.getId());
				tv.setViolationId(violation.getId());
				session.save(tv);
			}
			
			}
			
			TagViolation tagViolation = new TagViolation();
			tagViolation.setTagId(vrp.getTagId());
			tagViolation.setViolationId(violation.getId());
			session.save(tagViolation);
		}
		
		if(vrp.getTagId2() != null && vrp.getTagId2() > 0){
			TagViolation tagViolation = new TagViolation();
			tagViolation.setTagId(vrp.getTagId2());
			tagViolation.setViolationId(violation.getId());
			session.save(tagViolation);
		}
		
		if(vrp.getTagId3String() != null && !"".equalsIgnoreCase(vrp.getTagId3String())	){
			
		/*	Tag tag = new Tag();
			tag.setTitle(vrp.getTagId3String());
			tag.setParentTagId(vrp.getTagId2());
			tag.setLevel(0);
			session.save(tag);
		*/	
			
			TagViolation tagViolation = new TagViolation();
			tagViolation.setTagId(Integer.parseInt(vrp.getTagId3String()));
			tagViolation.setViolationId(violation.getId());
			session.save(tagViolation);
			
			Tag tag = tagService.get(Integer.parseInt(vrp.getTagId3String()));
			if("Dusuk".equalsIgnoreCase(tag.getDescription())){
				violation.setViolationLevel(violation.getViolationLevel()+5);
			}else if("Orta".equalsIgnoreCase(tag.getDescription())){
				violation.setViolationLevel(violation.getViolationLevel()+10);
			}else{
				violation.setViolationLevel(violation.getViolationLevel()+20);
			}
		}else{
			violation.setViolationLevel(violation.getViolationLevel()+10);
		}
		
		violation.setViolationLevel(violation.getViolationLevel()+handicapCount*10);
		
		session.save(violation);
		
		Integer lastAdded = vrp.getLastAddedViolationId();
		if(vrp.getLastAddedViolationId() == null){
			lastAdded = getLastAdded();
		}
		
		
		List<TagViolation> tagViolations = tagService.getTagViolationByViolationId(lastAdded*2);
		Iterator<TagViolation> iter2 = tagViolations.iterator();
		while(iter2.hasNext()){
			TagViolation tv = (TagViolation) iter2.next();
			
			tv.setViolationId(violation.getId());
			session.save(tv);

		}
	}
	
	public void addTagsForReport(ViolationReportProof vrp,Violation violation) {
		logger.debug("Adding new Violation");
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		
		if(vrp.getLocationTag() != null && !"".equalsIgnoreCase(vrp.getLocationTag())){
			
			List<String> items = Arrays.asList(vrp.getLocationTag().split("\\s*,\\s*"));
			
			Iterator iterItems = items.iterator();
			
			while(iterItems.hasNext()){
				String title = (String)iterItems.next();
				Tag tag = tagService.getByTagTitle(title);
				if(tag == null){
					tag = new Tag();
					tag.setTitle(vrp.getLocationTag());
					tag.setLevel(5);
					session.save(tag);
					
				}
				
				TagViolation tagViolation = new TagViolation();
				tagViolation.setTagId(tag.getId());
				tagViolation.setViolationId(vrp.getId());
				session.save(tagViolation);
			}
		}
		
		if(vrp.getTagId() != null && vrp.getTagId() > 0){
			TagViolation tagViolation = new TagViolation();
			tagViolation.setTagId(vrp.getTagId());
			tagViolation.setViolationId(vrp.getId());
			session.save(tagViolation);
		}
		
		if(vrp.getTagId2() != null && vrp.getTagId2() > 0){
			TagViolation tagViolation = new TagViolation();
			tagViolation.setTagId(vrp.getTagId2());
			tagViolation.setViolationId(vrp.getId());
			session.save(tagViolation);
		}
		
		if(vrp.getTagId3String() != null && !"".equalsIgnoreCase(vrp.getTagId3String())	){
			
		/*	Tag tag = new Tag();
			tag.setTitle(vrp.getTagId3String());
			tag.setParentTagId(vrp.getTagId2());
			tag.setLevel(0);
			session.save(tag);
		*/	
			
			TagViolation tagViolation = new TagViolation();
			tagViolation.setTagId(Integer.parseInt(vrp.getTagId3String()));
			tagViolation.setViolationId(vrp.getId());
			session.save(tagViolation);
			
			Tag tag = tagService.get(Integer.parseInt(vrp.getTagId3String()));
			if("Dusuk".equalsIgnoreCase(tag.getDescription())){
				violation.setViolationLevel(violation.getViolationLevel()+5);
			}else if("Orta".equalsIgnoreCase(tag.getDescription())){
				violation.setViolationLevel(violation.getViolationLevel()+10);
			}else{
				violation.setViolationLevel(violation.getViolationLevel()+20);
			}
		}else{
			violation.setViolationLevel(violation.getViolationLevel()+10);
		}
		
		
		
	}
	
	/**
	 * Deletes an existing Violation
	 * @param id the id of the existing Violation
	 */
	public void delete(Integer id) {
		logger.debug("Deleting existing Violation");
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		// Retrieve existing Violation first
		Violation Violation = (Violation) session.get(Violation.class, id);
		
		// Delete 
		session.delete(Violation);
	}
	
	
	/**
	 * Edits an existing Violation
	 */
	public void edit(Violation violation) {
		logger.debug("Editing existing Violation");
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		// Retrieve existing Violation via id
		Violation existingViolation = (Violation) session.get(Violation.class, violation.getId());
		
		// Assign updated values to this Violation
		existingViolation.setLocationId(violation.getLocationId());
		existingViolation.setResolverId(violation.getResolverId());
		existingViolation.setStatus(violation.getStatus());
		existingViolation.setTitle(violation.getTitle());
		existingViolation.setViolationLevel(violation.getViolationLevel());

		// Save updates
		session.save(existingViolation);
	}
	
	public int getLastAddedViolationReport() {
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		Query query = session.createQuery("from ViolationReport order by id desc");
		//query.setParameter("userName", userName);
		List list = query.list();
		ViolationReport violation = (ViolationReport)list.get(0);
		
		// Retrieve existing Violation first
		//Violation violation = (Violation) session.get(Violation.class, id);
		
		return violation.getId();
	}
	
	public int getLastAddedLocation() {
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		Query query = session.createQuery("from Location order by id desc");
		//query.setParameter("userName", userName);
		List list = query.list();
		Location location = (Location)list.get(0);
		
		// Retrieve existing Violation first
		//Violation violation = (Violation) session.get(Violation.class, id);
		
		return location.getId();
	}
	
	public int getUserIdByUsername(String username) {
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		Query query = session.createQuery("from User where username = :username ");
		query.setParameter("username", username);
		List list = query.list();
		User user = (User)list.get(0);
		
		return user.getId();
	}
	
	/**
	 * Adds a new ViolationReport adn proof
	 */
	public void addViolationReportAndProof(ViolationReportProof vrp,int violationId,String url,String type) {
		logger.debug("Adding new Violation");
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		String userName = vrp.getUserName() ;
		if(userName == null || "".equalsIgnoreCase(userName)){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		userName = auth.getName(); //get logged in username
		}
       Integer reporterId = getUserIdByUsername(userName);
		
		ViolationReport violation = new ViolationReport();
		
		violation.setDescription(vrp.getDescription());
		violation.setDate(ViolationGeneral.getSqlDate());
		violation.setFirstReport(true);
		violation.setViolationId(violationId);
		violation.setReporterId(reporterId);
		
		if("".equalsIgnoreCase(type)){
			violation.setFirstReport(true);
		}else if ("RESOLVE".equalsIgnoreCase(type)){
			violation.setFirstReport(false);
			violation.setResolveReport(true);
		}else if("REOPEN".equalsIgnoreCase(type)){
			violation.setFirstReport(true);
			violation.setResolveReport(false);
			violation.setFollower(false);
		}else if("REPORT".equalsIgnoreCase(type)){
			violation.setFirstReport(false);
			violation.setResolveReport(false);
			violation.setFollower(false);
		}else if("ABUSE".equalsIgnoreCase(type)){
			violation.setFirstReport(false);
			violation.setResolveReport(false);
			violation.setFollower(false);
			violation.setAbuseReport(true);
		}
	
		// Save Violation Report
		session.save(violation);
		
		if(url != null && !"".equalsIgnoreCase(url)){
		
		Proof proof = new Proof();
		
		proof.setDescription(vrp.getDescription());
		proof.setProofType(vrp.getProofType());
		proof.setViolationReportId(violation.getId());
		proof.setUrl(url);
		
		// Save Proof
		session.save(proof);
		
		}

	}
	
	public ViolationReportProof getVRP( Integer id ) {
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		ViolationReportProof vrp = null;
		// Retrieve existing Violation first
		Violation violation = (Violation) session.get(Violation.class, id);
		
		Query query = session.createQuery("from ViolationReport where violationId = :violationId ");
		query.setParameter("violationId", id);
		List list = query.list();
		if(!list.isEmpty()){
		ViolationReport violationReport = (ViolationReport)list.get(0);
		
		Query query2 = session.createQuery("from Proof where violationReportId = :violationReportId ");
		query2.setParameter("violationReportId", violationReport.getId());
		List list2 = query2.list();
		Proof proof = (Proof)list2.get(0);
		
		
	 vrp = new ViolationReportProof();
		vrp.setDescription(violation.getDescription());
		vrp.setResolverId(violation.getResolverId());
		vrp.setLocationId(violation.getLocationId());
		vrp.setUrl(proof.getUrl());
		vrp.setTitle(violation.getTitle());
		vrp.setStatus(violation.getStatus());
		
		vrp.setId(id);
		
		Query locationQuery = session.createQuery("from Location where id = :locationId ");
		locationQuery.setParameter("locationId", violation.getLocationId());
		list = locationQuery.list();
		Location location = (Location)list.get(0);
			
		vrp.setLattitude(location.getLattitude());
		vrp.setLongtitude(location.getLongtitude());
		}
		return vrp;
	}
	
	public Integer isFollowing(Integer violationId,Integer userId) {
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		Query query = session.createQuery("from ViolationReport where reporterId = :reporterId and violationId = :violationId");
		query.setParameter("reporterId", userId);
		query.setParameter("violationId", violationId);
		List list = query.list();
		
		if(list.isEmpty())
			return 0;
		else
			return 1;
		//ViolationReport violation = (ViolationReport)list.get(0);
		
		// Retrieve existing Violation first
		//Violation violation = (Violation) session.get(Violation.class, id);
	}
	
	public void follow(Integer violationId,Integer userId) {
		logger.debug("Following violation");
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		// Retrieve existing Violation first
		ViolationReport violationReport = new ViolationReport();
		violationReport.setViolationId(violationId);
		violationReport.setReporterId(userId);
		violationReport.setFollower(true);
		violationReport.setDate(ViolationGeneral.getSqlDate());
		
		session.save(violationReport);
		
	}
	
	public void unFollow(Integer violationId,Integer userId) {
		logger.debug("Unfollowing violation");
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		Query query = session.createQuery("from ViolationReport where reporterId = :reporterId and violationId = :violationId and isFollower=1");
		query.setParameter("reporterId", userId);
		query.setParameter("violationId", violationId);
		List list = query.list();
		
		if(!list.isEmpty()){
			ViolationReport vr = (ViolationReport)list.get(0);
			session.delete(vr);
		}
		
		
	}
	
	public List<Violation> getReportedAllByUserId(Integer userId) {
		logger.debug("Retrieving all violations by userId");
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		// Create a Hibernate query (HQL)
		Query query = session.createQuery("FROM  Violation where id in (select distinct(vr.violationId) from ViolationReport vr where (vr.isFirstReport = 1 or (vr.isFollower = 0 and vr.isResolveReport = 0 and isAbuseReport = 0)) and vr.reporterId = :reporterId order by date desc) ");
		query.setParameter("reporterId", userId);
		
		// Retrieve all
		return  query.list();
	}
	
	
	public List<ProofEx> listProofsByViolationId(Integer violationId) {
		logger.debug("Retrieving all violations by userId");
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		// Create a Hibernate query (HQL)
		Query query = session.createQuery("FROM  Proof where violationReportId in (select distinct(vr.id) from ViolationReport vr where  vr.violationId = :violationId order by date desc) ");
		query.setParameter("violationId", violationId);
		
		List<ProofEx> proofExs = new ArrayList<ProofEx>(); 
		
		List<Proof> proofs = query.list();
		Iterator iter = proofs.iterator();
		while(iter.hasNext()){
			Proof proof = (Proof)iter.next();
			ProofEx pEx = new ProofEx();
			pEx.setUrl(proof.getUrl());
			
				query = session.createQuery("FROM  ViolationReport vr where  vr.id = :violationId ");
				query.setParameter("violationId", proof.getViolationReportId());
				List<ViolationReport> vrs = query.list();
				if(!vrs.isEmpty()){
						
						ViolationReport vr = vrs.get(0);
						pEx.setDate(vr.getDate());
						if(!"".equalsIgnoreCase(proof.getUrl()) && (vr.getReporterId() != null)){
							
							User user = (User) session.get(User.class, vr.getReporterId());
							pEx.setUserName(user.getUserName());
							
							if(vr.isFirstReport())
								pEx.setReportType("Ilk raporu bildiren");
							else if(vr.isResolveReport())
								pEx.setReportType("Cozumleyen");
							else if(!vr.isResolveReport() && !vr.isFirstReport() && !vr.isAbuseReport())
								pEx.setReportType("Tekrar rapor bildiren");
							
							
							pEx.setViolationReportId(proof.getViolationReportId());
							pEx.setProofType(proof.getProofType());
							
								proofExs.add(pEx);
					}
					}
				
		    
		}
		
		// Retrieve all
		return  proofExs;
	}
	
	public List<Violation> getFollowedAllByUserId(Integer userId) {
		logger.debug("Retrieving all followed violations by userId");
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		// Create a Hibernate query (HQL)
		Query query = session.createQuery("FROM  Violation where id in (select distinct(vr.violationId) from ViolationReport vr where vr.isFollower = 1 and vr.reporterId = :reporterId order by date desc) ");
		query.setParameter("reporterId", userId);
		
		// Retrieve all
		return  query.list();
	}
	
	public List<Violation> getResolvedAllByUserId(Integer userId) {
		logger.debug("Retrieving all followed violations by userId");
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		// Create a Hibernate query (HQL)
		Query query = session.createQuery("FROM  Violation where id in (select distinct(vr.violationId) from ViolationReport vr where vr.isResolveReport = 1 and vr.reporterId = :reporterId order by date desc) ");
		query.setParameter("reporterId", userId);
		
		// Retrieve all
		return  query.list();
	}
	
	public List<ViolationReportProof> getVRPsFromViolations(List<Violation> violations) {
		logger.debug("Retrieving all followed violations by userId");
		
		List<ViolationReportProof> vrps = new ArrayList<ViolationReportProof>();
		Iterator iter = violations.iterator();
		
		while(iter.hasNext()){
			Violation viol = (Violation)iter.next();
			vrps.add(getVRP(viol.getId()));
		}
		
		// Retrieve all
		return  vrps;
	}
	
	public Location getLocationById(Integer locationId) {
		logger.debug("Retrieving all violations by userId");
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		Location location = (Location) session.get(Location.class, locationId);
		
		// Retrieve all
		return  location;
	}
	
	public List<ViolationReport> getViolationReportsByViolationId(Integer violationId) {
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		Query query = session.createQuery("from ViolationReport WHERE violationId = :violationId");
		query.setParameter("violationId", violationId);
		List list = query.list();
		//ViolationReport location = (ViolationReport)list.get(0);
		
		return list;
	}
	
	public List<Violation> listViolationsBetweenDates(Timestamp startDate, Timestamp endDate,List<Violation> violationList) {
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		List<Violation> returnList = new ArrayList<Violation>();
		
	/*	if(violationList != null && !violationList.isEmpty()){
			returnList = violationList;
		}
	*/	
		Query query = session.createQuery("from Violation v WHERE v.id in ( SELECT distinct(vr.violationId) FROM ViolationReport vr WHERE vr.date >= :startDate AND  vr.date <= :endDate AND isFirstReport = 1)");
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		List<Violation> list = query.list();
		if(!list.isEmpty() && violationList != null && !violationList.isEmpty()){
			Iterator iter = list.iterator();
			
			while(iter.hasNext()){
				Violation v = (Violation)iter.next();
				Iterator iter2 = violationList.iterator();
				while(iter2.hasNext()){
					Violation v2 = (Violation)iter2.next();
					if (v.getId().equals(v2.getId())){
						returnList.add(v);
						break;
					}
				}
			}
			//list.retainAll(returnList);
		}
		//ViolationReport location = (ViolationReport)list.get(0);
		
		return returnList;
	}
	
	public List<Violation> getViolationsBetweenLocations(String upLeftLat,String upLeftLon, String downRightLat,String downRightLon,List<Violation> violations) {
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		double latUpLeft =  Double.parseDouble(upLeftLat);
		double lonUpLeft =  Double.parseDouble(upLeftLon);
		double latDownRight =  Double.parseDouble(downRightLat);
		double lonDownRight =  Double.parseDouble(downRightLon);
		
		Iterator iter = violations.iterator();
		List<Violation> returnList = new ArrayList<Violation>();
		
		while(iter.hasNext()){
			Violation viol = (Violation) iter.next();
			
			if(viol.getLocationId() != null){
			
			Query query = session.createQuery("from Location WHERE id = :locationId");
			query.setParameter("locationId", viol.getLocationId());
			List list = query.list();
			
			if(!list.isEmpty()){
				Location location = (Location) list.get(0);
				
				if(Double.parseDouble(location.getLattitude()) >= latUpLeft && Double.parseDouble(location.getLattitude()) <= latDownRight 
						&& Double.parseDouble(location.getLongtitude()) <= lonUpLeft && Double.parseDouble(location.getLongtitude()) >= lonDownRight){
					returnList.add(viol);
				}
			
			}
			}
		}
		//ViolationReport location = (ViolationReport)list.get(0);
		
		return returnList;
	}
	
	public boolean isBetweenCoords(double latUpLeft,double lonUpLeft,double latDownRight,double lonDownRight){
		
		
		
		return false;
		
	}
	
}
