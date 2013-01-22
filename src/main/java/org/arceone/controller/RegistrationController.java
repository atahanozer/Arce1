package org.arceone.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.arceone.domain.RegistrationValidation;
import org.arceone.domain.User;
import org.arceone.domain.UserEx;
import org.arceone.service.HandicapTypeService;
import org.arceone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
//@RequestMapping("/registrationform.html")
public class RegistrationController {
       // @Autowired
        private RegistrationValidation registrationValidation;
        @Autowired
        //@Resource(name="userService")
        private UserService userService;
    	@Autowired
    	private HandicapTypeService handicapTypeService;
        
        public void setRegistrationValidation(
                        RegistrationValidation registrationValidation) {
                this.registrationValidation = registrationValidation;
        }
      //String dominos = "ş";
        // Display the form on the get request
        @RequestMapping(value = "/showRegistration" ,method = RequestMethod.GET)
        public String showRegistration(Map model) {
        	//model.addAttribute("handicapTypeList", handicapTypeService.getAllToMap());
                UserEx registration = new UserEx();
                model.put("handicapTypeList", handicapTypeService.getAllToMap());
                model.put("registration", registration);
                return "registrationform";
        }

        // Process the form.
        @RequestMapping(value = "/processRegistration" ,method = RequestMethod.POST)
        public String processRegistration(Map model,@Valid UserEx registration,
                        BindingResult result) {
                // set custom Validation by user
        	model.put("registration", registration);
        	RegistrationValidation registrationValidation = new RegistrationValidation();
                registrationValidation.validate(registration, result);
                if (result.hasErrors()) {
                	
                        return "registrationform";
                }
               // userService = new UserService();
                User user = userService.convertUserExToUser(registration);
                userService.add(user);
                userService.addUserHandicapType(user, registration.getHandicapTypes());
                
                return "registrationSuccess";
        }
}
