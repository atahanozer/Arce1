package org.arceone.service;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.arceone.domain.User;
import org.arceone.domain.UserAuth;
import org.arceone.domain.UserEx;
import org.arceone.domain.UserHandicapType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for processing Users
 * 
 */
@Service("userService")
@Transactional
public class UserService {

	protected static Logger logger = Logger.getLogger("service");
	
	//@Autowired(required=true)
	@Resource(name="sessionFactory")
	private SessionFactory sessionFactory;
	
	
	/**
	 * Retrieves all Users
	 * 
	 * @return a list of Users
	 */
	public List<User> getAll() {
		logger.debug("Retrieving all Users");
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		// Create a Hibernate query (HQL)
		Query query = session.createQuery("FROM  User");
		
		// Retrieve all
		return  query.list();
	}
	
	/**
	 * Retrieves a single User
	 */
	public User get( Integer id ) {
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		// Retrieve existing User first
		User User = (User) session.get(User.class, id);
		
		return User;
	}
	
	/**
	 * Retrieves a single User
	 */
	public User getByUserName( String userName ) {
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		Query query = session.createQuery("from User where userName = :userName ");
		query.setParameter("userName", userName);
		List list = query.list();
		User user = null;
		if(!list.isEmpty())
		user = (User)list.get(0);
		//String dominos = "ş";
		// Retrieve existing User first
		//User user = (User) session.get(User.class, id);
		
		return user;
	}
	
	public boolean isAdmin( String userName ) {
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		Query query = session.createQuery("from UserAuth where username = :userName ");
		query.setParameter("userName", userName);
		List list = query.list();
		UserAuth user = null;
		if(!list.isEmpty())
		user = (UserAuth)list.get(0);
		//String dominos = "ş";
		// Retrieve existing User first
		//User user = (User) session.get(User.class, id);
		
		if("ROLE_ADMIN".equalsIgnoreCase(user.getAuthority()) )
			return true;
		
		return false;
	}
	
	/**
	 * Adds a new User
	 */
	public void add(User user) {
		logger.debug("Adding new User");
		UserAuth userAuth = new UserAuth();
		userAuth.setAuthority("ROLE_USER");
		userAuth.setUserName(user.getUserName());
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		// Save
		session.save(user);
		session.save(userAuth);
	}
	
	public void addUserHandicapType(User user,String handicapTypes) {
		logger.debug("Adding UserHandicapType");
		
		if(handicapTypes != null){
		char chars[] = handicapTypes.toCharArray();		
		int i=0;
		while(i<chars.length){
			String c = ""+chars[i];
			if(!(',' == chars[i])){
				UserHandicapType uht = new UserHandicapType(); 
				uht.setUserId(user.getId());
				uht.setHandicapTypeId(Integer.parseInt(c));
				
				// Retrieve session from Hibernate
				Session session = sessionFactory.getCurrentSession();
				
				// Save
				session.save(uht);
			}
			i++;
		}
		}
		


	}
	
	/**
	 * Deletes an existing User
	 * @param id the id of the existing User
	 */
	public void delete(Integer id) {
		logger.debug("Deleting existing User");
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		// Retrieve existing User first
		User User = (User) session.get(User.class, id);
		
		// Delete 
		session.delete(User);
	}
	
	/**
	 * Deletes an existing User
	 * @param id the id of the existing User
	 */
	public void block(Integer id) {
		logger.debug("Deleting existing User");
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		// Retrieve existing User first
		User user = (User) session.get(User.class, id);
		user.setIsBlocked(1);
		// Delete 
		session.save(user);
	}
	
	/**
	 * Edits an existing User
	 */
	public void edit(User user) {
		logger.debug("Editing existing User");
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		// Retrieve existing User via id
		User existingUser = (User) session.get(User.class, user.getId());
		
		// Assign updated values to this User
		existingUser.setUserName(user.getUserName());
		existingUser.setPassword(user.getPassword());
		existingUser.setEmail(user.getEmail());
		existingUser.setIsBlocked(user.getIsBlocked());

		// Save updates
		session.save(existingUser);
	}
	
	public User convertUserExToUser(UserEx userEx) {
		User user = new User();
		user.setConfirmPassword(userEx.getConfirmPassword());
		user.setEmail(userEx.getEmail());
		user.setIsBlocked(userEx.getIsBlocked());
		user.setPassword(userEx.getPassword());
		user.setUserName(userEx.getUserName());
		return user;
	}
	
	public String getUserHandicapType(Integer userId) {
		logger.debug("Retrieving all Users");
		
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		
		// Create a Hibernate query (HQL)
		Query query = session.createQuery("FROM  UserHandicapType WHERE userId = :userId");
		query.setParameter("userId", userId);
		List list = query.list();
		String handicapTypes="";
		if(!list.isEmpty()){
			Iterator iter = list.iterator();
			int i =0;
			while(iter.hasNext()){
				UserHandicapType uht = (UserHandicapType)iter.next();
				if(i==0)
					handicapTypes+=uht.getHandicapTypeId().toString();
				else
					handicapTypes += "," + uht.getHandicapTypeId().toString();
				i++;
			}
		}
		
		
		// Retrieve all
		return  handicapTypes;
	}
}
