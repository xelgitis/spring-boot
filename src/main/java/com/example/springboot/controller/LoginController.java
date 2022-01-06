package com.example.springboot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.domain.LoginRequest;
import com.example.springboot.domain.LoginResponse;
import com.example.springboot.service.LoginService;

import javaslang.control.Try;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import javax.validation.Valid;

@RestController
public class LoginController {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private LoginService loginService;	
	
	@PostMapping(value = "/login", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public LoginResponse loginCheck(@Valid @RequestBody LoginRequest request) {
		
		logger.info("Zahtev za proveru podataka za logovanje korisnika, username :{} ", request.getUsername());

		LoginResponse response = loginService.login(request);
		
		logger.info("Response za login username :{} - {} ", request.getUsername(), response);
		
		return response;		
	}
}
