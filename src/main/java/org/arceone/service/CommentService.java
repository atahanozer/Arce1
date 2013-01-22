package org.arceone.service;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.arceone.domain.Comment;
import org.arceone.domain.CommentEx;
import org.arceone.domain.UserAuth;
import org.arceone.domain.ViolationGeneral;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for processing Comments
 * 
 */
@Service("commentService")
@Transactional
public class CommentService {

	protected static Logger logger = Logger.getLogger("service");
	
	@Autowired
	private UserService userService;
	//String dominos = "ş";
	//@Autowired(required=true)
	@Resource(name="sessionFactory")
	private SessionFactory sessionFactory;
	
	
	/**
	 * Retrieves all Comments
	 * 
	 * @return a list of Comments
	 */
	public List<CommentEx> getAll() {
		logger.debug("Retrieving all Comments");
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		
		// Create a Hibernate query (HQL)
		Query query = session.createQuery("FROM  Comment order by date desc");
		
		List<Comment> comments = query.list();
		Iterator iter = comments.iterator();
		
		List<CommentEx> commentExs = new ArrayList<CommentEx>();
		
		while(iter.hasNext()){
			Comment comment = (Comment) iter.next();
			CommentEx commentex = new CommentEx();
			commentex.setDescription(comment.getDescription());
			commentex.setUserName(userService.get(comment.getUserId()).getUserName());
			commentex.setDate(comment.getDate());
			commentExs.add(commentex);
		}
		
		// Retrieve all
		return  commentExs;
	}
	
	public List<CommentEx> getByViolationId(Integer violId) {
		logger.debug("Retrieving all Comments");
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		
		// Create a Hibernate query (HQL)
		Query query = session.createQuery("FROM  Comment where violationId = :violId order by date desc");
		query.setParameter("violId", violId);
		
		List<Comment> comments = query.list();
		Iterator iter = comments.iterator();
		
		List<CommentEx> commentExs = new ArrayList<CommentEx>();
		
		while(iter.hasNext()){
			Comment comment = (Comment) iter.next();
			CommentEx commentex = new CommentEx();
			commentex.setDescription(comment.getDescription());
			commentex.setUserName(userService.get(comment.getUserId()).getUserName());
			commentex.setDate(comment.getDate());
			commentExs.add(commentex);
		}
		
		// Retrieve all
		return  commentExs;
	}
	
	/**
	 * Retrieves a single Comment
	 */
	public Comment get( Integer id ) {
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		// Retrieve existing Comment first
		Comment Comment = (Comment) session.get(Comment.class, id);
		
		return Comment;
	}
	
	/**
	 * Retrieves a single Comment
	 */
	public Comment getByUserName( String userName ) {
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		Query query = session.createQuery("from Comment where userName = :userName ");
		query.setParameter("userName", userName);
		List list = query.list();
		Comment comment = (Comment)list.get(0);
		
		// Retrieve existing Comment first
		//Comment comment = (Comment) session.get(Comment.class, id);
		
		return comment;
	}
	
	/**
	 * Adds a new Comment
	 */
	public void add(Comment comment) {
		logger.debug("Adding new Comment");
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		comment.setDate(ViolationGeneral.getSqlDate());
		// Save
		session.save(comment);
	}
	
	/**
	 * Deletes an existing Comment
	 * @param id the id of the existing Comment
	 */
	public void delete(Integer id) {
		logger.debug("Deleting existing Comment");
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		// Retrieve existing Comment first
		Comment Comment = (Comment) session.get(Comment.class, id);
		
		// Delete 
		session.delete(Comment);
	}
	
	/**
	 * Deletes an existing Comment
	 * @param id the id of the existing Comment
	 */
	public void block(Integer id) {
		logger.debug("Deleting existing Comment");
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		// Retrieve existing Comment first
		Comment comment = (Comment) session.get(Comment.class, id);
		// Delete 
		session.save(comment);
	}
	
	/**
	 * Edits an existing Comment
	 */
	public void edit(Comment comment) {
		logger.debug("Editing existing Comment");
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		// Retrieve existing Comment via id
		Comment existingComment = (Comment) session.get(Comment.class, comment.getId());
		
		
		
		// Assign updated values to this Comment
		existingComment.setDescription(comment.getDescription());
		existingComment.setUserId(comment.getUserId());
		existingComment.setViolationId(comment.getViolationId());
		existingComment.setDate(ViolationGeneral.getSqlDate());


		// Save updates
		session.save(existingComment);
	}
}
