package com.warehousekeeper.root.controllers;


import com.warehousekeeper.root.dto.PersonDTO;
import com.warehousekeeper.root.models.Person;
import com.warehousekeeper.root.services.RegistrationService;
import com.warehousekeeper.root.util.PersonValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

/**
 * @author Dmytro
 * @version 1.0
 * Controller responsible for new user registration
 */
@Controller
@RequestMapping("/auth")
public class AuthController {

    private final PersonValidator personValidator;
    private final RegistrationService registrationService;
    private  final ModelMapper modelMapper;
    @Autowired
    public AuthController(PersonValidator personValidator, RegistrationService registrationService, ModelMapper modelMapper) {
        this.personValidator = personValidator;
        this.registrationService = registrationService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/login")
    public String login(){
       return "auth/login";
   }

   @GetMapping("/registration")
    public String reg(@ModelAttribute("person") Person person){
       return "auth/reg";
   }
    /**
     * Register new user by calling register method on AuthService
     *
     * @param personDTO object with user credentials for registration
     */
   @PostMapping("/registration")
    public String performReg(@ModelAttribute("person") @Valid PersonDTO personDTO, BindingResult bindingResult){
       Person person = convertToPerson(personDTO);
        personValidator.validate(person, bindingResult);

       if(bindingResult.hasErrors())
           return "/auth/reg";
       registrationService.register(person);
       return "redirect:/auth/login";
   }
    private Person convertToPerson(PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }
}
