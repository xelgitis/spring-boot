package com.example.springboot.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.example.springboot.domain.RegistrationRequest;
import com.example.springboot.domain.RegistrationResponse;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class RegistrationControllerIT {
	
	@Autowired
	TestRestTemplate restTamplete; 	

	@Test
	@Transactional
	void testRegister() {
		RegistrationRequest request = new RegistrationRequest();
		request.setUsername("olga.jesic");
		request.setPassword("olga.jesic");
		request.setAddress("Ivana Blagojevica 14");
		request.setName("Olga Jesic");
		request.setEmail("olga.jesic@mozzartbet.com");
		request.setRole("user");
		
		RegistrationResponse newUser = restTamplete.postForObject("/register", request, RegistrationResponse.class);
		
		assertNotNull(newUser.getUsername());	
	}
	
	@Test
	@Transactional	
	void testRegisterInvalidUsername() {
		
		RegistrationRequest request = new RegistrationRequest();
		request.setPassword("olga.jesic");
		request.setAddress("Ivana Blagojevica 14");
		request.setName("Olga Jesic");
		request.setEmail("olga.jesic@mozzartbet.com");
		request.setRole("user");
		
		RegistrationResponse newUser = restTamplete.postForObject("/register", request, RegistrationResponse.class);
		
		assertNull(newUser.getUsername());	
	}	
	
	@Test
	@Transactional	
	void testRegisterInvalidPassword() {
		
		RegistrationRequest request = new RegistrationRequest();
		request.setUsername("olga.jesic");
		request.setAddress("Ivana Blagojevica 14");
		request.setName("Olga Jesic");
		request.setEmail("olga.jesic@mozzartbet.com");
		request.setRole("user");
		
		RegistrationResponse newUser = restTamplete.postForObject("/register", request, RegistrationResponse.class);
		
		assertNull(newUser.getUsername());	
	}	
	
	@Test
	@Transactional	
	void testRegisterExistingUsername() {
		
		RegistrationRequest request = new RegistrationRequest();
		request.setUsername("olga.jesic");
		request.setPassword("olga.jesic");
		request.setAddress("Ivana Blagojevica 14");
		request.setName("Olga Jesic");
		request.setEmail("olga.jesic@mozzartbet.com");
		request.setRole("user");
		
		RegistrationResponse newUser = restTamplete.postForObject("/register", request, RegistrationResponse.class);
		
		assertNull(newUser.getUsername());	
	}	
	
	@Test
	@Transactional	
	void testRegisterExistingEmail() {
		
		RegistrationRequest request = new RegistrationRequest();
		request.setUsername("olga.jesic");
		request.setPassword("olga.jesic");
		request.setAddress("Ivana Blagojevica 14");
		request.setName("Olga Jesic");
		request.setEmail("olga.jesic@mozzartbet.com");
		request.setRole("user");
		
		RegistrationResponse newUser = restTamplete.postForObject("/register", request, RegistrationResponse.class);
		
		assertNull(newUser.getUsername());	
	}	

}
