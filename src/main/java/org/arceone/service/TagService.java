package org.arceone.service;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.arceone.domain.Tag;
import org.arceone.domain.TagHandicapType;
import org.arceone.domain.TagViolation;
import org.arceone.domain.Violation;
import org.arceone.domain.ViolationGeneral;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for processing Comments
 * 
 */
@Service("tagService")
@Transactional
public class TagService {

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
	public List<Tag> getAll() {
		logger.debug("Retrieving all Comments");
		String dominos = "ş";
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		
		// Create a Hibernate query (HQL)
		Query query = session.createQuery("FROM  Tag");
		
		List<Tag> tags = query.list();

		
		// Retrieve all
		return  tags;
	}
	
	/**
	 * Retrieves a single Tag
	 */
	public Tag get( Integer id ) {
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		// Retrieve existing Tag first
		Tag Tag = (Tag) session.get(Tag.class, id);
		
		return Tag;
	}
	
	/**
	 * Retrieves a single Tag
	 */
	public Tag getByTagTitle( String tagTitle ) {
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		//String dominos = "ş";
		Query query = session.createQuery("from Tag where title = :tagTitle ");
		query.setParameter("tagTitle", tagTitle);
		List list = query.list();
		Tag tag =null;
		if(!list.isEmpty())
			tag = (Tag)list.get(0);
		
		// Retrieve existing Tag first
		//Tag tag = (Tag) session.get(Tag.class, id);
		
		return tag;
	}
	
	/**
	 * Retrieves a single Tag
	 */
	public boolean hasTagViolation( Integer tagId, Integer violationId ) {
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		Query query = session.createQuery("from TagViolation where tagId = :tagId and violationId = :violationId ");
		query.setParameter("tagId", tagId);
		query.setParameter("violationId", violationId);
		List list = query.list();
		if(!list.isEmpty())
			return true;
		else
			return false;
		
	}
	
	/**
	 * Retrieves a single Tag
	 */
	public List<Tag> getByLevel( Integer level ) {
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		Query query = session.createQuery("from Tag where level = :level ");
		query.setParameter("level", level);
		List list = query.list();
		// Retrieve existing Tag first
		//Tag tag = (Tag) session.get(Tag.class, id);
		
		return list;
	}
	
	public TagViolation getTagViolationByTagAndViolationId( Integer tagId, Integer violationId ) {
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		Query query = session.createQuery("from TagViolation where tagId = :tagId and violationId = :violationId ");
		query.setParameter("tagId", tagId);
		query.setParameter("violationId", violationId);
		List list = query.list();
		TagViolation tagViolation =null;
		if(!list.isEmpty())
			tagViolation = (TagViolation)list.get(0);
		
		return tagViolation;
	}
	
	public TagViolation getTagViolationByTagId( Integer tagId ) {
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		Query query = session.createQuery("from TagViolation where tagId = :tagId ");
		query.setParameter("tagId", tagId);
		List list = query.list();
		TagViolation tagViolation =null;
		if(!list.isEmpty())
			tagViolation = (TagViolation)list.get(0);
		
		return tagViolation;
	}
	
	public List<TagViolation> getTagViolationByViolationId( Integer violationId ) {
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		Query query = session.createQuery("from TagViolation where violationId = :violationId ");
		query.setParameter("violationId", violationId);
		List<TagViolation> list = query.list();
		
		return list;
	}
	
	public List<Tag> getTagsByViolationId( Integer violationId ) {
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		//List<Tag> tags = new ArrayList<Tag>();
		Query query = session.createQuery("from Tag t where  t.id in (select tv.tagId from TagViolation tv where tv.violationId = :violationId) ");
		query.setParameter("violationId", violationId);
		List<Tag> tags = query.list();
		
		
		return tags;
	}
	
	
	public List<Tag> getParentableTags() {
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		//List<Tag> tags = new ArrayList<Tag>();
		Query query = session.createQuery("from Tag t where  t.level = 2 ");
		List<Tag> tags = query.list();
		
		
		return tags;
	}
	
	public List<Tag> getParentedTags(Integer parentId) {
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		//List<Tag> tags = new ArrayList<Tag>();
		Query query = session.createQuery("from Tag t where  t.parentTagId = :parentId ");
		query.setParameter("parentId", parentId);
		List<Tag> tags = query.list();
		
		
		return tags;
	}
	
	public List<TagHandicapType> getTagsByHandicapType( String handicapTypes ) {
		// Retrieve session from Hibernate
		if(handicapTypes == null)
			return new ArrayList<TagHandicapType>();
		
		Session session = sessionFactory.getCurrentSession();
		List<Integer> idList = new ArrayList<Integer>();
		char chars[] = handicapTypes.toCharArray();		
		int i=0;
		while(i<chars.length){
			String c = ""+chars[i];
			if(!(',' == chars[i])){
				idList.add(Integer.parseInt(c));
			}
			i++;
		}
		
		List list = new ArrayList<TagHandicapType>();
		
		if(!idList.isEmpty()){
		Query query = session.createQuery("from TagHandicapType where handicapTypeId in (:idList) ");
		query.setParameterList("idList", idList);
		 list = query.list();
		}
		//TagHandicapType tag = (TagHandicapType)list.get(0);
		
		// Retrieve existing Tag first
		//Tag tag = (Tag) session.get(Tag.class, id);
		
		return list;
	}
	
	public List<Tag> getTagsByHandicapType( List<TagHandicapType> tagHandicapTypes ) {
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		List<Integer> idList = new ArrayList<Integer>();
		
		Iterator iter = tagHandicapTypes.iterator();
		while(iter.hasNext()){
			TagHandicapType tht = (TagHandicapType) iter.next();
			idList.add(tht.getTagId());
		}
		
		
		Query query = session.createQuery("from Tag where id in (:idList) ");
		query.setParameterList("idList", idList);
		List list = query.list();
		//TagHandicapType tag = (TagHandicapType)list.get(0);
		
		// Retrieve existing Tag first
		//Tag tag = (Tag) session.get(Tag.class, id);
		
		return list;
	}
	
	public List<Tag> getTagsListByHandicapType( List<TagHandicapType> tagHandicapTypes ) {
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		List<Integer> idList = new ArrayList<Integer>();
		
		Iterator iter = tagHandicapTypes.iterator();
		while(iter.hasNext()){
			TagHandicapType tht = (TagHandicapType) iter.next();
			idList.add(tht.getTagId());
		}
		
		
		Query query = session.createQuery("from Tag where id in (:idList) ");
		query.setParameterList("idList", idList);
		List list = query.list();
		//TagHandicapType tag = (TagHandicapType)list.get(0);
		
		// Retrieve existing Tag first
		//Tag tag = (Tag) session.get(Tag.class, id);
		
		return list;
	}
	
	public List<Violation> listViolationsByTagHandicapType( List<TagHandicapType> tagIdList ) {
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		List<Integer> idList = new ArrayList<Integer>();
		
		Iterator iter = tagIdList.iterator();
		
		while(iter.hasNext()){
			TagHandicapType tht =	(TagHandicapType)iter.next();
			idList.add(tht.getTagId());
		}

		List list = new ArrayList<TagHandicapType>();
		
		if(!idList.isEmpty()){	
			Query query = session.createQuery("from  Violation v where v.id in (select distinct(violationId) from TagViolation where tagId in (:idList)) ");
			query.setParameterList("idList", idList);
			 list = query.list();
		}else{
			
		}
		
		return list;
	}
	
	
	public List<Violation> listViolationsByAndOperatedTags( List<Violation> tagIdList, Integer tagId ) {
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		List<Integer> idList = new ArrayList<Integer>();
		
		Iterator iter = tagIdList.iterator();
		
		while(iter.hasNext()){
			Violation tht =	(Violation)iter.next();
			idList.add(tht.getId());
		}

		
		Query query = session.createQuery("from  Violation v where v.id in (select distinct(violationId) from TagViolation where tagId = :tagId and violationId in (:idList)) ");
		query.setParameterList("idList", idList);
		query.setParameter("tagId", tagId);
		List list = query.list();

		
		return list;
	}
	
	public List<Violation> listViolationsByTags( List<String> tagTitleList ) {
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		List<Integer> idList = new ArrayList<Integer>();
		
		
		
		Iterator iter = tagTitleList.iterator();
		List<Tag> tagList = new ArrayList<Tag>();
		long tagCounter =0;
		while(iter.hasNext()){
			String tagName = (String)iter.next();
			
			if(!"".equalsIgnoreCase(tagName)){
			
			Query query = session.createQuery("FROM Tag where title = :tagName ");
			
			query.setParameter("tagName", tagName);
			List<Tag> list = query.list();
			if(!list.isEmpty()){
				tagList.addAll(list);
				Tag tag = list.get(0);
				idList.add(tag.getId());
				tagCounter++;
			}
			}
			//idList.add(tht.getId());
		}
		List<Integer> violIdList = new ArrayList<Integer>();
		List<Violation> violList = new ArrayList<Violation>();
		
		//Iterator iter2 = tagList.iterator();

			//Tag tag = (Tag)iter2.next();
			//Integer tagId = tag.getId();
			Query query2 = session.createQuery("from  Violation v where v.id in (select distinct(tv.violationId) FROM TagViolation tv where tv.tagId in (:idList) and :tagCounter = " +
					"(select COUNT(id) FROM TagViolation tv2 where tv.violationId = tv2.violationId and tv2.tagId in (:idList2)))");
			query2.setParameterList("idList", idList);
			query2.setParameterList("idList2", idList);
			query2.setParameter("tagCounter", tagCounter);
			
			violList = query2.list();


		
		return violList;
	}
	
	/**
	 * Adds a new Tag
	 */
	public void add(Tag tag) {
		logger.debug("Adding new Tag");
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		// Save
		session.save(tag);
	}
	
	/**
	 * Deletes an existing Tag
	 * @param id the id of the existing Tag
	 */
	public void delete(Integer id) {
		logger.debug("Deleting existing Tag");
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		// Retrieve existing Tag first
		Tag Tag = (Tag) session.get(Tag.class, id);
		
		// Delete 
		session.delete(Tag);
	}
	
	/**
	 * Deletes an existing Tag
	 * @param id the id of the existing Tag
	 */
	public void block(Integer id) {
		logger.debug("Deleting existing Tag");
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		// Retrieve existing Tag first
		Tag tag = (Tag) session.get(Tag.class, id);
		// Delete 
		session.save(tag);
	}
	
	/**
	 * Edits an existing Tag
	 */
	public void edit(Tag tag) {
		logger.debug("Editing existing Tag");
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		// Retrieve existing Tag via id
		Tag existingTag = (Tag) session.get(Tag.class, tag.getId());
			
		// Assign updated values to this Tag
		existingTag.setDescription(tag.getDescription());
		existingTag.setLevel(tag.getLevel());
		existingTag.setParentTagId(tag.getParentTagId());
		existingTag.setTitle(tag.getTitle());

		// Save updates
		session.save(existingTag);
	}
	
	public boolean addTagViolation(String tagTitle,Integer lastAddedViolationId) {
		logger.debug("Adding new Tag");
		boolean isExisting = true;
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		Tag tag = getByTagTitle(tagTitle);
		if(tag == null){
			tag = new Tag();
			tag.setTitle(tagTitle);
			tag.setLevel(1);
			session.save(tag);
		}
		
		if(getTagViolationByTagAndViolationId(tag.getId(),lastAddedViolationId*2) == null){
			isExisting = false;
			TagViolation tagViolation = new TagViolation();
			tagViolation.setTagId(tag.getId());
			tagViolation.setViolationId(lastAddedViolationId*2);//Su an violation eklenmedigi icin gecici bir id atiliyor, violation eklendiginde orjinal deger atilacak
			session.save(tagViolation);
		}
		

		// Save
		//session.save(tag);
		return isExisting;
	}
	
}
