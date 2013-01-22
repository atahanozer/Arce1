package org.arceone.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.arceone.domain.User;
import org.arceone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Handles and retrieves user request
 */
@Controller
@RequestMapping("/user")
public class UserController {

	protected static Logger logger = Logger.getLogger("controller");
	@Autowired
	//@Resource(name="userService")
	private UserService userService;
	//String dominos = "ş";
	/**
	 * Handles and retrieves all users and show it in a JSP page
	 * 
	 * @return the name of the JSP page
	 */
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String getUsers(Model model) {
    	
    	logger.debug("Received request to show all users");
    	
    	// Retrieve all users by delegating the call to UserService
    	List<User> users = userService.getAll();
    	
    	// Attach users to the Model
    	model.addAttribute("users", users);
    	
    	// This will resolve to /WEB-INF/jsp/userspage.jsp
    	return "userspage";
	}
    
    /**
     * Retrieves the add page
     * 
     * @return the name of the JSP page
     */
    @RequestMapping(value = "/users/add", method = RequestMethod.GET)
    public String getAdd(Model model) {
    	logger.debug("Received request to show add page");
    
    	// Create new User and add to model
    	// This is the formBackingOBject
    	model.addAttribute("userAttribute", new User());

    	// This will resolve to /WEB-INF/jsp/addpage.jsp
    	return "addpage";
	}
 
    /**
     * Adds a new user by delegating the processing to UserService.
     * Displays a confirmation JSP page
     * 
     * @return  the name of the JSP page
     */
    @RequestMapping(value = "/users/add", method = RequestMethod.POST)
    public String add(@ModelAttribute("userAttribute") User user) {
		logger.debug("Received request to add new user");
		
    	// The "userAttribute" model has been passed to the controller from the JSP
    	// We use the name "userAttribute" because the JSP uses that name
		
		// Call UserService to do the actual adding
		userService.add(user);

    	// This will resolve to /WEB-INF/jsp/addedpage.jsp
		return "addedpage";
	}
    
    /**
     * Deletes an existing user by delegating the processing to UserService.
     * Displays a confirmation JSP page
     * 
     * @return  the name of the JSP page
     */
    @RequestMapping(value = "/users/delete", method = RequestMethod.GET)
    public String delete(@RequestParam(value="id", required=true) Integer id, 
    										Model model) {
   
		logger.debug("Received request to delete existing user");
		
		// Call UserService to do the actual deleting
		userService.delete(id);
		
		// Add id reference to Model
		model.addAttribute("id", id);
    	
    	// This will resolve to /WEB-INF/jsp/deletedpage.jsp
		return "deletedpage";
	}
    
    /**
     * Retrieves the edit page
     * 
     * @return the name of the JSP page
     */
    @RequestMapping(value = "/users/edit", method = RequestMethod.GET)
    public String getEdit(@RequestParam(value="id", required=true) Integer id,  
    										Model model) {
    	logger.debug("Received request to show edit page");
    
    	// Retrieve existing User and add to model
    	// This is the formBackingOBject
    	model.addAttribute("userAttribute", userService.get(id));
    	
    	// This will resolve to /WEB-INF/jsp/editpage.jsp
    	return "editpage";
	}
    
    /**
     * Edits an existing user by delegating the processing to UserService.
     * Displays a confirmation JSP page
     * 
     * @return  the name of the JSP page
     */
    @RequestMapping(value = "/users/edit", method = RequestMethod.POST)
    public String saveEdit(@ModelAttribute("userAttribute") User user, 
    										   @RequestParam(value="id", required=true) Integer id, 
    												Model model) {
    	logger.debug("Received request to update user");
    
    	// The "userAttribute" model has been passed to the controller from the JSP
    	// We use the name "userAttribute" because the JSP uses that name
    	
    	// We manually assign the id because we disabled it in the JSP page
    	// When a field is disabled it will not be included in the ModelAttribute
    	user.setId(id);
    	
    	// Delegate to UserService for editing
    	userService.edit(user);
    	
    	// Add id reference to Model
		model.addAttribute("id", id);
		
    	// This will resolve to /WEB-INF/jsp/editedpage.jsp
		return "editedpage";
	}
    
    /**
     * Deletes an existing user by delegating the processing to UserService.
     * Displays a confirmation JSP page
     * 
     * @return  the name of the JSP page
     */
    @RequestMapping(value = "/users/block", method = RequestMethod.GET)
    public String block(@RequestParam(value="id", required=true) Integer id,  
    										Model model) {
    	logger.debug("Received request to show edit page");
    
    	// Retrieve existing User and add to model
    	// This is the formBackingOBject
    	User user = userService.get(id);
    	user.setIsBlocked(1);
    	model.addAttribute("userAttribute", user);
    	
    	// This will resolve to /WEB-INF/jsp/editpage.jsp
    	return "editpage";
	}
    
    /**
     * Retrieves the edit page
     * 
     * @return the name of the JSP page
     */
    @RequestMapping(value = "/users/show", method = RequestMethod.GET)
    public String show(@RequestParam(value="userName", required=true) String userName,  
    										Model model) {
    	logger.debug("Received request to show edit page");
    
    	// Retrieve existing User and add to model
    	// This is the formBackingOBject
    	model.addAttribute("userAttribute", userService.getByUserName(userName));
    	
    	// This will resolve to /WEB-INF/jsp/editpage.jsp
    	return "userShow";
	}
    
    
    @RequestMapping(value="{userName}", method = RequestMethod.GET)
	public @ResponseBody ArrayList<User> getShopInJSON(@PathVariable String userName) {
    	
    	ArrayList<User> shops = new ArrayList<User>();
    	//User[] shops = new User[100];
    	
    	if("0".equalsIgnoreCase(userName)){
		
    		shops = (ArrayList<User>) userService.getAll();
    		
			/*User shop = new User();
			shop.setUserName("user");
			shops.add(shop);
			User shop2 = new User();
			shop2.setUserName("admin");
			shops.add(shop2);
			//shop.setStaffName(new String[]{"mkyong1", "mkyong2"});
		*/
    	}else{
    		User user = userService.get(Integer.parseInt(userName));
    		shops.add(user);
    	}
		return shops;
 
	}
    
}
