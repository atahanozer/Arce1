package org.arceone.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.arceone.domain.Tag;
import org.arceone.service.TagService;
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
@RequestMapping("/tag")
public class TagController {

	protected static Logger logger = Logger.getLogger("controller");
	@Autowired
	//@Resource(name="tagService")
	private TagService tagService;
	//String dominos = "ş";
	/**
	 * Handles and retrieves all tags and show it in a JSP page
	 * 
	 * @return the name of the JSP page
	 */
    @RequestMapping(value = "/tags", method = RequestMethod.GET)
    public String getUsers(Model model) {
    	
    	logger.debug("Received request to show all tags");
    	
    	// Retrieve all tags by delegating the call to TagService
    	List<Tag> tags = tagService.getAll();
    	
    	// Attach tags to the Model
    	model.addAttribute("tags", tags);
    	
    	// This will resolve to /WEB-INF/jsp/userspage.jsp
    	return "tags";
	}
    
    /**
     * Retrieves the add page
     * 
     * @return the name of the JSP page
     */
    @RequestMapping(value = "/tags/add", method = RequestMethod.GET)
    public String getAdd(Model model) {
    	logger.debug("Received request to show add page");
    
    	// Create new Tag and add to model
    	// This is the formBackingOBject
    	model.addAttribute("tagAttribute", new Tag());
    	
    	try {
			model.addAttribute("parentTagList", getTagParentableData(tagService.getParentableTags()));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			model.addAttribute("parentTagList",null);
			e1.printStackTrace();
		}
    	
    	try {
			model.addAttribute("levelList", getTagLevelData());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	

    	// This will resolve to /WEB-INF/jsp/addpage.jsp
    	return "tagAdd";
	}
 
    /**
     * Adds a new user by delegating the processing to TagService.
     * Displays a confirmation JSP page
     * 
     * @return  the name of the JSP page
     */
    @RequestMapping(value = "/tags/add", method = RequestMethod.POST)
    public String add(@ModelAttribute("userAttribute") Tag tag) {
		logger.debug("Received request to add new user");
		
    	// The "userAttribute" model has been passed to the controller from the JSP
    	// We use the name "userAttribute" because the JSP uses that name
		
		// Call TagService to do the actual adding
		tagService.add(tag);

    	// This will resolve to /WEB-INF/jsp/addedpage.jsp
		return "tags";
	}
    
    /**
     * Deletes an existing user by delegating the processing to TagService.
     * Displays a confirmation JSP page
     * 
     * @return  the name of the JSP page
     */
    @RequestMapping(value = "/tags/delete", method = RequestMethod.GET)
    public String delete(@RequestParam(value="id", required=true) Integer id, 
    										Model model) {
   
		logger.debug("Received request to delete existing user");
		
		// Call TagService to do the actual deleting
		tagService.delete(id);
		
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
    @RequestMapping(value = "/tags/edit", method = RequestMethod.GET)
    public String getEdit(@RequestParam(value="id", required=true) Integer id,  
    										Model model) {
    	logger.debug("Received request to show edit page");
    
    	// Retrieve existing Tag and add to model
    	// This is the formBackingOBject
    	model.addAttribute("userAttribute", tagService.get(id));
    	
    	// This will resolve to /WEB-INF/jsp/editpage.jsp
    	return "editpage";
	}
    
    /**
     * Edits an existing user by delegating the processing to TagService.
     * Displays a confirmation JSP page
     * 
     * @return  the name of the JSP page
     */
    @RequestMapping(value = "/tags/edit", method = RequestMethod.POST)
    public String saveEdit(@ModelAttribute("userAttribute") Tag user, 
    										   @RequestParam(value="id", required=true) Integer id, 
    												Model model) {
    	logger.debug("Received request to update user");
    
    	// The "userAttribute" model has been passed to the controller from the JSP
    	// We use the name "userAttribute" because the JSP uses that name
    	
    	// We manually assign the id because we disabled it in the JSP page
    	// When a field is disabled it will not be included in the ModelAttribute
    	user.setId(id);
    	
    	// Delegate to TagService for editing
    	tagService.edit(user);
    	
    	// Add id reference to Model
		model.addAttribute("id", id);
		
    	// This will resolve to /WEB-INF/jsp/editedpage.jsp
		return "editedpage";
	}
    
    /**
     * Deletes an existing user by delegating the processing to TagService.
     * Displays a confirmation JSP page
     * 
     * @return  the name of the JSP page
     */
    @RequestMapping(value = "/tags/block", method = RequestMethod.GET)
    public String block(@RequestParam(value="id", required=true) Integer id,  
    										Model model) {
    	logger.debug("Received request to show edit page");
    
    	// Retrieve existing Tag and add to model
    	// This is the formBackingOBject
    	Tag user = tagService.get(id);
    	model.addAttribute("userAttribute", user);
    	
    	// This will resolve to /WEB-INF/jsp/editpage.jsp
    	return "editpage";
	}
    
    /**
     * Retrieves the edit page
     * 
     * @return the name of the JSP page
     */
    @RequestMapping(value = "/tags/show", method = RequestMethod.GET)
    public String show(@RequestParam(value="userName", required=true) String userName,  
    										Model model) {
    	logger.debug("Received request to show edit page");
    
    	// Retrieve existing Tag and add to model
    	// This is the formBackingOBject
    //	model.addAttribute("userAttribute", tagService.getByUserName(userName));
    	
    	// This will resolve to /WEB-INF/jsp/editpage.jsp
    	return "userShow";
	}
    
    
    @RequestMapping(value="{userName}", method = RequestMethod.GET)
	public @ResponseBody ArrayList<Tag> getShopInJSON(@PathVariable String userName) {
    	
    	ArrayList<Tag> shops = new ArrayList<Tag>();
    	//Tag[] shops = new Tag[100];
    	
    	if("0".equalsIgnoreCase(userName)){
		
    		shops = (ArrayList<Tag>) tagService.getAll();
    		
			/*Tag shop = new Tag();
			shop.setUserName("user");
			shops.add(shop);
			Tag shop2 = new Tag();
			shop2.setUserName("admin");
			shops.add(shop2);
			//shop.setStaffName(new String[]{"mkyong1", "mkyong2"});
		*/
    	}else{
    		Tag user = tagService.get(Integer.parseInt(userName));
    		shops.add(user);
    	}
		return shops;
 
	}
    
    public static Map getTagLevelData() throws Exception {
		Map referenceData = new HashMap();
		Map<String,String> levels = new LinkedHashMap<String,String>();
		levels.put("1", "1");
		levels.put("2", "2");
		levels.put("3", "3");

		referenceData.put("levelList", levels);
		return levels;
	}
    
    public static Map getTagParentableData(List<Tag> tags) throws Exception {
  		Map referenceData = new HashMap();
  		Map<Integer,String> levels = new LinkedHashMap<Integer,String>();
  		
  		Iterator iter = tags.iterator();
  		while(iter.hasNext()){
  			Tag tag = (Tag)iter.next();
  			levels.put(tag.getId(), tag.getTitle());
  		}

  		referenceData.put("levelList", levels);
  		return levels;
  	}
    
}
