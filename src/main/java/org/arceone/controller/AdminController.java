package org.arceone.controller;

import java.util.List;

import org.arceone.domain.User;
import org.arceone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AdminController {
	@Autowired
	private UserService userService;	
	
    @RequestMapping(value = "/admin" , method = RequestMethod.GET)
    public String setUp(Model model){
    	//String dominos = "ş";
    	
    	// Retrieve all users by delegating the call to UserService
    	List<User> users = userService.getAll();
    	
    	// Attach users to the Model
    	model.addAttribute("users", users);
    	
        return "admin-home";
    }
}
