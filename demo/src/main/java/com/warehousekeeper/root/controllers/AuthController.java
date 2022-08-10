package com.warehousekeeper.root.controllers;


import com.warehousekeeper.root.models.Person;
import com.warehousekeeper.root.services.RegistrationService;
import com.warehousekeeper.root.util.PersonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;


@Controller
@RequestMapping("/auth")
public class AuthController {

    private final PersonValidator personValidator;
    private final RegistrationService registrationService;
    @Autowired
    public AuthController(PersonValidator personValidator, RegistrationService registrationService) {
        this.personValidator = personValidator;
        this.registrationService = registrationService;
    }

    @GetMapping("/login")
    public String login(){
       return "auth/login";
   }

   @GetMapping("/registration")
    public String reg(@ModelAttribute("person") Person person){
       return "auth/reg";
   }

   @PostMapping("/registration")
    public String performReg(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult){
        personValidator.validate(person, bindingResult);

       if(bindingResult.hasErrors())
           return "/auth/reg";
       registrationService.register(person);
       return "redirect:/auth/login";
   }
}
