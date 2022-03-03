package com.example.springboot.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.creator.ConversionUtils;
import com.example.springboot.domain.RegistrationRequest;
import com.example.springboot.domain.RegistrationResponse;
import com.example.springboot.domain.User;
import com.example.springboot.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class RegistrationController {
	
	@Autowired
	private ConversionUtils converter;
	
    @Autowired
    private UserService userService;    
	
    @ResponseStatus(HttpStatus.CREATED)
	@PostMapping(value = "/register", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public RegistrationResponse register(@Valid @RequestBody RegistrationRequest request) {
		
		log.info("Zahtev za registraciju novog korisnika={} ", request);
		
		request.setUsername(request.getUsername().toLowerCase());
     		
		User user = converter.convertRegistrationRequest(request);
		log.debug("Kreirani korisnik ={} ", user);

	    RegistrationResponse response = userService.registerUser(user);
		
        log.info("Odgovor servisa za registraciju za korisnicko ime: {}, response: {}", request.getUsername(), response);
        return response;
	}	    

}
