package com.example.springboot.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.example.springboot.domain.ErrorResponseDto;
import com.example.springboot.domain.RegistrationRequest;
import com.example.springboot.domain.RegistrationResponse;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class RegistrationControllerIT {
	
	@Autowired
	TestRestTemplate restTamplete; 	

	@Test
	@Transactional
	void testRegister() {

		RegistrationRequest request = RegistrationRequest.builder()
				                      .username("olga.jesic")
				                      .password("olga.jesic")
				                      .address("Ivana Blagojevica 14")
				                      .name("Olga Jesic")
				                      .email("olga.jesic@mozzartbet.com")
				                      .role("user")
				                      .build();
		
		ResponseEntity<RegistrationResponse> response = restTamplete.postForEntity("/register", new HttpEntity<>(request), RegistrationResponse.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());	
	}
	
	@Test
	@Transactional	
	void testRegisterInvalidUsername() {
		RegistrationRequest request = RegistrationRequest.builder()
				                      .username(null)
				                      .password("olga.jesic")
				                      .address("Ivana Blagojevica 14")
				                      .name("Olga Jesic")
				                      .email("olga.jesic@mozzartbet.com")
				                      .role("user")
				                      .build();
		
		ResponseEntity<ErrorResponseDto> errorResponse = restTamplete.postForEntity("/register", new HttpEntity<>(request), ErrorResponseDto.class);
		assertEquals(HttpStatus.BAD_REQUEST, errorResponse.getStatusCode());				
	}	
	
	@Test
	@Transactional	
	void testRegisterInvalidPassword() {
		RegistrationRequest request = RegistrationRequest.builder()
				                      .username("olga.jesic")
				                      .password(null)
				                      .address("Ivana Blagojevica 14")
				                      .name("Olga Jesic")
				                      .email("olga.jesic@mozzartbet.com")
				                      .role("user")
				                      .build();
		
		ResponseEntity<ErrorResponseDto> errorResponse = restTamplete.postForEntity("/register", new HttpEntity<>(request), ErrorResponseDto.class);
		assertEquals(HttpStatus.BAD_REQUEST, errorResponse.getStatusCode());
	}	
	
	@Test
	@Transactional	
	void testRegisterExistingUsername() {
		RegistrationRequest request = RegistrationRequest.builder()
				                      .username("olga.jesic")
				                      .password("olga.jesic")
				                      .address("Ivana Blagojevica 14")
				                      .name("Olga Jesic")
				                      .email("olga.jesic@mozzartbet.com")
				                      .role("user")
				                      .build();
		
		ResponseEntity<ErrorResponseDto> errorResponse = restTamplete.postForEntity("/register", new HttpEntity<>(request), ErrorResponseDto.class);
		assertEquals(HttpStatus.CONFLICT, errorResponse.getStatusCode());
	}	
	
	@Test
	@Transactional	
	void testRegisterExistingEmail() {
		RegistrationRequest request = RegistrationRequest.builder()
				                      .username("milos.milosevic")
				                      .password("milos.milosevic")
				                      .address("Ivana Blagojevica 14")
				                      .name("Olga Jesic")
				                      .email("olga.jesic@mozzartbet.com")
				                      .role("user")
				                      .build();
		ResponseEntity<ErrorResponseDto> errorResponse = restTamplete.postForEntity("/register", new HttpEntity<>(request), ErrorResponseDto.class);
		assertEquals(HttpStatus.CONFLICT, errorResponse.getStatusCode());
	}	

}
