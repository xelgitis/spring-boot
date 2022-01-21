package com.example.springboot.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.example.springboot.domain.LoginRequest;
import com.example.springboot.domain.LoginResponse;
import com.example.springboot.domain.RegistrationRequest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class LoginControllerIT {
	
	@Autowired
	TestRestTemplate restTamplete;	

	@Test
	@Transactional
	void testLoginCheck() {
		
		LoginRequest request = new LoginRequest("olga.jesic", "olga.jesic");
		//request.setUsername("olga.jesic");
		//request.setPassword("olga.jesic");
		
		LoginResponse logedUser = restTamplete.postForObject("/login", request, LoginResponse.class);
		
		assertNotNull(logedUser.getUsername());
	}
	
	@Test
	@Transactional
	void testLoginWrongUsername() {
		
		LoginRequest request = new LoginRequest("oolga.jesic", "olga.jesic");
		//request.setUsername("oolga.jesic");
		//request.setPassword("olga.jesic");
		
		LoginResponse logedUser = restTamplete.postForObject("/login", request, LoginResponse.class);
		
		assertNull(logedUser.getUsername());
	}
	
	@Test
	@Transactional
	void testLoginWrongPassword() {
		
		LoginRequest request = new LoginRequest("olga.jesic", "olga.jesic");
		//request.setUsername("olga.jesic");
		//request.setPassword("oolga.jesic");
		
		LoginResponse logedUser = restTamplete.postForObject("/login", request, LoginResponse.class);
		
		assertNull(logedUser.getUsername());
	}	
	
	@Test
	@Transactional
	void testLoginNoUsername() {
		
		LoginRequest request = new LoginRequest(null, "olga.jesic");
		//request.setPassword("olga.jesic");
		
		LoginResponse logedUser = restTamplete.postForObject("/login", request, LoginResponse.class);
		
		assertNull(logedUser);
	}	
	
	@Test
	@Transactional
	void testLoginNoPassword() {
		
		LoginRequest request = new LoginRequest("olga.jesic", null);
		//request.setUsername("olga.jesic");
		
		LoginResponse logedUser = restTamplete.postForObject("/login", request, LoginResponse.class);
		
		assertNull(logedUser);
	}	

}
