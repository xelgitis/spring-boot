package com.example.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.domain.LoginRequest;
import com.example.springboot.domain.LoginResponse;
import com.example.springboot.service.LoginService;

import lombok.extern.slf4j.Slf4j;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import javax.validation.Valid;

@RestController
@Slf4j
public class LoginController {
	
	@Autowired
	private LoginService loginService;	
	
	@PostMapping(value = "/login", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public LoginResponse loginCheck(@Valid @RequestBody LoginRequest request) {
		
		log.info("Zahtev za proveru podataka za logovanje korisnika, username :{} ", request.getUsername());

		LoginResponse response = loginService.login(request);
		
		log.info("Response za login username :{} - {} ", request.getUsername(), response);
		
		return response;		
	}
}
