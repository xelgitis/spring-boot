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
import org.springframework.transaction.annotation.Transactional;

import com.example.springboot.domain.ErrorResponseDto;
import com.example.springboot.domain.LoginRequest;
import com.example.springboot.domain.LoginResponse;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class LoginControllerIT {
	
	@Autowired
	TestRestTemplate restTamplete;	

	@Test
	@Transactional
	void testLoginCheck() {
		
		LoginRequest request = LoginRequest.builder()
				               .username("olga.jesic")
				               .password("olga.jesic")
				               .build();

		ResponseEntity<LoginResponse> response = restTamplete.postForEntity("/login", new HttpEntity<>(request), LoginResponse.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	@Transactional
	void testLoginWrongUsername() {
		LoginRequest request = LoginRequest.builder()
				               .username("oolga.jesic")
				               .password("olga.jesic")
				               .build();
		
		ResponseEntity<ErrorResponseDto> errorResponse = restTamplete.postForEntity("/login", new HttpEntity<>(request), ErrorResponseDto.class);
		assertEquals(HttpStatus.NOT_FOUND, errorResponse.getStatusCode());		
	}
	
	//TODO: ne radi!
	@Test
	@Transactional
	void testLoginWrongPassword() {		
		LoginRequest request = LoginRequest.builder()
				               .username("olga.jesic")
				               .password("oolga.jesic")
				               .build();
		
		ResponseEntity<ErrorResponseDto> errorResponse = restTamplete.postForEntity("/login", new HttpEntity<>(request), ErrorResponseDto.class);
		assertEquals(HttpStatus.UNAUTHORIZED, errorResponse.getStatusCode());
	}	
	
	@Test
	@Transactional
	void testLoginNoUsername() {
		LoginRequest request = LoginRequest.builder()
				               .username(null)
				               .password("olga.jesic")
				               .build();
		ResponseEntity<ErrorResponseDto> errorResponse = restTamplete.postForEntity("/login", new HttpEntity<>(request), ErrorResponseDto.class);
		assertEquals(HttpStatus.BAD_REQUEST, errorResponse.getStatusCode());
	}	
	
	@Test
	@Transactional
	void testLoginNoPassword() {
		LoginRequest request = LoginRequest.builder()
				               .username("olga.jesic")
				               .password(null)
				               .build();
		
		ResponseEntity<ErrorResponseDto> errorResponse = restTamplete.postForEntity("/login", new HttpEntity<>(request), ErrorResponseDto.class);		
		assertEquals(HttpStatus.BAD_REQUEST, errorResponse.getStatusCode());		
	}	

}
