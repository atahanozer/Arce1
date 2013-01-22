package org.arceone.service;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.arceone.domain.Comment;
import org.arceone.domain.CommentEx;
import org.arceone.domain.HandicapType;
import org.arceone.domain.HandicapType;
import org.arceone.domain.UserAuth;
import org.arceone.domain.ViolationGeneral;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for processing Comments
 * 
 */
@Service("handicapTypeService")
@Transactional
public class HandicapTypeService {

	protected static Logger logger = Logger.getLogger("service");
	
	@Autowired
	private UserService userService;
	
	//@Autowired(required=true)
	@Resource(name="sessionFactory")
	private SessionFactory sessionFactory;
	
	
	/**
	 * Retrieves all Comments
	 * 
	 * @return a list of Comments
	 */
	public List<HandicapType> getAll() {
		logger.debug("Retrieving all Comments");
		//String dominos = "ş";
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		
		// Create a Hibernate query (HQL)
		Query query = session.createQuery("FROM  HandicapType");
		
		List<HandicapType> handicapTypes = query.list();

		
		// Retrieve all
		return  handicapTypes;
	}
	
	public Map getAllToMap() {
		logger.debug("Retrieving all Handicap Types");
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		
		// Create a Hibernate query (HQL)
		Query query = session.createQuery("FROM  HandicapType");
		
		List<HandicapType> handicapTypes = query.list();
		
		Iterator iter = handicapTypes.iterator();
		
	//	List<CommentEx> commentExs = new ArrayList<CommentEx>();
		Map<String,String> handicapTypeMap = new LinkedHashMap<String,String>();
		while(iter.hasNext()){
			HandicapType handicapType = (HandicapType) iter.next();
			handicapTypeMap.put(handicapType.getId().toString(), handicapType.getTitle());

		}
		// Retrieve all
		return  handicapTypeMap;
	}
	
	public Map getHandicaptypesByViolationId(Integer violationId) {
		logger.debug("Retrieving all Handicap Types");
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		
		// Create a Hibernate query (HQL)
		Query query = session.createQuery("FROM  HandicapType h where h.id in " +
				"(select tht.handicapTypeId from TagHandicapType tht where tht.tagId in  (select tv.tagId from TagViolation tv where tv.violationId in (:violationId) )) ");
		query.setParameter("violationId", violationId);
		List<HandicapType> handicapTypes = query.list();
		
		Iterator iter = handicapTypes.iterator();
		
	//	List<CommentEx> commentExs = new ArrayList<CommentEx>();
		Map<String,String> handicapTypeMap = new LinkedHashMap<String,String>();
		while(iter.hasNext()){
			HandicapType handicapType = (HandicapType) iter.next();
			handicapTypeMap.put(handicapType.getId().toString(), handicapType.getTitle());

		}
		// Retrieve all
		return  handicapTypeMap;
	}
	
	
	/**
	 * Retrieves a single HandicapType
	 */
	public HandicapType get( Integer id ) {
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		// Retrieve existing HandicapType first
		HandicapType HandicapType = (HandicapType) session.get(HandicapType.class, id);
		
		return HandicapType;
	}
	
	/**
	 * Retrieves a single HandicapType
	 */
	public HandicapType getByUserName( String userName ) {
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		Query query = session.createQuery("from HandicapType where userName = :userName ");
		query.setParameter("userName", userName);
		List list = query.list();
		HandicapType handicapType = (HandicapType)list.get(0);
		
		// Retrieve existing HandicapType first
		//HandicapType handicapType = (HandicapType) session.get(HandicapType.class, id);
		
		return handicapType;
	}
	
	/**
	 * Adds a new HandicapType
	 */
	public void add(HandicapType handicapType) {
		logger.debug("Adding new HandicapType");
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		// Save
		session.save(handicapType);
	}
	
	/**
	 * Deletes an existing HandicapType
	 * @param id the id of the existing HandicapType
	 */
	public void delete(Integer id) {
		logger.debug("Deleting existing HandicapType");
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		// Retrieve existing HandicapType first
		HandicapType HandicapType = (HandicapType) session.get(HandicapType.class, id);
		
		// Delete 
		session.delete(HandicapType);
	}
	
	/**
	 * Deletes an existing HandicapType
	 * @param id the id of the existing HandicapType
	 */
	public void block(Integer id) {
		logger.debug("Deleting existing HandicapType");
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		// Retrieve existing HandicapType first
		HandicapType handicapType = (HandicapType) session.get(HandicapType.class, id);
		// Delete 
		session.save(handicapType);
	}
	
	/**
	 * Edits an existing HandicapType
	 */
	public void edit(HandicapType handicapType) {
		logger.debug("Editing existing HandicapType");
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		// Retrieve existing HandicapType via id
		HandicapType existingHandicapType = (HandicapType) session.get(HandicapType.class, handicapType.getId());
		
		
		
		// Assign updated values to this HandicapType
		existingHandicapType.setDescription(handicapType.getDescription());
		existingHandicapType.setLevel(handicapType.getLevel());
		existingHandicapType.setParentTypeId(handicapType.getParentTypeId());
		existingHandicapType.setTitle(handicapType.getTitle());

		// Save updates
		session.save(existingHandicapType);
	}
}
