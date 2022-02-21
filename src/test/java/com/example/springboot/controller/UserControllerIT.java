package com.example.springboot.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.example.springboot.domain.LoginRequest;
import com.example.springboot.domain.LoginResponse;
import com.example.springboot.domain.User;
import com.example.springboot.domain.UserRequest;

//import lombok.extern.slf4j.Slf4j;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
//@Slf4j
class UserControllerIT {
	
	private String sessionID;
	private String uri;
	private String username = "olga.jesic"; //change the username to test CRUD for different users
	
	@Autowired
	TestRestTemplate restTamplete;
	
	//try all testcases logged as admin and logged as regular user	
	@BeforeEach
	public void initialLogin(){
		LoginRequest request = LoginRequest.builder()
				               .username("olga.jesic")
				               .password("olga.jesic")
				               .build();
	
		LoginResponse logedUser = restTamplete.postForObject("/login", request, LoginResponse.class);
		sessionID = logedUser.getSessionId();	
		uri = "/users/"+ sessionID + "?user=" + username;
	}	
	
	//valid test scenarios 
	@Test
	@Transactional
	void testGetUser() {
		User user = restTamplete.getForObject(uri, User.class);
		assertNotNull(user.getUsername());	
	}	

	@Test
	@Transactional
	void testUpdateUser() {
		UserRequest request = UserRequest.builder()
				              .email("olga.jesic@gmail.com")
				              .build();
		restTamplete.put(uri, request);
		User user = restTamplete.getForObject(uri, User.class);
		assertTrue(user.getEmail().equalsIgnoreCase("olga.jesic@gmail.com"));
	}

	@Test
	@Transactional
	void testDeleteUser() {
		restTamplete.delete(uri);
		User user = restTamplete.getForObject(uri, User.class);
		assertNull(user.getUsername());
	}
	
	//invalid test scenarios - change the username to the one which does not exist in the DB or 
	//when someone is logged as regular and than try to get data from some other user
	@Test
	@Transactional
	void testGetInvalidUser() {
		User user = restTamplete.getForObject(uri, User.class);
		assertNull(user.getUsername());	
	}
	
	@Test
	@Transactional
	void testUpdateInvalidUser() {
		UserRequest request = UserRequest.builder()
				              .email("olga.jesic@mozzartbet.com")
				              .build();
		restTamplete.put(uri, request);
		//check that this user does not exist - meaning no update was done or
		//it is not allowed to change this user
		User user = restTamplete.getForObject(uri, User.class);
		assertNull(user.getUsername());	
	}	
	
	@Test
	@Transactional
	void testDeleteInvalidUser() {
		restTamplete.delete(uri);
		User user = restTamplete.getForObject(uri, User.class);
		//check that this user does not exist - meaning no delete was done or
		//it was not allowed to delete this user
		assertNull(user.getUsername());
	}	
	
}
