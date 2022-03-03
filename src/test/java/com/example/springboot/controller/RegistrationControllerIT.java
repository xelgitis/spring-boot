package com.example.springboot.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.example.springboot.domain.ErrorResponseDto;
import com.example.springboot.domain.RegistrationRequest;
import com.example.springboot.domain.RegistrationResponse;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Slf4j
class RegistrationControllerIT {
	
	@Autowired
	TestRestTemplate restTamplete; 	

	@Test
	void testRegister() {
		log.debug("TEST: testRegister");
		RegistrationRequest request = RegistrationRequest.builder()
				                      .username("marija.markovic")
				                      .password("marija.markovic")
				                      .address("Ivana Blagojevica 14")
				                      .name("Marija Markovic")
				                      .email("marija.markovic@mozzartbet.com")
				                      .role("user")
				                      .build();
		
		ResponseEntity<RegistrationResponse> response = restTamplete.postForEntity("/register", new HttpEntity<>(request), RegistrationResponse.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());	
	}
	
	@Test
	void testRegisterInvalidUsername() {
		log.debug("TEST: testRegisterInvalidUsername");
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
	void testRegisterInvalidPassword() {
		log.debug("TEST: testRegisterInvalidPassword");
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
	void testRegisterExistingUsername() {
		log.debug("TEST: testRegisterExistingUsername");
		RegistrationRequest newRequest = RegistrationRequest.builder()
				                         .username("olga.jesic")
				                         .password("olga.jesic")
				                         .address("Ivana Blagojevica 14")
				                         .name("Olga Jesic")
				                         .email("olga.jesic@mozzartbet.com")
				                         .role("user")
				                         .build();
		
		ResponseEntity<ErrorResponseDto> errorNewResponse = restTamplete.postForEntity("/register", new HttpEntity<>(newRequest), ErrorResponseDto.class);
		assertEquals(HttpStatus.CONFLICT, errorNewResponse.getStatusCode());
	}	
	
	@Test	
	void testRegisterExistingEmail() {
		log.debug("TEST: testRegisterExistingEmail");
		//preduslov: vec postoji user sa istim email-om u bazi		
		RegistrationRequest newRequest = RegistrationRequest.builder()
				                         .username("milos.milosevic")
				                         .password("milos.milosevic")
				                         .address("Ivana Blagojevica 14")
				                         .name("Olga Jesic")
				                         .email("olga.jesic@mozzartbet.com")
				                         .role("user")
				                         .build();
		
		ResponseEntity<ErrorResponseDto> errorNewResponse = restTamplete.postForEntity("/register", new HttpEntity<>(newRequest), ErrorResponseDto.class);
		assertEquals(HttpStatus.CONFLICT, errorNewResponse.getStatusCode());
	}	

}
