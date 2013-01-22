package org.arceone.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.arceone.domain.User;
import org.arceone.domain.UserAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
 
import com.google.gdata.client.*;
import com.google.gdata.client.photos.*;
import com.google.gdata.data.*;
import com.google.gdata.data.media.*;
import com.google.gdata.data.photos.*;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

/**
 * Service for processing Users
 * 
 */
@Service("picasaService")
@Transactional
public class PicasaService {

	protected static Logger logger = Logger.getLogger("service");
	
	//@Autowired(required=true)
	@Resource(name="sessionFactory")
	private  SessionFactory sessionFactory;
	
	
	public  String uploadPhoto( String path , String albumId, String mediaType) {
		// Retrieve session from Hibernate
		Session session = sessionFactory.getCurrentSession();
		String href = null;
		
		PicasawebService myService = new PicasawebService("exampleCo-exampleApp-1");
		try {
			myService.setUserCredentials("boun.swe574.group3@gmail.com", "swe5742012");
		
			URL feedUrl = new URL("http://picasaweb.google.com/data/feed/api/user/boun.swe574.group3@gmail.com?kind=album");
			
			URL uploadUrlConstant = new URL("http://picasaweb.google.com/data/feed/api/user/boun.swe574.group3@gmail.com/albumid/");
			
			URL albumUrl = new URL("http://picasaweb.google.com/data/feed/api/user/boun.swe574.group3@gmail.com/");
			
			
			UserFeed myUserFeed = myService.getFeed(feedUrl, UserFeed.class);
			
			
			AlbumEntry myNewAlbum = new AlbumEntry();
			 
			myNewAlbum.setTitle(new PlainTextConstruct(albumId));
			//myNewAlbum.setDescription(new PlainTextConstruct("My recent trip to France was delightful!"));
			 
			URL uploadUrl = null; 
			
			for (AlbumEntry myAlbum : myUserFeed.getAlbumEntries()) {
				if(albumId.equalsIgnoreCase(myAlbum.getTitle().getPlainText()))
					uploadUrl = new URL(myAlbum.getId());
			    System.out.println(myAlbum.getTitle().getPlainText()+myAlbum.getId());
			}
			
			if(uploadUrl == null){
			AlbumEntry insertedEntry = myService.insert(albumUrl, myNewAlbum);
			uploadUrl = new URL(insertedEntry.getId());
			}
		
			String url = uploadUrl.toString();
			int i = url.lastIndexOf("/");
			String albumIdentity = url.substring(i+1, url.length());
			uploadUrlConstant = new URL(uploadUrlConstant.toString().concat(albumIdentity));
			
			MediaFileSource myMedia = new MediaFileSource(new File(path), mediaType);
			PhotoEntry returnedPhoto = myService.insert(uploadUrlConstant, PhotoEntry.class, myMedia);
			

			if (returnedPhoto.getMediaContents().size() > 0) {
	            // !!!!!!!!!!!!!!!This is exactly JPEG URL
	            href = returnedPhoto.getMediaContents().get(0).getUrl();
	        }
			 
		
			
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

		
		// Retrieve existing User first
		//User User = (User) session.get(User.class, id);
		
		return href;
	}
	
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
		User user = (User)list.get(0);
		
		// Retrieve existing User first
		//User user = (User) session.get(User.class, id);
		
		return user;
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
	
	/**
	 * Deletes an existing User
	 * @param id the id of the existing User
	 */
	public void delete(Integer id) {
		logger.debug("Deleting existing User");
		//String dominos = "ş";
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
}
