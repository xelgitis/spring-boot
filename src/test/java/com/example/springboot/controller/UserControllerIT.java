package com.example.springboot.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.springboot.domain.ErrorResponseDto;
import com.example.springboot.domain.LoginRequest;
import com.example.springboot.domain.LoginResponse;
import com.example.springboot.domain.RegistrationResponse;
import com.example.springboot.domain.Role;
import com.example.springboot.domain.User;
import com.example.springboot.domain.UserRequest;
import com.example.springboot.service.UserService;

import lombok.extern.slf4j.Slf4j;

//import lombok.extern.slf4j.Slf4j;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Slf4j
class UserControllerIT {
	
	private String sessionID;
	private Map<String, String> params = new HashMap<>();
	private String uri = "/users/{sessionID}/?user={user}";
	private String username;  //change the username to test CRUD for different users
	
	@Autowired
	TestRestTemplate restTamplete;
	
	@Autowired
	UserService userService;	
	
	public void login(String loggedUser) {		
		LoginRequest request = LoginRequest.builder()
				               .username(loggedUser)
				               .password(loggedUser)
				               .build();
	
		LoginResponse logedUser = restTamplete.postForObject("/login", request, LoginResponse.class);
		sessionID = logedUser.getSessionId();
		params.put("sessionID", sessionID);
	}	
	
	public void createTmpUser() {		
		Role userRole = Role.builder()
    			       .name("user")
    			       .build();
		
		User userRegistration  = User.builder()
				  				.username("marija.petrovic")
				  				.password("marija.petrovic")
				  				.address("Ivana Blagojevica 14")
				  				.name("Marija Petrovic")
				  				.email("marija.petrovic@mozzartbet.com")
				  				.role(userRole)
				  				.build();	
		
		RegistrationResponse userResponse = userService.registerUser(userRegistration);
		assertEquals(HttpStatus.OK, userResponse.getStatus().getHttpStatus());	
	}		
	
	//ulogovati se kao admin i procitati usera koji postoji u bazi
	@Test
	void testGetUser() {
		log.debug("TEST: testGetUser");
		login("bogdan.blazic");
		username = "olga.jesic";
		params.put("user", username);
		ResponseEntity<User> response = restTamplete.getForEntity(uri, User.class, params);
		assertEquals(HttpStatus.OK, response.getStatusCode());	
	}	
	
	//ulogovati se kao admin i dohvatiti usera koji ne postoji u bazi
	@Test
	void testGetInvalidUser() {
		log.debug("TEST: testGetInvalidUser");
		login("bogdan.blazic");
		username = "oolga.jesic";
		params.put("user", username);
		
		ResponseEntity<ErrorResponseDto> errorResponse = restTamplete.getForEntity(uri, ErrorResponseDto.class, params);
		assertEquals(HttpStatus.NOT_FOUND, errorResponse.getStatusCode());
	}	

	//ulogovati se kao admin i updejt usera koji postoji u bazi
	@Test
	void testUpdateUser() {
		log.debug("TEST: testUpdateUser");
		login("bogdan.blazic");
		username = "olga.jesic";
		params.put("user", username);	
		UserRequest request = UserRequest.builder()
				              .email("olga.jesic@gmail.com")
				              .build();		
		restTamplete.put(uri, request, params);
		ResponseEntity<User> response = restTamplete.getForEntity(uri, User.class, params);
		assertTrue(response.getBody().getEmail().equalsIgnoreCase("olga.jesic@gmail.com"));
	}
	
	//ulogovati se kao admin i updejt usera koji ne postoji u bazi
	@Test
	void testUpdateInvalidUser() {
		log.debug("TEST: testUpdateInvalidUser");
		login("bogdan.blazic");
		username = "oolga.jesic";
		params.put("user", username);	
		UserRequest request = UserRequest.builder()
				              .email("olga.jesic@mozzartbet.com")
				              .build();
		restTamplete.put(uri, request, params);
		ResponseEntity<ErrorResponseDto> errorResponse = restTamplete.getForEntity(uri, ErrorResponseDto.class, params);
		assertEquals(HttpStatus.NOT_FOUND, errorResponse.getStatusCode());
	}		

	//ulogovati se kao admin i brisanje usera koji postoji u bazi
	@Test
	void testDeleteUser() {
		log.debug("TEST: testDeleteUser");
		login("bogdan.blazic");		
		createTmpUser();
		username = "marija.petrovic";
		params.put("user", username);
		restTamplete.delete(uri, params);
		ResponseEntity<ErrorResponseDto> errorResponse = restTamplete.getForEntity(uri, ErrorResponseDto.class, params);
		assertEquals(HttpStatus.NOT_FOUND, errorResponse.getStatusCode());
	}
	
	@Test
	void testDeleteInvalidUser() {
		log.debug("TEST: testDeleteUser");
		login("bogdan.blazic");	
		username = "oolga.jesic";
		params.put("user", username);	
		restTamplete.delete(uri, params);
		ResponseEntity<ErrorResponseDto> errorResponse = restTamplete.getForEntity(uri, ErrorResponseDto.class, params);
		assertEquals(HttpStatus.NOT_FOUND, errorResponse.getStatusCode());
	}	
	
	//ulogovati se kao regular user i procitati drugog usera
	@Test
	void testGetUserLoggedRegular() {
		log.debug("TEST: testGetUserLoggedRegular");
		login("olga.jesic");		
		username = "bogdan.blazic";
		params.put("user", username);
		ResponseEntity<ErrorResponseDto> errorResponse = restTamplete.getForEntity(uri, ErrorResponseDto.class, params);
		assertEquals(HttpStatus.FORBIDDEN, errorResponse.getStatusCode());
	}	
	
	//ulogovati se kao regular user i procitati sebe
	@Test
	void testGetMySelfLoggedRegular() {
		log.debug("TEST: testGetMySelfLoggedRegular");
		login("olga.jesic");		
		username = "olga.jesic";
		params.put("user", username);
		ResponseEntity<User> response = restTamplete.getForEntity(uri, User.class, params);
		assertEquals(HttpStatus.OK, response.getStatusCode());		
	}	
	
	//ulogovati se kao regular user i updejt drugog usera
	@Test
	void testUpdateUserLoggedRegular() {
		log.debug("TEST: testUpdateUserLoggedRegular");
		login("olga.jesic");		
		username = "bogdan.blazic";
		params.put("user", username);	
		UserRequest request = UserRequest.builder()
				              .email("bogdan.blazic@gmail.com")
				              .build();		
		restTamplete.put(uri, request, params);
		ResponseEntity<ErrorResponseDto> errorResponse = restTamplete.getForEntity(uri, ErrorResponseDto.class, params);
		assertEquals(HttpStatus.FORBIDDEN, errorResponse.getStatusCode());
	}
	
	//ulogovati se kao regular user i updejt samog sebe
	@Test
	void testUpdateMySelfLoggedRegular() {
		log.debug("TEST: testUpdateUserLoggedRegular");
		login("olga.jesic");		
		username = "olga.jesic";
		params.put("user", username);	
		UserRequest request = UserRequest.builder()
				              .email("olga.jesic@gmail.com")
				              .build();
		restTamplete.put(uri, request, params);
		ResponseEntity<User> response = restTamplete.getForEntity(uri, User.class, params);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().getEmail().equalsIgnoreCase("olga.jesic@gmail.com"));
	}	
	
	//ulogovati se kao regular user i delete drugog usera
	@Test
	void testDeleteUserLoggedRegular() {
		log.debug("TEST: testDeleteUserLoggedRegular");
		login("olga.jesic");		
		username = "bogdan.blazic";
		params.put("user", username);		
		restTamplete.delete(uri, params);
		ResponseEntity<ErrorResponseDto> errorResponse = restTamplete.getForEntity(uri, ErrorResponseDto.class, params);
		assertEquals(HttpStatus.FORBIDDEN, errorResponse.getStatusCode());		
	}	
}
