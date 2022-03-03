package com.example.springboot.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.springboot.domain.ErrorResponseDto;
import com.example.springboot.domain.LoginRequest;
import com.example.springboot.domain.LoginResponse;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Slf4j
class LoginControllerIT {
	
	@Autowired
	TestRestTemplate restTamplete;

	@Test
	void testLoginCheck() {
		log.debug("TEST: testLoginCheck");
		
		LoginRequest request = LoginRequest.builder()
				               .username("olga.jesic")
				               .password("olga.jesic")
				               .build();

		ResponseEntity<LoginResponse> response = restTamplete.postForEntity("/login", new HttpEntity<>(request), LoginResponse.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	void testLoginWrongUsername() {
		log.debug("TEST: testLoginWrongUsername");		
		LoginRequest request = LoginRequest.builder()
				               .username("oolga.jesic")
				               .password("olga.jesic")
				               .build();
		
		ResponseEntity<ErrorResponseDto> errorResponse = restTamplete.postForEntity("/login", new HttpEntity<>(request), ErrorResponseDto.class);
		assertEquals(HttpStatus.NOT_FOUND, errorResponse.getStatusCode());		
	}
	
	//TODO: ne radi!
	@Test
	void testLoginWrongPassword() {	
		log.debug("TEST: testLoginWrongPassword");	
		LoginRequest request = LoginRequest.builder()
				               .username("olga.jesic")
				               .password("oolga.jesic")
				               .build();
		
		ResponseEntity<ErrorResponseDto> errorResponse = restTamplete.postForEntity("/login", new HttpEntity<>(request), ErrorResponseDto.class);
		assertEquals(HttpStatus.UNAUTHORIZED, errorResponse.getStatusCode());
	}	
	
	@Test
	void testLoginNoUsername() {
		log.debug("TEST: testLoginNoUsername");
		LoginRequest request = LoginRequest.builder()
				               .username(null)
				               .password("olga.jesic")
				               .build();
		ResponseEntity<ErrorResponseDto> errorResponse = restTamplete.postForEntity("/login", new HttpEntity<>(request), ErrorResponseDto.class);
		assertEquals(HttpStatus.BAD_REQUEST, errorResponse.getStatusCode());
	}	
	
	@Test
	void testLoginNoPassword() {
		log.debug("TEST: testLoginNoPassword");
		LoginRequest request = LoginRequest.builder()
				               .username("olga.jesic")
				               .password(null)
				               .build();
		
		ResponseEntity<ErrorResponseDto> errorResponse = restTamplete.postForEntity("/login", new HttpEntity<>(request), ErrorResponseDto.class);		
		assertEquals(HttpStatus.BAD_REQUEST, errorResponse.getStatusCode());		
	}	

}
