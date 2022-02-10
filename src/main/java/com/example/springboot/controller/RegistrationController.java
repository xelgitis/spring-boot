package com.example.springboot.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.creator.ConversionUtils;
import com.example.springboot.domain.RegistrationRequest;
import com.example.springboot.domain.RegistrationResponse;
import com.example.springboot.domain.User;
import com.example.springboot.service.UserService;
import com.example.springboot.validator.RequestValidator;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class RegistrationController {
	
	@Autowired
	private RequestValidator validator;
	
	@Autowired
	private ConversionUtils converter;
	
    @Autowired
    private UserService userService;    
	
    //use @Valid annotation to perform checks defined in RegistrationRequest
	@PostMapping(value = "/register", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public RegistrationResponse register(@Valid @RequestBody RegistrationRequest request) {
		
		log.info("Zahtev za registraciju novog korisnika={} ", request);
		
		request.setUsername(request.getUsername().toLowerCase());
		
		//validate if user already exist or if email exist
		validator.validate(request);
		log.debug("Zavrsena validacija za novog korisnika={} ", request);
		
		//convert request to user
		User user = converter.convertRegistrationRequest(request);
		log.debug("Kreirani korisnik ={} ", user);

		//try to register user and get a response
	    RegistrationResponse response = userService.registerUser(user);
		
        log.info("Odgovor servisa za registraciju za korisnicko ime: {}, response: {}", request.getUsername(), response);
        return response;
	}	    

}
