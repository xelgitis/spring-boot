package com.example.springboot.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import com.example.springboot.domain.LoginRequest;
import com.example.springboot.domain.LoginResponse;
import com.example.springboot.domain.RegistrationRequest;
import com.example.springboot.domain.RegistrationResponse;
import com.example.springboot.domain.User;
import com.example.springboot.domain.UserRequest;

//import lombok.extern.slf4j.Slf4j;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
//@Slf4j
class UserControllerIT {
	
	private String sessionID;
	private String uri;
	private String loggedUser = "olga.jesic";
	private String username;  //change the username to test CRUD for different users
	
	@Autowired
	TestRestTemplate restTamplete;
	
	//try all testcases logged as admin and logged as regular user	
	@BeforeEach
	public void initialSetUp(){
		//kreiranje administratora i regular usera	
		RegistrationRequest request1 = RegistrationRequest.builder()
                					   .username("bogdan.blazic")
                                       .password("bogdan.blazic")
                                       .address("Ivana Blagojevica 14")
                                       .name("Bogdan Blazic")
                                       .email("bogdan.blazic@mozzartbet.com")
                                       .role("administrator")
                                       .build();

       ResponseEntity<RegistrationResponse> response1 = restTamplete.postForEntity("/register", new HttpEntity<>(request1), RegistrationResponse.class);
       assertEquals(HttpStatus.CREATED, response1.getStatusCode());			
		
	   RegistrationRequest request2 = RegistrationRequest.builder()
					                  .username("olga.jesic")
					                  .password("olga.jesic")
					                  .address("Ivana Blagojevica 14")
					                  .name("Olga Jesic")
					                  .email("olga.jesic@mozzartbet.com")
					                  .role("user")
					                  .build();
			
	    ResponseEntity<RegistrationResponse> response2 = restTamplete.postForEntity("/register", new HttpEntity<>(request2), RegistrationResponse.class);
		assertEquals(HttpStatus.CREATED, response2.getStatusCode());						
		
		//
		LoginRequest request = LoginRequest.builder()
				               .username(loggedUser)
				               .password(loggedUser)
				               .build();
	
		LoginResponse logedUser = restTamplete.postForObject("/login", request, LoginResponse.class);
		sessionID = logedUser.getSessionId();		
	}	
	
	//ulogovati se kao admin i procitati usera koji postoji u bazi
	@Test
	@Transactional
	void testGetUser() {		
		username = "olga.jesic";
		uri = "/users/"+ sessionID + "?user=" + username;
		User user = restTamplete.getForObject(uri, User.class);
		assertNotNull(user.getUsername());	
	}	
	
	//ulogovati se kao admin i dohvatiti usera koji ne postoji u bazi
	@Test
	@Transactional
	void testGetInvalidUser() {
		username = "oolga.jesic";
		uri = "/users/"+ sessionID + "?user=" + username;		
		User user = restTamplete.getForObject(uri, User.class);
		assertNull(user.getUsername());	
	}	

	//ulogovati se kao admin i updejt usera koji postoji u bazi
	@Test
	@Transactional
	void testUpdateUser() {
		username = "olga.jesic";
		uri = "/users/"+ sessionID + "?user=" + username;		
		UserRequest request = UserRequest.builder()
				              .email("olga.jesic@gmail.com")
				              .build();		
		restTamplete.put(uri, request);
		User user = restTamplete.getForObject(uri, User.class);
		assertTrue(user.getEmail().equalsIgnoreCase("olga.jesic@gmail.com"));
	}
	
	//ulogovati se kao admin i updejt usera koji ne postoji u bazi
	@Test
	@Transactional
	void testUpdateInvalidUser() {
		username = "oolga.jesic";
		uri = "/users/"+ sessionID + "?user=" + username;	
		UserRequest request = UserRequest.builder()
				              .email("olga.jesic@mozzartbet.com")
				              .build();
		restTamplete.put(uri, request);
		User user = restTamplete.getForObject(uri, User.class);
		assertNull(user.getUsername());	
	}		

	//ulogovati se kao admin i brisanje usera koji postoji u bazi
	@Test
	@Transactional
	void testDeleteUser() {
		username = "olga.jesic";
		uri = "/users/"+ sessionID + "?user=" + username;
		restTamplete.delete(uri);
		User user = restTamplete.getForObject(uri, User.class);
		assertNull(user.getUsername());
	}

	
	@Test
	@Transactional
	void testDeleteInvalidUser() {
		username = "oolga.jesic";
		uri = "/users/"+ sessionID + "?user=" + username;		
		restTamplete.delete(uri);
		User user = restTamplete.getForObject(uri, User.class);
		assertNull(user.getUsername());
	}	
	
	//ulogovati se kao regular user i procitati drugog usera
	@Test
	@Transactional
	void testGetUserLoggedRegular() {		
		username = "bogdan.blazic";
		uri = "/users/"+ sessionID + "?user=" + username;
		User user = restTamplete.getForObject(uri, User.class);
		assertNull(user.getUsername());	
	}	
	
	//ulogovati se kao regular user i procitati sebe
	@Test
	@Transactional
	void testGetMySelfLoggedRegular() {		
		username = "olga.jesic";
		uri = "/users/"+ sessionID + "?user=" + username;
		User user = restTamplete.getForObject(uri, User.class);
		assertNotNull(user.getUsername());	
	}	
	
	//ulogovati se kao regular user i updejt drugog usera
	@Test
	@Transactional
	void testUpdateUserLoggedRegular() {
		username = "bogdan.blazic";
		uri = "/users/"+ sessionID + "?user=" + username;		
		UserRequest request = UserRequest.builder()
				              .email("bogdan.blazic@gmail.com")
				              .build();		
		restTamplete.put(uri, request);
		User user = restTamplete.getForObject(uri, User.class);
		assertNull(user.getUsername());	
	}
	
	//ulogovati se kao regular user i updejt samog sebe
	@Test
	@Transactional
	void testUpdateMySelfLoggedRegular() {
		username = "olga.jesic";
		uri = "/users/"+ sessionID + "?user=" + username;	
		UserRequest request = UserRequest.builder()
				              .email("olga.jesic@gmail.com")
				              .build();
		restTamplete.put(uri, request);
		User user = restTamplete.getForObject(uri, User.class);
		assertTrue(user.getEmail().equalsIgnoreCase("olga.jesic@gmail.com"));
	}	
	
	//ulogovati se kao regular user i delete drugog usera
	@Test
	@Transactional
	void testDeleteUserLoggedRegular() {
		username = "bogdan.blazic";
		uri = "/users/"+ sessionID + "?user=" + username;		
		restTamplete.delete(uri);
	}
	
	//ulogovati se kao regular user i updejt samog sebe
	@Test
	@Transactional
	void testDeleteMySelfLoggedRegular() {
		username = "olga.jesic";
		uri = "/users/"+ sessionID + "?user=" + username;	
		restTamplete.delete(uri);
		User user = restTamplete.getForObject(uri, User.class);
		assertNull(user.getUsername());
	}		
	
}
