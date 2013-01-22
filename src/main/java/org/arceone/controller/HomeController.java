package org.arceone.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.arceone.domain.Person;
import org.arceone.domain.Violation;
import org.arceone.service.PersonService;
import org.arceone.service.TagService;
import org.arceone.service.UserService;
import org.arceone.service.ViolationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * Handles and retrieves person request
 */
@Controller
/*@RequestMapping("/") */
public class HomeController {

	protected static Logger logger = Logger.getLogger("controller");
	
	@Autowired
	//@Resource(name="violationService")
	private ViolationService violationService;

	@Autowired
	private TagService tagService;
	
	@Autowired
	private UserService userService;
	
@RequestMapping("/")
public String home(Model map) {
        return "index";
}

@RequestMapping(value = "/home" , method = RequestMethod.GET)
public String setUp(Model model){
	
	Integer userId = getUserId();
	
	List<Violation> reportedViolations = new ArrayList<Violation>();
	List<Violation> followedViolations = new ArrayList<Violation>();
	List<Violation> resolvedViolations = new ArrayList<Violation>();
	List<Violation> relatedViolations = new ArrayList<Violation>();
	
	if(userId > 0){
		//String dominos = "ş";
	  	// Retrieve all violations by delegating the call to 
		reportedViolations = violationService.getReportedAllByUserId(userId);
		
		// Retrieve all violations by delegating the call to 
		 followedViolations = violationService.getFollowedAllByUserId(userId);
			
		 resolvedViolations = violationService.getResolvedAllByUserId(userId);
	
		 relatedViolations = tagService.listViolationsByTagHandicapType(tagService.getTagsByHandicapType(userService.getUserHandicapType(userId)));
	
	// Attach violations to the Model
	model.addAttribute("reportedViolations", reportedViolations);
	
	model.addAttribute("followedViolations", followedViolations);
	
	model.addAttribute("resolvedViolations", resolvedViolations);
	
	model.addAttribute("relatedViolations", relatedViolations);
	}else{
		// Retrieve all violations by delegating the call to UserService
    	List<Violation> violations = violationService.getNews();
    	
    	// Attach violations to the Model
    	model.addAttribute("violations", violations);
    	
    	// This will resolve to /WEB-INF/jsp/userspage.jsp
    	return "violations";
	}
	
	
    return "home";
}


@RequestMapping(value = "/showOnMap" , method = RequestMethod.GET)
public String showOnMap(Model model){
	
  	// Retrieve all violations by delegating the call to 
	List<Violation> reportedViolations = violationService.getReportedAllByUserId(getUserId());
	
	// Retrieve all violations by delegating the call to 
	List<Violation> followedViolations = violationService.getFollowedAllByUserId(getUserId());
		
	List<Violation> resolvedViolations = violationService.getResolvedAllByUserId(getUserId());
	
	
	// Attach violations to the Model
	model.addAttribute("reportedViolations", reportedViolations);
	
	model.addAttribute("followedViolations", followedViolations);
	
	model.addAttribute("resolvedViolations", resolvedViolations);
	
    return "showOnMap";
}

@RequestMapping(value = "/index", method = RequestMethod.GET)
public String printMessage(ModelMap model, Principal principal) {

String username = principal.getName();
model.addAttribute("user", username);
model.addAttribute("msg", "Spring Security Custom Login Form");
return "welcome";

}

@RequestMapping(value = "/login", method = RequestMethod.GET)
public String login(ModelMap model) {

return "login";

}

@RequestMapping(value = "/failLogin", method = RequestMethod.GET)
public String failedLogin(ModelMap model) {

model.addAttribute("error", "true");
return "login";

}

@RequestMapping(value = "/logoff", method = RequestMethod.GET)
public String logoff(ModelMap model) {

return "login";

}
   
//Returns session user id
public Integer getUserId(){
	
	 Authentication auth = SecurityContextHolder.getContext().getAuthentication();
 String name = auth.getName(); //get logged in username
 Integer userId = 0;
	 if(!"guest".equalsIgnoreCase(name)){
		 userId = violationService.getUserIdByUsername(name);
	 }
 
return userId;
	
}
    
}
