package org.arceone.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.arceone.domain.HandicapType;
import org.arceone.domain.ProofEx;
import org.arceone.domain.RegistrationValidation;
import org.arceone.domain.Tag;
import org.arceone.domain.TagHandicapType;
import org.arceone.domain.User;
import org.arceone.domain.UserEx;
import org.arceone.domain.Violation;
import org.arceone.domain.ViolationEx;
import org.arceone.domain.ViolationReport;
import org.arceone.domain.ViolationReportEx;
import org.arceone.domain.ViolationReportProof;
import org.arceone.service.HandicapTypeService;
import org.arceone.service.TagService;
import org.arceone.service.UserService;
import org.arceone.service.ViolationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Handles and retrieves user request
 */
@Controller
@RequestMapping("/rest")
public class RestController {

	protected static Logger logger = Logger.getLogger("controller");
	@Autowired
	//@Resource(name="userService")
	private UserService userService;
	
	@Autowired
	private ViolationService violationService;
	
	@Autowired
	private TagService tagService;
	
	@Autowired
	private HandicapTypeService handicapTypeService;
	
	/**
	 * Handles and retrieves all users and show it in a JSP page
	 * 
	 * @return the name of the JSP page
	 */
   
    
    @RequestMapping(value="/user/{userName}", method = RequestMethod.GET)
	public @ResponseBody ArrayList<User> getShopInJSON(@PathVariable String userName) {
    	//String dominos = "ş";
    	ArrayList<User> shops = new ArrayList<User>();
    	//User[] shops = new User[100];
    	//String dominos = "ş";
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
    
    @RequestMapping(value="/violation/{violationId}", method = RequestMethod.GET)
 	public @ResponseBody ArrayList<Violation> getViolationInJSON(@PathVariable Integer violationId) {
     	
     	ArrayList<Violation> shops = new ArrayList<Violation>();  	
     	if(violationId == 0){ 		
			shops = (ArrayList<Violation>) violationService.getAll();
     	}else{
     		Violation violation = violationService.get(violationId);
     		shops.add(violation);
     	}
 		return shops;
  
 	}
    
    @RequestMapping(value="/getHandicapTypes/{violationId}", method = RequestMethod.GET)
 	public @ResponseBody List<HandicapType> getHandicapTypesInJSON(@PathVariable Integer violationId) {
     	
    	return handicapTypeService.getAll();
  
 	}
    
    @RequestMapping(value="/getTagsByLevel/{violationId}", method = RequestMethod.GET)
   	public @ResponseBody List<Tag> getTagsByLevelInJSON(@PathVariable Integer violationId) {
       	
      	return tagService.getByLevel(new Integer(violationId));
    
   	}
    
    @RequestMapping(value="/getTagsByParentId/{violationId}", method = RequestMethod.GET)
   	public @ResponseBody List<Tag> getTagsByParentIdInJSON(@PathVariable Integer violationId) {
       	
      	return tagService.getParentedTags(new Integer(violationId));
    
   	}
    
    @RequestMapping(value="/getFollowedByUser/{userName}", method = RequestMethod.GET)
   	public @ResponseBody List<ViolationEx> getFollowedByUserInJSON(@PathVariable String userName) {
       	
    	User user = userService.getByUserName(userName+"");
    	ArrayList<ViolationEx> violationExs = new ArrayList<ViolationEx>();
    	if(user != null){
    			
    		Iterator iter = violationService.getFollowedAllByUserId(user.getId()).iterator();
  		
  		while(iter.hasNext()){
  			Violation viol = (Violation)iter.next();
  			ViolationEx violEx = new ViolationEx(viol);
  			
  			violEx.setProofs(violationService.listProofsByViolationId(viol.getId()));
  			
  			List<ViolationReport> violationReports =  violationService.getViolationReportsByViolationId(viol.getId());
     			
     			List<ViolationReportEx> vReportExs = new ArrayList<ViolationReportEx>();
     			
     					Iterator iter2 = violationReports.iterator();
     					while(iter2.hasNext()){
     						ViolationReport vReport = (ViolationReport) iter2.next();
     						ViolationReportEx vrEx = new ViolationReportEx(vReport);
     						
     						if(vReport.getReporterId() != null){
     	   						vrEx.setUser(userService.get(vReport.getReporterId()));
     						}
     						vReportExs.add(vrEx);
     					}
     			
     			
     			violEx.setReports(vReportExs);
  			
  			//violEx.setReports(violationService.getViolationReportsByViolationId(viol.getId()));
     			violEx.setLocation(violationService.getLocationById(viol.getLocationId()));
     			violationExs.add(violEx);
  		}
      	
  		return violationExs;
    	}
    	else
    		return null;
    
   	}
    
    @RequestMapping(value="/getReportedByUser/{userName}", method = RequestMethod.GET)
   	public @ResponseBody List<ViolationEx> getReportedByUserInJSON(@PathVariable String userName) {
       	
    	User user = userService.getByUserName(userName+"");
    	ArrayList<ViolationEx> violationExs = new ArrayList<ViolationEx>();
    	if(user != null){
    		Iterator iter = violationService.getReportedAllByUserId(user.getId()).iterator();
      		
      		while(iter.hasNext()){
      			Violation viol = (Violation)iter.next();
      			ViolationEx violEx = new ViolationEx(viol);
      			
      			violEx.setProofs(violationService.listProofsByViolationId(viol.getId()));
      			
      			List<ViolationReport> violationReports =  violationService.getViolationReportsByViolationId(viol.getId());
         			
         			List<ViolationReportEx> vReportExs = new ArrayList<ViolationReportEx>();
         			
         					Iterator iter2 = violationReports.iterator();
         					while(iter2.hasNext()){
         						ViolationReport vReport = (ViolationReport) iter2.next();
         						ViolationReportEx vrEx = new ViolationReportEx(vReport);
         						
         						if(vReport.getReporterId() != null){
         	   						vrEx.setUser(userService.get(vReport.getReporterId()));
         						}
         						vReportExs.add(vrEx);
         					}
         			
         			
         			violEx.setReports(vReportExs);
      			
      			//violEx.setReports(violationService.getViolationReportsByViolationId(viol.getId()));
         			violEx.setLocation(violationService.getLocationById(viol.getLocationId()));
         			violationExs.add(violEx);
      		}
          	
      		return violationExs;
      		}
    	else
    		return null;
    
   	}
    
    @RequestMapping(value="/vrp/{violationId}", method = RequestMethod.GET)
 	public @ResponseBody List<ViolationReportProof> getVRPInJSON(@PathVariable Integer violationId) {
     	
     	ArrayList<Violation> violations = new ArrayList<Violation>();  	
     	if(violationId == 0){ 		
			violations = (ArrayList<Violation>) violationService.getAll();
     	}else{
     		Violation violation = violationService.get(violationId);
     		violations.add(violation);
     	}

 		return violationService.getVRPsFromViolations(violations);
  
 	}
    
    
    @RequestMapping(value="/proofByViolationId/{violationId}", method = RequestMethod.GET)
  	public @ResponseBody List<ProofEx> getPBVIInJSON(@PathVariable Integer violationId) {
      	
      	ArrayList<Violation> violations = new ArrayList<Violation>();  	

      		Violation violation = violationService.get(violationId);
      		violations.add(violation);
      		     		
      	
  		return violationService.listProofsByViolationId(violationId);
   
  	}
    
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public String addUser(@RequestBody User user) {
        // do something with user.name, user.email; etc
    	
    	  // set custom Validation by user
    	//model.put("registration", registration);
    	RegistrationValidation registrationValidation = new RegistrationValidation();
            Errors result = null;
		//	registrationValidation.validate(user, result);
       /*     if (result.hasErrors()) {
            	
                    return "error";
            }*/
           // userService = new UserService();
            userService.add(user);

    	
        return "success";
    }
    
    @RequestMapping(value="/getUserInfo/{userName}", method = RequestMethod.GET)
	public @ResponseBody ArrayList<UserEx> getLoginInfoInJSON(@PathVariable String userName) {
    	Integer userId = Integer.parseInt(userName);
    	ArrayList<UserEx> users = new ArrayList<UserEx>();
    	
    	if(userId < 0){


		
    	}else{
    		 User user = null;
    		if(userId==0){
        		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                String name = auth.getName();
              
                
                user = userService.getByUserName(name);
    		}else{

        		
        		 user = userService.get(userId);
    			
    		}

    		if(user == null){
    			
    		}else{
    			
    		
    		UserEx userEx = new UserEx(user);
    		
    		userEx.setHandicapTypes(userService.getUserHandicapType(userId));
    		
    		userEx.setAdmin(userService.isAdmin(user.getUserName()));
    		
    		//Followed Viols
    		List<Violation> followedViolations = violationService.getFollowedAllByUserId(userId);
    		
    		List<ViolationEx> followedViolationExs = new ArrayList<ViolationEx>();
    		
    		Iterator iter = followedViolations.iterator();
    		
    		while(iter.hasNext()){
    			Violation viol = (Violation)iter.next();
    			ViolationEx violEx = new ViolationEx(viol);
    			
    			violEx.setProofs(violationService.listProofsByViolationId(viol.getId()));
    			
	List<ViolationReport> violationReports =  violationService.getViolationReportsByViolationId(viol.getId());
       			
       			List<ViolationReportEx> vReportExs = new ArrayList<ViolationReportEx>();
       			
       					Iterator iter2 = violationReports.iterator();
       					while(iter2.hasNext()){
       						ViolationReport vReport = (ViolationReport) iter2.next();
       						ViolationReportEx vrEx = new ViolationReportEx(vReport);
       						
       						if(vReport.getReporterId() != null){
       	   						vrEx.setUser(userService.get(vReport.getReporterId()));
       						}
       						vReportExs.add(vrEx);
       					}
       			
       			
       			violEx.setReports(vReportExs);
    			
    			
    		//	violEx.setReports(violationService.getViolationReportsByViolationId(viol.getId()));
    			violEx.setLocation(violationService.getLocationById(viol.getLocationId()));
    			
    			followedViolationExs.add(violEx);
    		}
    		
    		userEx.setFollowedViolations(followedViolationExs);
    		
    		//Reported Violations
    		
    		List<Violation> reportedViolations = violationService.getReportedAllByUserId(userId);
    		
    		List<ViolationEx> reportedViolationExs = new ArrayList<ViolationEx>();
    		
    		iter = reportedViolations.iterator();
    		
    		while(iter.hasNext()){
    			Violation viol = (Violation)iter.next();
    			ViolationEx violEx = new ViolationEx(viol);
    			
    			violEx.setProofs(violationService.listProofsByViolationId(viol.getId()));
    			
	List<ViolationReport> violationReports =  violationService.getViolationReportsByViolationId(viol.getId());
       			
       			List<ViolationReportEx> vReportExs = new ArrayList<ViolationReportEx>();
       			
       					Iterator iter2 = violationReports.iterator();
       					while(iter2.hasNext()){
       						ViolationReport vReport = (ViolationReport) iter2.next();
       						ViolationReportEx vrEx = new ViolationReportEx(vReport);
       						
       						if(vReport.getReporterId() != null){
       	   						vrEx.setUser(userService.get(vReport.getReporterId()));
       						}
       						vReportExs.add(vrEx);
       					}
       			
       			
       			violEx.setReports(vReportExs);
    			
    		//	violEx.setReports(violationService.getViolationReportsByViolationId(viol.getId()));
    			
    			reportedViolationExs.add(violEx);
    		}
    		
    		userEx.setReportedViolations(reportedViolationExs);
    		
  		//Resolved Violations
    		
    		List<Violation> resolvedViolations = violationService.getResolvedAllByUserId(userId);
    		
    		List<ViolationEx> resolvedViolationExs = new ArrayList<ViolationEx>();
    		
    		iter = resolvedViolations.iterator();
    		
    		while(iter.hasNext()){
    			Violation viol = (Violation)iter.next();
    			ViolationEx violEx = new ViolationEx(viol);
    			
    			violEx.setProofs(violationService.listProofsByViolationId(viol.getId()));
    			
	List<ViolationReport> violationReports =  violationService.getViolationReportsByViolationId(viol.getId());
       			
       			List<ViolationReportEx> vReportExs = new ArrayList<ViolationReportEx>();
       			
       					Iterator iter2 = violationReports.iterator();
       					while(iter2.hasNext()){
       						ViolationReport vReport = (ViolationReport) iter2.next();
       						ViolationReportEx vrEx = new ViolationReportEx(vReport);
       						
       						if(vReport.getReporterId() != null){
       	   						vrEx.setUser(userService.get(vReport.getReporterId()));
       						}
       						vReportExs.add(vrEx);
       					}
       			
       			
       			violEx.setReports(vReportExs);
    			
    			//violEx.setReports(violationService.getViolationReportsByViolationId(viol.getId()));
    			
    			resolvedViolationExs.add(violEx);
    		}
    		
    		userEx.setResolvedViolations(resolvedViolationExs);
    		
    		
    		users.add(userEx);
    		}
    	}
		return users;
 
	}
    
    @RequestMapping(value="/login/{loginInfo}", method = RequestMethod.GET)
   	public @ResponseBody ArrayList<UserEx> login(@PathVariable String loginInfo) {
       	
       	ArrayList<UserEx> users = new ArrayList<UserEx>();
       	
       String userName = loginInfo.substring(0, loginInfo.lastIndexOf('&'));
       String password = loginInfo.substring(loginInfo.lastIndexOf('&')+1);
       	
        
       
       boolean isValid = true;
       
       User userr = userService.getByUserName(userName);
       
       Integer userId = null;
  		
       UserEx userEx = null;
  		
  		if(userr != null && userr.getPassword().equalsIgnoreCase(password)){
  			userId = userr.getId();
  			 userEx = new UserEx(userr);
  			isValid = true;
  		}else{
  			isValid = false;
  			userEx = new UserEx();
  			userEx.setResult(0);
  			users.add(userEx);
  		}
       
       
       	if(!isValid){
   		

       	}else{
       		User user = userService.get(userId);
       		if(user == null){
       			
       		}else{
       			
       		
       		userEx.setHandicapTypes(userService.getUserHandicapType(userId));
       		
       		//Followed Viols
       		List<Violation> followedViolations = violationService.getFollowedAllByUserId(userId);
       		
       		List<ViolationEx> followedViolationExs = new ArrayList<ViolationEx>();
       		
       		Iterator iter = followedViolations.iterator();
       		
       		while(iter.hasNext()){
       			Violation viol = (Violation)iter.next();
       			ViolationEx violEx = new ViolationEx(viol);
       			
       			violEx.setProofs(violationService.listProofsByViolationId(viol.getId()));
       			
       			
       			List<ViolationReport> violationReports =  violationService.getViolationReportsByViolationId(viol.getId());
       			
       			List<ViolationReportEx> vReportExs = new ArrayList<ViolationReportEx>();
       			
       					Iterator iter2 = violationReports.iterator();
       					while(iter2.hasNext()){
       						ViolationReport vReport = (ViolationReport) iter2.next();
       						ViolationReportEx vrEx = new ViolationReportEx(vReport);
       						
       						if(vReport.getReporterId() != null){
       	   						vrEx.setUser(userService.get(vReport.getReporterId()));
       						}
       						vReportExs.add(vrEx);
       					}
       			
       			
       			violEx.setReports(vReportExs);
       			
       			//violEx.setReports(violationService.getViolationReportsByViolationId(viol.getId()));
       			violEx.setLocation(violationService.getLocationById(viol.getLocationId()));
       			
       			followedViolationExs.add(violEx);
       		}
       		
       		userEx.setFollowedViolations(followedViolationExs);
       		
       		//Reported Violations
       		
       		List<Violation> reportedViolations = violationService.getReportedAllByUserId(userId);
       		
       		List<ViolationEx> reportedViolationExs = new ArrayList<ViolationEx>();
       		
       		iter = reportedViolations.iterator();
       		
       		while(iter.hasNext()){
       			Violation viol = (Violation)iter.next();
       			ViolationEx violEx = new ViolationEx(viol);
       			
       			violEx.setProofs(violationService.listProofsByViolationId(viol.getId()));
       			
	List<ViolationReport> violationReports =  violationService.getViolationReportsByViolationId(viol.getId());
       			
       			List<ViolationReportEx> vReportExs = new ArrayList<ViolationReportEx>();
       			
       					Iterator iter2 = violationReports.iterator();
       					while(iter2.hasNext()){
       						ViolationReport vReport = (ViolationReport) iter2.next();
       						ViolationReportEx vrEx = new ViolationReportEx(vReport);
       						
       						if(vReport.getReporterId() != null){
       	   						vrEx.setUser(userService.get(vReport.getReporterId()));
       						}
       						vReportExs.add(vrEx);
       					}
       			
       			
       			violEx.setReports(vReportExs);
       			
       		//	violEx.setReports(violationService.getViolationReportsByViolationId(viol.getId()));
       			
       			reportedViolationExs.add(violEx);
       		}
       		
       		userEx.setReportedViolations(reportedViolationExs);
       		
     		//Resolved Violations
       		
       		List<Violation> resolvedViolations = violationService.getReportedAllByUserId(userId);
       		
       		List<ViolationEx> resolvedViolationExs = new ArrayList<ViolationEx>();
       		
       		iter = reportedViolations.iterator();
       		
       		while(iter.hasNext()){
       			Violation viol = (Violation)iter.next();
       			ViolationEx violEx = new ViolationEx(viol);
       			
       			violEx.setProofs(violationService.listProofsByViolationId(viol.getId()));
       			
       			List<ViolationReport> violationReports =  violationService.getViolationReportsByViolationId(viol.getId());
       			
       			List<ViolationReportEx> vReportExs = new ArrayList<ViolationReportEx>();
       			
       					Iterator iter2 = violationReports.iterator();
       					while(iter2.hasNext()){
       						ViolationReport vReport = (ViolationReport) iter2.next();
       						ViolationReportEx vrEx = new ViolationReportEx(vReport);
       						
       						if(vReport.getReporterId() != null){
       	   						vrEx.setUser(userService.get(vReport.getReporterId()));
       						}
       						vReportExs.add(vrEx);
       					}
       			
       			
       			violEx.setReports(vReportExs);
       			
       			resolvedViolationExs.add(violEx);
       		}
       		
       		userEx.setResolvedViolations(resolvedViolationExs);
       		
       		
       		users.add(userEx);
       		}
       	}
   		return users;
    
   	}
    
    @RequestMapping(value="/getViolation/{userName}", method = RequestMethod.GET)
  	public @ResponseBody ArrayList<ViolationEx> getViolationInJSON(@PathVariable String userName) {
      	Integer violationId = Integer.parseInt(userName);
      	ArrayList<ViolationEx> violationExs = new ArrayList<ViolationEx>();
      	
      	if(violationId < 0){
  		
      	}else{

      		Violation viol = violationService.get(violationId);
      		 
      		ViolationEx violEx = new ViolationEx(viol);
  			
  			violEx.setProofs(violationService.listProofsByViolationId(viol.getId()));
  			
  			List<ViolationReport> violationReports =  violationService.getViolationReportsByViolationId(viol.getId());
   			
   			List<ViolationReportEx> vReportExs = new ArrayList<ViolationReportEx>();
   			
   					Iterator iter2 = violationReports.iterator();
   					while(iter2.hasNext()){
   						ViolationReport vReport = (ViolationReport) iter2.next();
   						ViolationReportEx vrEx = new ViolationReportEx(vReport);
   						
   						if(vReport.getReporterId() != null){
   	   						vrEx.setUser(userService.get(vReport.getReporterId()));
   						}
   						
   						vReportExs.add(vrEx);
   					}
   			
   			
   			violEx.setReports(vReportExs);
  			
  			
  		//	violEx.setReports(violationService.getViolationReportsByViolationId(viol.getId()));
  			violEx.setLocation(violationService.getLocationById(viol.getLocationId()));
      		 
  			violationExs.add(violEx);
      	}	
  			return violationExs;
   
  	}
    
    @RequestMapping(value="/searchViolations/{parameters}", method = RequestMethod.GET)
  	public @ResponseBody ArrayList<ViolationEx> searchViolationsInJSON(@PathVariable String parameters) {
      //	Integer userId = Integer.parseInt(userName);
    	Integer userId=0;
      	ArrayList<ViolationEx> violationExs = new ArrayList<ViolationEx>();
      	
      	List<Violation> violations = new ArrayList<Violation>();
      	
      	List<String> items = Arrays.asList(parameters.split("\\s*&\\s*"));
      	

      	

      	
      	//Tags
      	String strKeyswords = items.get(1); 
      	List<String> tempKeywords = Arrays.asList(strKeyswords.split("\\s*-\\s*"));
      	
      	List<String> keywords = new ArrayList<String>();
      	
      	Iterator tempIter = tempKeywords.iterator();
      	while(tempIter.hasNext()){
      		
      		String temp = (String)tempIter.next();
      		keywords.add(temp);
      	}
      	
      	
      	if(!"".equalsIgnoreCase(items.get(0)) && (keywords.isEmpty() || "".equalsIgnoreCase(keywords.get(0)))){
          	User user = null;
          	
          	String name = items.get(0); 
            
            user = userService.getByUserName(name);
            
            if(user != null){
            	String handicapTypes = userService.getUserHandicapType(user.getId());
            //	List<TagHandicapType> taghandicaptypes =  tagService.getTagsByHandicapType(handicapTypes);
            	
            	if(!"".equalsIgnoreCase(handicapTypes)){
	            	violations = tagService.listViolationsByTagHandicapType(tagService.getTagsByHandicapType(handicapTypes));
	            	
	            	/*Iterator iter = tags.iterator();
		            	while(iter.hasNext()){
		            		Tag tag = (Tag) iter.next();
		            		if(!keywords.contains(tag.getTitle()))
		            		keywords.add(tag.getTitle());
		            		
		            	}*/
            	}
            }
      	}
      	    	
      	if((keywords.isEmpty() || "".equalsIgnoreCase(keywords.get(0))) ){
      		violations = violationService.getAll();
      	}else{
      		violations = tagService.listViolationsByTags(keywords);
      	}
      
      	//limits
      	String strLimits = items.get(2); 
      	List<String> limits = Arrays.asList(strLimits.split("\\s*-\\s*"));
      	
      	Integer limitUp = Integer.parseInt(limits.get(1));
      	Integer limitDown = Integer.parseInt(limits.get(0));
      	
      	
      	//date between
      	String strDates = items.get(3); 
      	List<String> dates = Arrays.asList(strDates.split("\\s*-\\s*"));
      	
      	if(!"".equalsIgnoreCase(strDates) ){
      	
      	long startDateLong = Long.parseLong(dates.get(0));
      	long endDateLong = Long.parseLong(dates.get(1));
      	
      	 java.sql.Timestamp startDate = new Timestamp(startDateLong);
      	 java.sql.Timestamp endDate = new Timestamp(endDateLong);
      	
      	violations = violationService.listViolationsBetweenDates(startDate, endDate, violations);
      	 
      	}
      	//levels
      	String strLevels = items.get(4); 
      	
      	if(!"".equalsIgnoreCase(strLevels)){
	      	List<String> levels = Arrays.asList(strLevels.split("\\s*-\\s*"));
	      	
	      	Integer levelUp = Integer.parseInt(levels.get(1));
	      	Integer levelDown = Integer.parseInt(levels.get(0));
      	
	
	      	ArrayList<Integer> arl=new ArrayList<Integer>();
	      	int lCounter = 0;
	      	Iterator iterLevel = violations.iterator();
	      	while(iterLevel.hasNext()){
	      		Violation v = (Violation)iterLevel.next();
	      		int vLevel = v.getViolationLevel();
	      			if(!(vLevel <= levelUp && vLevel >= levelDown)){
	      				//violations.remove(v);
	      				arl.add(new Integer(lCounter));
	      				
	      			}
	      			lCounter++;
	      	}
	   //arl yi reverse order la yap   
	      
	      	Collections.reverse(arl);
	      	Iterator iterArl = arl.iterator();
	      	while(iterArl.hasNext()){
	      		Integer i = (Integer)iterArl.next();
	      		violations.remove(i.intValue());
	      	}
      	}
      	
      	
      	//locations
      	if(items.size() > 5){
	      	String strLocations1 = items.get(5); 
	      	List<String> locations1 = Arrays.asList(strLocations1.split("\\s*-\\s*"));
	      	
	      	String strLocations2 = items.get(6); 
	      	List<String> locations2 = Arrays.asList(strLocations2.split("\\s*-\\s*"));
	      	
	      	violations = violationService.getViolationsBetweenLocations(locations1.get(0), locations1.get(1), locations2.get(0), locations2.get(1), violations);
      	
      	}
      	
      	if(limitUp > violations.size())
      		limitUp = violations.size();
      	
      	violations = violations.subList(limitDown, limitUp);
      	
  		Iterator iter = violations.iterator();
  		
  		while(iter.hasNext()){
  			Violation viol = (Violation)iter.next();
  			ViolationEx violEx = new ViolationEx(viol);
  			
  			violEx.setProofs(violationService.listProofsByViolationId(viol.getId()));
  			
  			List<ViolationReport> violationReports =  violationService.getViolationReportsByViolationId(viol.getId());
     			
     			List<ViolationReportEx> vReportExs = new ArrayList<ViolationReportEx>();
     			
     					Iterator iter2 = violationReports.iterator();
     					while(iter2.hasNext()){
     						ViolationReport vReport = (ViolationReport) iter2.next();
     						ViolationReportEx vrEx = new ViolationReportEx(vReport);
     						
     						if(vReport.getReporterId() != null){
     	   						vrEx.setUser(userService.get(vReport.getReporterId()));
     						}
     						vReportExs.add(vrEx);
     					}
     			
     			
     			violEx.setReports(vReportExs);
  			
  			//violEx.setReports(violationService.getViolationReportsByViolationId(viol.getId()));
     			violEx.setLocation(violationService.getLocationById(viol.getLocationId()));
     			violationExs.add(violEx);
  		}
      	
  		return violationExs;
   
  	}
    
    
    @RequestMapping(value = "/run/{id}", method = RequestMethod.GET)
	public String getRunReportPage(@PathVariable Integer id, Model model,HttpSession session,HttpServletRequest request) {
		DefaultHttpClient httpclient = new DefaultHttpClient();//j_spring_security_check http://localhost:8080/spring-hibernate-mysql/
	//	HttpPost httpPost = new HttpPost("http://swe.cmpe.boun.edu.tr:8280/arceone/j_spring_security_check");
		HttpPost httpPost = new HttpPost("http://localhost:8080/spring-hibernate-mysql/tag/tags/add");
		
	/*	StringEntity input = null;
		try {
			input = new StringEntity("{\"j_username\":atahan,\"j_password\":\"atahan\"}");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		input.setContentType("application/json");
		*/
		
		 // Prepare post parameters
        List <NameValuePair> nvps = new ArrayList <NameValuePair>();
        nvps.add(new BasicNameValuePair("j_username", "user"));
        nvps.add(new BasicNameValuePair("j_password", "atahan"));
        nvps.add(new BasicNameValuePair("title", "atahan"));
        nvps.add(new BasicNameValuePair("description", "atahan"));
        nvps.add(new BasicNameValuePair("level", "1"));
        nvps.add(new BasicNameValuePair("parentTagId", null));
        try {
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//httpPost.setEntity(input);
		HttpResponse response2 = null;
		try {
			response2 = httpclient.execute(httpPost);
			response2.getAllHeaders();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
		    System.out.println(response2.getStatusLine());
		    HttpEntity entity2 = (HttpEntity) response2.getEntity();
		    // do something useful with the response body
		    // and ensure it is fully consumed
		    EntityUtils.consume((org.apache.http.HttpEntity) entity2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		    httpPost.releaseConnection();
		}
		
		return "home";
}// 														   Set-Cookie: JSESSIONID=B93701050D83537E86483885796AD9F5; Path=/arceone/; HttpOnly, Location: http://swe.cmpe.boun.edu.tr:8280/arceone/home;jsessionid=B93701050D83537E86483885796AD9F5, Transfer-Encoding: chunked, Date: Sat, 15 Dec 2012 20:00:25 GMT]
 // HTTP/1.1 302 Moved Temporarily [Server: Apache-Coyote/1.1, Set-Cookie: JSESSIONID=F0A46813D12F95974DD98C214574F511; Path=/arceone/; HttpOnly, Set-Cookie: SPRING_SECURITY_REMEMBER_ME_COOKIE=""; Expires=Thu, 01-Jan-1970 00:00:10 GMT; Path=/arceone, Location: http://swe.cmpe.boun.edu.tr:8280/arceone/login.jsp;jsessionid=F0A46813D12F95974DD98C214574F511?error=true, Transfer-Encoding: chunked, Date: Sat, 15 Dec 2012 20:01:48 GMT]
    @RequestMapping(value = "/runAddViolation/{id}", method = RequestMethod.GET)
   	public String runAddViolation(@PathVariable Integer id, Model model,HttpSession session,HttpServletRequest request) {
   		DefaultHttpClient httpclient = new DefaultHttpClient();//j_spring_security_check http://localhost:8080/spring-hibernate-mysql/
   		HttpPost httpPostLogin = new HttpPost("http://swe.cmpe.boun.edu.tr:8280/arceone/j_spring_security_check");
   		HttpPost httpPost = new HttpPost("http://localhost:8080/spring-hibernate-mysql/violation/violations/addMobile");
   		//HttpPost httpPost = new HttpPost("http://localhost:8080/spring-hibernate-mysql/violation/violations/unFollowMobile");
   		
   	/*	StringEntity input = null;
   		try {
   			input = new StringEntity("{\"j_username\":atahan,\"j_password\":\"atahan\"}");
   		} catch (UnsupportedEncodingException e) {
   			// TODO Auto-generated catch block
   			e.printStackTrace();
   		}
   		input.setContentType("application/json");
   		*/
   		
   		 // Prepare post parameters
           List <NameValuePair> nvps = new ArrayList <NameValuePair>();
           nvps.add(new BasicNameValuePair("title", "title"));
           nvps.add(new BasicNameValuePair("description", "çdescription"));
           //nvps.add(new BasicNameValuePair("handicapType", "1,2"));
           nvps.add(new BasicNameValuePair("tagIdString", "64"));
           nvps.add(new BasicNameValuePair("tagId2String", "78"));
           nvps.add(new BasicNameValuePair("tagId3String", "102"));
           nvps.add(new BasicNameValuePair("locationTag", "Mecidiyekoy,Kadikoy"));
         //  nvps.add(new BasicNameValuePair("violationLevelString", "3"));
        //   nvps.add(new BasicNameValuePair("proofUrl", "deneme/deneme.jpg"));
           nvps.add(new BasicNameValuePair("longtitude", "39.0"));
           nvps.add(new BasicNameValuePair("lattitude", "40.1"));
           nvps.add(new BasicNameValuePair("userName", "user"));
           nvps.add(new BasicNameValuePair("id", "1"));
           try {
        	   httpPostLogin.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
   			httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
   		} catch (UnsupportedEncodingException e1) {
   			// TODO Auto-generated catch block
   			e1.printStackTrace();
   		}
   		
   		//httpPost.setEntity(input);
   		HttpResponse response2 = null;
   		try {
   			response2 = httpclient.execute(httpPostLogin);
   			response2.getAllHeaders();
   		} catch (ClientProtocolException e) {
   			// TODO Auto-generated catch block
   			e.printStackTrace();
   		} catch (IOException e) {
   			// TODO Auto-generated catch block
   			e.printStackTrace();
   		}

   		try {
   		    System.out.println(response2.getStatusLine());
   		    HttpEntity entity2 = (HttpEntity) response2.getEntity();
   		    // do something useful with the response body
   		    // and ensure it is fully consumed
   		    EntityUtils.consume((org.apache.http.HttpEntity) entity2);
   		} catch (IOException e) {
   			// TODO Auto-generated catch block
   			e.printStackTrace();
   		} finally {
   		    httpPostLogin.releaseConnection();
   		}
   		
   	//httpPost.setEntity(input);
   		 response2 = null;
   		try {
   			response2 = httpclient.execute(httpPost);
   			response2.getAllHeaders();
   		} catch (ClientProtocolException e) {
   			// TODO Auto-generated catch block
   			e.printStackTrace();
   		} catch (IOException e) {
   			// TODO Auto-generated catch block
   			e.printStackTrace();
   		}

   		try {
   		    System.out.println(response2.getStatusLine());
   		    HttpEntity entity2 = (HttpEntity) response2.getEntity();
   		    // do something useful with the response body
   		    // and ensure it is fully consumed
   		    EntityUtils.consume((org.apache.http.HttpEntity) entity2);
   		} catch (IOException e) {
   			// TODO Auto-generated catch block
   			e.printStackTrace();
   		} finally {
   		    httpPost.releaseConnection();
   		}
   		
   		
   		return "home";
   }// 					
}
