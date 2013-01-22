package org.arceone.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;
import org.arceone.domain.Comment;
import org.arceone.domain.CommentEx;
import org.arceone.domain.Tag;
import org.arceone.domain.User;
import org.arceone.domain.Violation;
import org.arceone.domain.ViolationGeneral;
import org.arceone.domain.ViolationReportProof;
import org.arceone.service.CommentService;
import org.arceone.service.HandicapTypeService;
import org.arceone.service.PicasaService;
import org.arceone.service.TagService;
import org.arceone.service.UserService;
import org.arceone.service.ViolationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;


/**
 * Handles and retrieves user request
 */
@Controller
@RequestMapping("/violation")
public class ViolationController {
	
	
	protected static Logger logger = Logger.getLogger("controller");
	@Autowired
	//@Resource(name="violationService")
	private ViolationService violationService;
	
	@Autowired
	private PicasaService picasaService;
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private HandicapTypeService handicapTypeService;
	
	@Autowired
	private TagService tagService;
	
	@Autowired
	private UserService userService;
	/**
	 * Handles and retrieves all violations and show it in a JSP page
	 * 
	 * @return the name of the JSP page
	 */
    @RequestMapping(value = "/violations", method = RequestMethod.GET)
    public String getViolations(Model model) {
    	//String dominos = "ş";
    	logger.debug("Received request to show all violations");
    	
    	// Retrieve all violations by delegating the call to UserService
    	List<Violation> violations = violationService.getAll();
    	
    	// Attach violations to the Model
    	model.addAttribute("violations", violations);
    	
    	// This will resolve to /WEB-INF/jsp/userspage.jsp
    	return "violations";
	}
    
    @RequestMapping(value = "/violations/news", method = RequestMethod.GET)
    public String getnewViolations(Model model) {
    	
    	logger.debug("Received request to show all violations");
    	
    	// Retrieve all violations by delegating the call to UserService
    	List<Violation> violations = violationService.getNews();
    	
    	// Attach violations to the Model
    	model.addAttribute("violations", violations);
    	
    	// This will resolve to /WEB-INF/jsp/userspage.jsp
    	return "violations";
	}
    
    /**
     * Retrieves the add page
     * 
     * @return the name of the JSP page
     */
    @RequestMapping(value = "/violations/add", method = RequestMethod.GET)
    public String getAdd(Model model) {
    	logger.debug("Received request to show add page");
    
    	if(getUserId() > 0){
    	
		try {
			model.addAttribute("violationLevelList", ViolationGeneral.getViolationLevelData());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
		model.addAttribute("handicapTypeList", handicapTypeService.getAllToMap());
		
		try {
			model.addAttribute("tagList", getTagParentableData(tagService.getParentableTags()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		   
   
    	// Create new Violation and add to model
    	// This is the formBackingOBject
		ViolationReportProof vrp = new ViolationReportProof();
		vrp.setLastAddedViolationId(violationService.getLastAdded());
    	model.addAttribute("violationAttribute", vrp);

    	// This will resolve to /WEB-INF/jsp/addpage.jsp
    	return "violationAdd";
    	}else{
    		return "login";
    	}
	}
 
    /**
     * Adds a new user by delegating the processing to UserService.
     * Displays a confirmation JSP page
     * 
     * @return  the name of the JSP page
     */
    @RequestMapping(value = "/violations/add", method = RequestMethod.POST)
    public String add(@ModelAttribute("violationAttribute") ViolationReportProof vrp,HttpServletRequest request,
    		Model model) {
		logger.debug("Received request to add new user");
		
    	// The "violationAttribute" model has been passed to the controller from the JSP
    	// We use the name "violationAttribute" because the JSP uses that name
		String fileName2 = null;
		MultipartFile file = vrp.getFileData();
		fileName2 = file.getOriginalFilename();
		
		
	//	String root = request.getServletContext().getRealPath("/");
		
		// Call UserService to do the actual adding
		violationService.add(vrp);
		int vId = violationService.getLastAdded();
		
		try {
  
            String fileName = null;
            InputStream inputStream = null;
            OutputStream outputStream = null;
            String proofUrl="";
            if (file.getSize() > 0) {
                    inputStream = file.getInputStream();
                  /*  if (file.getSize() > 10000) {
                            System.out.println("File Size:::" + file.getSize());
                            return "/uploadfile";
                    }*/
                    System.out.println("size::" + file.getSize());
                    fileName = request.getServletContext().getRealPath("") + "/images/"
                                    + file.getOriginalFilename();
                    outputStream = new FileOutputStream(fileName);
                    System.out.println("fileName:" + file.getOriginalFilename());

                    int readBytes = 0;
                    byte[] buffer = new byte[50000];
                    while ((readBytes = inputStream.read(buffer, 0, 10000)) != -1) {
                            outputStream.write(buffer, 0, readBytes);
                    }
                    
                    outputStream.close();
                    inputStream.close();
                    proofUrl = picasaService.uploadPhoto(fileName,""+vId,"image/png");
            }
            

           violationService.addViolationReportAndProof(vrp,violationService.getLastAdded(),proofUrl,"");
            
           if(fileName != null && "".equalsIgnoreCase(fileName)){
            File deleteFile = new File(fileName);
            deleteFile.delete();
           }
            // ..........................................
       
    } catch (Exception e) {
            e.printStackTrace();
    }	
//0532 606 6067 hukumdar
		List<Violation> violations = violationService.getNews();
    	
    	// Attach violations to the Model
    	model.addAttribute("violations", violations);
    	
		return "violations";
	}
    
    
    /**
     * Adds a new user by delegating the processing to UserService.
     * Displays a confirmation JSP page
     * 
     * @return  the name of the JSP page
     */
    @RequestMapping(value = "/violations/addMobile", method = RequestMethod.POST)
    public String addForHttpPost(@ModelAttribute("violationAttribute") ViolationReportProof vrp,HttpServletRequest request,
    		Model model) {
		logger.debug("Received request to add new user");
		
    	// The "violationAttribute" model has been passed to the controller from the JSP
    	// We use the name "violationAttribute" because the JSP uses that name
	
		// Call UserService to do the actual adding
		
		try{
        	if(vrp.getTagIdString() != null)
            	vrp.setTagId(Integer.parseInt( vrp.getTagIdString()));
            if(vrp.getTagId2String() != null)
        		vrp.setTagId2(Integer.parseInt( vrp.getTagId2String()));
            if(vrp.getLocationTag() != null)
        		vrp.setLocationTag(ViolationGeneral.convertTrToEng(vrp.getLocationTag()));
            	
    	}catch(Exception e){
    		
    	}
		violationService.add(vrp);
		int vId = violationService.getLastAdded();
		
		if(vrp.getFileData() != null){
			String fileName2 = null;
			MultipartFile file = vrp.getFileData();
			if(file != null){
			fileName2 = file.getOriginalFilename();
				try {
		  
		            String fileName = null;
		            InputStream inputStream = null;
		            OutputStream outputStream = null;
		            String proofUrl="";
		            if (file.getSize() > 0) {
		                    inputStream = file.getInputStream();
		                  /*  if (file.getSize() > 10000) {
		                            System.out.println("File Size:::" + file.getSize());
		                            return "/uploadfile";
		                    }*/
		                    System.out.println("size::" + file.getSize());
		                    fileName = request.getServletContext().getRealPath("") + "/images/"
		                                    + file.getOriginalFilename();
		                    outputStream = new FileOutputStream(fileName);
		                    System.out.println("fileName:" + file.getOriginalFilename());
		
		                    int readBytes = 0;
		                    byte[] buffer = new byte[50000];
		                    while ((readBytes = inputStream.read(buffer, 0, 10000)) != -1) {
		                            outputStream.write(buffer, 0, readBytes);
		                    }
		                    
		                    outputStream.close();
		                    inputStream.close();
		                    proofUrl = picasaService.uploadPhoto(fileName,""+vId,"image/png");
		            }
		            
		
		           violationService.addViolationReportAndProof(vrp,violationService.getLastAdded(),proofUrl,"");
		            
		           if(fileName != null && "".equalsIgnoreCase(fileName)){
		            File deleteFile = new File(fileName);
		            deleteFile.delete();
		           }
		            // ..........................................
		       
		    } catch (Exception e) {
		            e.printStackTrace();
		    }	
		 }
		}else{
			violationService.addViolationReportAndProof(vrp,violationService.getLastAdded(),vrp.getProofUrl(),"");
			
		}
		//List<Violation> violations = violationService.getNews();
    	
    	// Attach violations to the Model
    	//model.addAttribute("violations", violations);
    	
		return "violations";
	}
    
    /**
     * Deletes an existing user by delegating the processing to UserService.
     * Displays a confirmation JSP page
     * 
     * @return  the name of the JSP page
     */
    @RequestMapping(value = "/violations/delete", method = RequestMethod.GET)
    public String delete(@RequestParam(value="id", required=true) Integer id, 
    										Model model) {
   
		logger.debug("Received request to delete existing user");
		
		// Call UserService to do the actual deleting
		violationService.delete(id);
		
		// Add id reference to Model
		model.addAttribute("id", id);
    	
    	// This will resolve to /WEB-INF/jsp/deletedpage.jsp
		return "violationDeleted";
	}
    
    /**
     * Retrieves the edit page
     * 
     * @return the name of the JSP page
     */
    @RequestMapping(value = "/violations/edit", method = RequestMethod.GET)
    public String getEdit(@RequestParam(value="id", required=true) Integer id,  
    										Model model) {
    	logger.debug("Received request to show edit page");
    
    	// Retrieve existing Violation and add to model
    	// This is the formBackingOBject
    	model.addAttribute("violationAttribute", violationService.get(id));
    	
    	// This will resolve to /WEB-INF/jsp/editpage.jsp
    	return "violationEdit";
	}
    
    /**
     * Edits an existing user by delegating the processing to UserService.
     * Displays a confirmation JSP page
     * 
     * @return  the name of the JSP page
     */
    @RequestMapping(value = "/violations/edit", method = RequestMethod.POST)
    public String saveEdit(@ModelAttribute("violationAttribute") Violation user, 
    										   @RequestParam(value="id", required=true) Integer id, 
    												Model model) {
    	logger.debug("Received request to update user");
    
    	// The "violationAttribute" model has been passed to the controller from the JSP
    	// We use the name "violationAttribute" because the JSP uses that name
    	
    	// We manually assign the id because we disabled it in the JSP page
    	// When a field is disabled it will not be included in the ModelAttribute
    	user.setId(id);
    	
    	// Delegate to UserService for editing
    	violationService.edit(user);
    	
    	// Add id reference to Model
		model.addAttribute("id", id);
		
    	// This will resolve to /WEB-INF/jsp/editedpage.jsp
		return "violationEdited";
	}
    
    /**
     * Deletes an existing user by delegating the processing to UserService.
     * Displays a confirmation JSP page
     * 
     * @return  the name of the JSP page
     */
    @RequestMapping(value = "/violations/block", method = RequestMethod.GET)
    public String block(@RequestParam(value="id", required=true) Integer id,  
    										Model model) {
    	logger.debug("Received request to show edit page");
    
    	// Retrieve existing Violation and add to model
    	// This is the formBackingOBject
    	Violation user = violationService.get(id);
    	model.addAttribute("violationAttribute", user);
    	
    	// This will resolve to /WEB-INF/jsp/editpage.jsp
    	return "violationEdit";
	}
    
    /**
     * Retrieves the edit page
     * 
     * @return the name of the JSP page
     */
    @RequestMapping(value = "/violations/show", method = RequestMethod.GET)
    public String show(@RequestParam(value="id", required=true) int id,  
    										Model model) {
    	logger.debug("Received request to show edit page");
    
    	// Retrieve existing Violation and add to model
    	// This is the formBackingOBject
    	ViolationReportProof vrp = violationService.getVRP(id);
    	
    	Integer userId=getUserId();
    	if(userId > 0){
    		vrp.setIsFollowing(violationService.isFollowing(id,userId));
    	}else{
    		vrp.setIsFollowing(0);
    	}
    	model.addAttribute("proofs", violationService.listProofsByViolationId(id));
    	
    	model.addAttribute("violationAttribute", vrp);
    	
    	List<CommentEx> comments = commentService.getByViolationId(id);
    	
    	// Attach violations to the Model
    	model.addAttribute("comments", comments);
    	
    	List<Tag> tags = tagService.getTagsByViolationId(id);
    	
    	// Attach violations to the Model
    	model.addAttribute("tags", tags);
    	
    	model.addAttribute("handicapTypeList", handicapTypeService.getHandicaptypesByViolationId(id));
    	
    	// This will resolve to /WEB-INF/jsp/editpage.jsp
    	return "violationShow";
	}
    
    @RequestMapping(value = "/violations/resolve", method = RequestMethod.POST)
    public String resolve(@ModelAttribute("violationAttribute") ViolationReportProof vrp,HttpServletRequest request, 
    										   @RequestParam(value="id", required=true) Integer id, 
    												Model model) {
    	logger.debug("Received request to resolve violation");
		
    	boolean isResolve = true;
    	boolean isReport = true;
    	boolean isAbuse = false;
    	 Authentication auth = SecurityContextHolder.getContext().getAuthentication();
         String name = auth.getName(); //get logged in username
         Collection<GrantedAuthority> auths =       auth.getAuthorities();
         Iterator iter = auths.iterator();
         while(iter.hasNext()){
        	 GrantedAuthority ga = (GrantedAuthority) iter.next();
        	 if ("ROLE_ADMIN".equalsIgnoreCase(ga.getAuthority()))
        		 isReport = false;
         }
        Integer resolverId = violationService.getUserIdByUsername(name);
         
    	model.addAttribute("violationAttribute", violationService.getVRP(id));
    	
    	Violation violation = violationService.get(id);
    	
    	if("ABUSE".equalsIgnoreCase(vrp.getStatus())){
    		isAbuse = true;
    	}
    	
	    if(!isReport){	
	    	if(ViolationGeneral.VIOLATION_STATUS_RESOLVED.equalsIgnoreCase(vrp.getStatus())){
	    		
	    		violation.setStatus(ViolationGeneral.VIOLATION_STATUS_NEW);
	    		isResolve = false;
	    	}else{
	        violation.setResolverId(resolverId);
	    	violation.setStatus(ViolationGeneral.VIOLATION_STATUS_RESOLVED);
	    	}
	    }else{
	    	violation.setViolationLevel(violation.getViolationLevel() + 5);
	    	isResolve = false;
	    }
	    
		if("ABUSE".equalsIgnoreCase(vrp.getStatus())){
    		isAbuse = true;
    		isReport = false;
    	}
	    
    	violationService.edit(violation);
    	
    	// The "violationAttribute" model has been passed to the controller from the JSP
    	// We use the name "violationAttribute" because the JSP uses that name
		String fileName2 = null;
		MultipartFile file = vrp.getFileData();
		fileName2 = file.getOriginalFilename();
	
		try {
  
            String fileName = null;
            InputStream inputStream = null;
            OutputStream outputStream = null;
            String proofUrl="";
            if (file.getSize() > 0) {
                    inputStream = file.getInputStream();
                  /*  if (file.getSize() > 10000) {
                            System.out.println("File Size:::" + file.getSize());
                            return "/uploadfile";
                    }*/
                    System.out.println("size::" + file.getSize());
                    fileName = request.getServletContext().getRealPath("") + "/images/"
                                    + file.getOriginalFilename();
                    outputStream = new FileOutputStream(fileName);
                    System.out.println("fileName:" + file.getOriginalFilename());

                    int readBytes = 0;
                    byte[] buffer = new byte[50000];
                    while ((readBytes = inputStream.read(buffer, 0, 10000)) != -1) {
                            outputStream.write(buffer, 0, readBytes);
                    }
                    
                    outputStream.close();
                    inputStream.close();
                    proofUrl = picasaService.uploadPhoto(fileName,""+id,"image/png");
            }
            
            if(isResolve)
            	violationService.addViolationReportAndProof(vrp,violation.getId(),proofUrl,"RESOLVE");
            else if (isReport)
            	violationService.addViolationReportAndProof(vrp,violation.getId(),proofUrl,"REPORT");
            else if (isAbuse)
            	violationService.addViolationReportAndProof(vrp,violation.getId(),proofUrl,"ABUSE");
            else
            	violationService.addViolationReportAndProof(vrp,violation.getId(),proofUrl,"REOPEN");
            
           if(fileName != null && "".equalsIgnoreCase(fileName)){
            File deleteFile = new File(fileName);
            deleteFile.delete();
           }
            // ..........................................
       
    } catch (Exception e) {
            e.printStackTrace();
    }	

    	// This will resolve to /WEB-INF/jsp/addedpage.jsp
		return "violationAdded";
	}
    
    @RequestMapping(value = "/violations/report", method = RequestMethod.POST)
    public String report(@ModelAttribute("violationAttribute") ViolationReportProof vrp,HttpServletRequest request, 
    										   @RequestParam(value="id", required=true) Integer id, 
    												Model model) {
    	logger.debug("Received request to report violation");
		
        Integer resolverId = violationService.getUserIdByUsername(vrp.getUserName());
          	
    	Violation violation = violationService.get(vrp.getId());

	    	violation.setViolationLevel(violation.getViolationLevel() + 5);
	    	
	    	violation.setStatus(ViolationGeneral.VIOLATION_STATUS_NEW);
	    	
    	violationService.edit(violation);
    	
    	try{
        	if(vrp.getTagIdString() != null)
            	vrp.setTagId(Integer.parseInt( vrp.getTagIdString()));
            if(vrp.getTagId2String() != null)
        		vrp.setTagId2(Integer.parseInt( vrp.getTagId2String()));
            if(vrp.getLocationTag() != null)
        		vrp.setLocationTag(ViolationGeneral.convertTrToEng(vrp.getLocationTag()));
            	
    	}catch(Exception e){
    		
    	}

		
		
		violationService.addTagsForReport(vrp,violation);
    	
    	// The "violationAttribute" model has been passed to the controller from the JSP
    	// We use the name "violationAttribute" because the JSP uses that name

		logger.debug("Adding proof");
        String proofUrl=vrp.getProofUrl();
        if(vrp.getFileData() != null){
    		String fileName2 = null;
    		MultipartFile file = vrp.getFileData();
    		fileName2 = file.getOriginalFilename();
			try {
	  
	            String fileName = null;
	            InputStream inputStream = null;
	            OutputStream outputStream = null;

	            if (file.getSize() > 0) {
	                    inputStream = file.getInputStream();
	                  /*  if (file.getSize() > 10000) {
	                            System.out.println("File Size:::" + file.getSize());
	                            return "/uploadfile";
	                    }*/
	                    System.out.println("size::" + file.getSize());
	                    fileName = request.getServletContext().getRealPath("") + "/images/"
	                                    + file.getOriginalFilename();
	                    outputStream = new FileOutputStream(fileName);
	                    System.out.println("fileName:" + file.getOriginalFilename());
	
	                    int readBytes = 0;
	                    byte[] buffer = new byte[50000];
	                    while ((readBytes = inputStream.read(buffer, 0, 10000)) != -1) {
	                            outputStream.write(buffer, 0, readBytes);
	                    }
	                    
	                    outputStream.close();
	                    inputStream.close();
	                    proofUrl = picasaService.uploadPhoto(fileName,""+id,"image/png");
	            }
	            
	            	violationService.addViolationReportAndProof(vrp,violation.getId(),proofUrl,"REPORT");
	 
	           if(fileName != null && "".equalsIgnoreCase(fileName)){
	            File deleteFile = new File(fileName);
	            deleteFile.delete();
	           }
	            // ..........................................
	           logger.debug("Proof added");
	    } catch (Exception e) {
	            e.printStackTrace();
	    }	
	}
		else{
			violationService.addViolationReportAndProof(vrp,violation.getId(),proofUrl,"REPORT");
		}
    	// This will resolve to /WEB-INF/jsp/addedpage.jsp
		return "violationAdded";
	}
  
    @RequestMapping(value = "/violations/resolveMobile", method = RequestMethod.POST)
    public String resolveMobile(@ModelAttribute("violationAttribute") ViolationReportProof vrp,HttpServletRequest request, 
    										   @RequestParam(value="id", required=true) Integer id, 
    												Model model) {
    	logger.debug("Received request to report violation");
		
        Integer resolverId = violationService.getUserIdByUsername(vrp.getUserName());
          	
    	Violation violation = violationService.get(vrp.getId());

    	 violation.setResolverId(resolverId);
	    violation.setStatus(ViolationGeneral.VIOLATION_STATUS_RESOLVED);
	    	
    	violationService.edit(violation);
    	
    	// The "violationAttribute" model has been passed to the controller from the JSP
    	// We use the name "violationAttribute" because the JSP uses that name
		
	
        String proofUrl=vrp.getProofUrl();
		if(vrp.getFileData() != null){
			try {
				String fileName2 = null;
				MultipartFile file = vrp.getFileData();
				fileName2 = file.getOriginalFilename();
	  
	            String fileName = null;
	            InputStream inputStream = null;
	            OutputStream outputStream = null;

	            if (file.getSize() > 0) {
	                    inputStream = file.getInputStream();
	                  /*  if (file.getSize() > 10000) {
	                            System.out.println("File Size:::" + file.getSize());
	                            return "/uploadfile";
	                    }*/
	                    System.out.println("size::" + file.getSize());
	                    fileName = request.getServletContext().getRealPath("") + "/images/"
	                                    + file.getOriginalFilename();
	                    outputStream = new FileOutputStream(fileName);
	                    System.out.println("fileName:" + file.getOriginalFilename());
	
	                    int readBytes = 0;
	                    byte[] buffer = new byte[50000];
	                    while ((readBytes = inputStream.read(buffer, 0, 10000)) != -1) {
	                            outputStream.write(buffer, 0, readBytes);
	                    }
	                    
	                    outputStream.close();
	                    inputStream.close();
	                    proofUrl = picasaService.uploadPhoto(fileName,""+id,"image/png");
	            }
	            
	            	violationService.addViolationReportAndProof(vrp,violation.getId(),proofUrl,"RESOLVE");
	 
	           if(fileName != null && "".equalsIgnoreCase(fileName)){
	            File deleteFile = new File(fileName);
	            deleteFile.delete();
	           }
	            // ..........................................
	       
	    } catch (Exception e) {
	            e.printStackTrace();
	    }	
	}
		else{
			violationService.addViolationReportAndProof(vrp,violation.getId(),proofUrl,"RESOLVE");
		}
    	// This will resolve to /WEB-INF/jsp/addedpage.jsp
		return "violationAdded";
	}   
    
    @RequestMapping(value = "/violations/reportAbuse", method = RequestMethod.POST)
    public String reportAbuse(@ModelAttribute("violationAttribute") ViolationReportProof vrp,HttpServletRequest request, 
    										   @RequestParam(value="id", required=true) Integer id, 
    												Model model) {
    	logger.debug("Received request to report violation");
		
        Integer resolverId = violationService.getUserIdByUsername(vrp.getUserName());
          	
    	Violation violation = violationService.get(vrp.getId());

    	 violation.setResolverId(resolverId);
	    violation.setStatus(ViolationGeneral.VIOLATION_STATUS_RESOLVED);
	    	
    	violationService.edit(violation);
    	
    	// The "violationAttribute" model has been passed to the controller from the JSP
    	// We use the name "violationAttribute" because the JSP uses that name

	
        String proofUrl=vrp.getProofUrl();
		if(vrp.getFileData() != null){
			try {
				String fileName2 = null;
				MultipartFile file = vrp.getFileData();
				fileName2 = file.getOriginalFilename();
	            String fileName = null;
	            InputStream inputStream = null;
	            OutputStream outputStream = null;

	            if (file.getSize() > 0) {
	                    inputStream = file.getInputStream();
	                  /*  if (file.getSize() > 10000) {
	                            System.out.println("File Size:::" + file.getSize());
	                            return "/uploadfile";
	                    }*/
	                    System.out.println("size::" + file.getSize());
	                    fileName = request.getServletContext().getRealPath("") + "/images/"
	                                    + file.getOriginalFilename();
	                    outputStream = new FileOutputStream(fileName);
	                    System.out.println("fileName:" + file.getOriginalFilename());
	
	                    int readBytes = 0;
	                    byte[] buffer = new byte[50000];
	                    while ((readBytes = inputStream.read(buffer, 0, 10000)) != -1) {
	                            outputStream.write(buffer, 0, readBytes);
	                    }
	                    
	                    outputStream.close();
	                    inputStream.close();
	                    proofUrl = picasaService.uploadPhoto(fileName,""+id,"image/png");
	            }
	            
	            	violationService.addViolationReportAndProof(vrp,violation.getId(),proofUrl,"ABUSE");
	 
	           if(fileName != null && "".equalsIgnoreCase(fileName)){
	            File deleteFile = new File(fileName);
	            deleteFile.delete();
	           }
	            // ..........................................
	       
	    } catch (Exception e) {
	            e.printStackTrace();
	    }	
	}
		else{
			violationService.addViolationReportAndProof(vrp,violation.getId(),proofUrl,"ABUSE");
		}
    	// This will resolve to /WEB-INF/jsp/addedpage.jsp
		return "violationAdded";
	}   
    
    @RequestMapping(value="/violations/addComment",method=RequestMethod.POST)
    public @ResponseBody String addComment(@ModelAttribute("violationAttribute") ViolationReportProof vrp, BindingResult result ){
        String returnText;
        if(!result.hasErrors()){
            Comment comment = new Comment();
            comment.setDescription(vrp.getCommentDescription());
            comment.setViolationId(vrp.getId());
            
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String name = auth.getName(); //get logged in username
           Integer userId = violationService.getUserIdByUsername(name);
            
            comment.setUserId(userId);
            commentService.add(comment);
            returnText = "<b>"+name+" ("+ comment.getDate() +") dedi ki:</b> <br>" + comment.getDescription();
        }else{
            returnText = "Sorry, an error has occur. Comment has not been added to list.";
        }
        return returnText;
    }
    
    @RequestMapping(value="/violations/addCommentMobile",method=RequestMethod.POST)
    public @ResponseBody String addCommentMobile(@ModelAttribute("violationAttribute") ViolationReportProof vrp, BindingResult result ){
        String returnText;
        if(!result.hasErrors()){
            Comment comment = new Comment();
            comment.setDescription(vrp.getCommentDescription());
            comment.setViolationId(vrp.getId());
            
           // Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String name = vrp.getUserName(); //get logged in username
           Integer userId = violationService.getUserIdByUsername(name);
            
            comment.setUserId(userId);
            commentService.add(comment);
            returnText = "<b>"+name+" ("+ comment.getDate() +") dedi ki:</b> <br>" + comment.getDescription();
        }else{
            returnText = "Sorry, an error has occur. Comment has not been added to list.";
        }
        return returnText;
    }
    
    @RequestMapping(value="/violations/addTag",method=RequestMethod.POST)
    public @ResponseBody String addTag(@ModelAttribute("violationAttribute") ViolationReportProof vrp, BindingResult result ){
        String returnText;
        if(!result.hasErrors()){
        	
            vrp.setLastAddedViolationId(violationService.getLastAdded());
           boolean isExisting = tagService.addTagViolation(vrp.getTagTitle(),violationService.getLastAdded());
            
            if(isExisting)
            	returnText = "";
            else
            	returnText = "<b>"+vrp.getTagTitle()+"</b>";
        }else{
            returnText = "Sorry, an error has occur. Comment has not been added to list.";
        }
        return returnText;
    }
    //Returns session user id
    public Integer getUserId(){
    	
   	 Authentication auth = SecurityContextHolder.getContext().getAuthentication();
     String name = auth.getName(); //get logged in username
     
     if("guest".equalsIgnoreCase(name)){
    	 return 0;
     }else
     
    return violationService.getUserIdByUsername(name);
    	
    }
    
    @RequestMapping(value="/violations/follow",method=RequestMethod.POST)
    public @ResponseBody String follow(@ModelAttribute("violationAttribute") ViolationReportProof vrp, BindingResult result ){
        String returnText;
        if(!result.hasErrors()){
        	
        	
        	violationService.follow(vrp.getId(),getUserId());
        	
            	returnText = "<b>"+vrp.getTagTitle()+"</b>";
        }else{
            returnText = "Sorry, an error has occur. Comment has not been added to list.";
        }
        return returnText;
    }
    
    @RequestMapping(value="/violations/followMobile",method=RequestMethod.POST)
    public @ResponseBody String followMobile(@ModelAttribute("violationAttribute") ViolationReportProof vrp, BindingResult result ){
        String returnText;
        if(!result.hasErrors()){
        	
        	User user = userService.getByUserName(vrp.getUserName());
        	violationService.follow(vrp.getId(),user.getId());
        	
            	returnText = "Follow is successful!";
        }else{
            returnText = "Sorry, an error has occur. Follow has not been added to list.";
        }
        return returnText;
    }
    
    @RequestMapping(value="/violations/unFollowMobile",method=RequestMethod.POST)
    public @ResponseBody String unFollowMobile(@ModelAttribute("violationAttribute") ViolationReportProof vrp, BindingResult result ){
        String returnText;
        if(!result.hasErrors()){
        	
        	User user = userService.getByUserName(vrp.getUserName());
        	violationService.unFollow(vrp.getId(),user.getId());
        	
        	returnText = "Unfollow is successful!";
        }else{
            returnText = "Sorry, an error has occur. Unfollow has not been added to list.";
        }
        return returnText;
    }
    
    @RequestMapping(value="/violations/unFollow",method=RequestMethod.POST)
    public @ResponseBody String unFollow(@ModelAttribute("violationAttribute") ViolationReportProof vrp, BindingResult result ){
        String returnText;
        if(!result.hasErrors()){
        	
        	
        	violationService.unFollow(vrp.getId(),getUserId());
        	
            	returnText = "<b>"+vrp.getTagTitle()+"</b>";
        }else{
            returnText = "Sorry, an error has occur. Comment has not been added to list.";
        }
        return returnText;
    }
    
    @RequestMapping(value="/violations/getTagCombo",method=RequestMethod.POST)
    public @ResponseBody String getTagCombo(@ModelAttribute("violationAttribute") ViolationReportProof vrp, BindingResult result ){
        String returnText="";
        if(result.getAllErrors().size() < 2){
        	
        	if(vrp.getTagId() != null && vrp.getTagId()> 0){
	        	List<Tag> tags = tagService.getParentedTags(vrp.getTagId());
	        	if(!tags.isEmpty()){
	        		returnText = "<tr id=\"aftertr2\"><td><label for=\"tagId2\"></label></td><td><select id=\"tagId2\" name=\"tagId2\">";
	        		Iterator iter = tags.iterator();
	        		while(iter.hasNext()){
	        			Tag tag = (Tag)iter.next();
	        			returnText += "<option value=\""+tag.getId()+"\">"+tag.getTitle()+"</option>";
	        		}
	        		returnText += "</select></td>tr>";
	        	}
        	}
        	
            //	returnText = "<b>"+vrp.getTagTitle()+"</b>";
        }else{
            returnText = "Sorry, an error has occur. Comment has not been added to list.";
        }
        return returnText;
    }
    
    @RequestMapping(value = "/violations/showOnMap" , method = RequestMethod.GET)
    public String showOnMap(Model model){
    	
      	// Retrieve all violations by delegating the call to 
    	List<Violation> violations = violationService.getAll();
    	
    	List<ViolationReportProof> vrps = violationService.getVRPsFromViolations(violations);
    	
    	model.addAttribute("handicapTypeList", handicapTypeService.getAllToMap());
    	try {
			model.addAttribute("tagList",getTagParentableData(tagService.getParentableTags()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	model.addAttribute("violationAttribute", new ViolationReportProof());
    	
    	// Attach violations to the Model
    	model.addAttribute("violations", vrps);
    	
    	
        return "showOnMap";
    }
    
    @RequestMapping(value = "/violations/getForMap" , method = RequestMethod.POST)
    public String getForMap(@ModelAttribute("violationAttribute") ViolationReportProof vrp,Model model){
    	
      	// Retrieve all violations by delegating the call to 
    	
    	if(vrp.getHandicapType() != null || (vrp.getTagId() != null && vrp.getTagId() > 0)){
    		List<Violation> violations;
    	if(vrp.getHandicapType() != null){	
    	 violations = tagService.listViolationsByTagHandicapType(tagService.getTagsByHandicapType(vrp.getHandicapType()));
    	}else{
    	 violations = violationService.getAll();
    	}
    	if(vrp.getTagId() != null && vrp.getTagId() > 0){
    		Integer tId;
    		if(vrp.getTagId2() != null && vrp.getTagId2() > 0){
    			tId = vrp.getTagId2();
    		}else{
    			tId = vrp.getTagId();
    		}
    		violations =	tagService.listViolationsByAndOperatedTags(violations, vrp.getTagId());
    	}
    	
    	List<ViolationReportProof> vrps = violationService.getVRPsFromViolations(violations);
    	
    	model.addAttribute("handicapTypeList", handicapTypeService.getAllToMap());
    	model.addAttribute("violationAttribute", new ViolationReportProof());
    	model.addAttribute("tagList",getTagParentableData(tagService.getParentableTags()));

    	// Attach violations to the Model
    	model.addAttribute("violations", vrps);
    	
    	
        return "showOnMap";
    	}else{
    		List<Violation> violations = violationService.getAll();
        	
        	List<ViolationReportProof> vrps = violationService.getVRPsFromViolations(violations);
        	
        	model.addAttribute("handicapTypeList", handicapTypeService.getAllToMap());
        	model.addAttribute("violationAttribute", new ViolationReportProof());
        	model.addAttribute("tagList",getTagParentableData(tagService.getParentableTags()));

        	// Attach violations to the Model
        	model.addAttribute("violations", vrps);
        	
        	
            return "showOnMap";
    		
    	}
    }
    
    public static Map getTagParentableData(List<Tag> tags) {
  		Map referenceData = new HashMap();
  		Map<Integer,String> levels = new LinkedHashMap<Integer,String>();
  		levels.put(0, "");
  		Iterator iter = tags.iterator();
  		while(iter.hasNext()){
  			Tag tag = (Tag)iter.next();
  			levels.put(tag.getId(), tag.getTitle());
  		}

  		referenceData.put("levelList", levels);
  		return levels;
  	}
    
}
