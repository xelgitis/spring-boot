package com.example.springboot.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.example.springboot.domain.LoginRequest;
import com.example.springboot.domain.LoginResponse;
import com.example.springboot.domain.User;
import com.example.springboot.domain.UserRequest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class UserControllerIT {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private String sessionID;
	private String uri;
	private String username = "mirjana.jesic"; //change the username to test CRUD for different users
	
	@Autowired
	TestRestTemplate restTamplete;
	
	//try all testcases logged as admin and logged as regular user	
	@BeforeEach
	public void initialLogin(){
		LoginRequest request = LoginRequest.builder().username("olga.jesic").password("olga.jesic").build();
	
		LoginResponse logedUser = restTamplete.postForObject("/login", request, LoginResponse.class);
		sessionID = logedUser.getSessionId();	
		uri = "/users/"+ sessionID + "?user=" + username;
	}	
	
	//valid test scenarios 
	@Test
	@Transactional
	void testGetUser() {
		User user = restTamplete.getForObject(uri, User.class);
		logger.info("Informacije o trazenom korisniku: {} ", user.toString());
		assertNotNull(user.getUsername());	
	}	

	@Test
	@Transactional
	void testUpdateUser() {
		UserRequest request = UserRequest.builder().email("olga.jesic@mozzartbet.com").build();
		restTamplete.put(uri, request);
		User user = restTamplete.getForObject(uri, User.class);
		assertTrue(user.getEmail().equalsIgnoreCase("olga.jesic@mozzartbet.com"));
	}

	@Test
	@Transactional
	void testDeleteUser() {
		restTamplete.delete(uri);
		User user = restTamplete.getForObject(uri, User.class);
		assertNull(user.getUsername());
	}
	
	//invalid test scenarios - change the username to the one which does not exist in the DB
	@Test
	@Transactional
	void testGetInvalidUser() {
		User user = restTamplete.getForObject(uri, User.class);
		assertNull(user.getUsername());	
	}
	
	@Test
	@Transactional
	void testUpdateInvalidUser() {
		UserRequest request = UserRequest.builder().email("olga.jesic@mozzartbet.com").build();
		restTamplete.put(uri, request);
		User user = restTamplete.getForObject(uri, User.class);
		assertNull(user.getUsername());	
	}	
	
	@Test
	@Transactional
	void testDeleteInvalidUser() {
		restTamplete.delete(uri);
		User user = restTamplete.getForObject(uri, User.class);
		assertNull(user.getUsername());
	}	
	
}
