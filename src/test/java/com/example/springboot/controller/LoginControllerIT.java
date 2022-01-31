package com.example.springboot.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
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

		LoginResponse logedUser = restTamplete.postForObject("/login", request, LoginResponse.class);
		
		assertNotNull(logedUser.getUsername());
	}
	
	@Test
	@Transactional
	void testLoginWrongUsername() {

		LoginRequest request = LoginRequest.builder()
				               .username("oolga.jesic")
				               .password("olga.jesic")
				               .build();
		
		ErrorResponseDto errorResponse = restTamplete.postForObject("/login", request, ErrorResponseDto.class);
		
		assertNotNull(errorResponse.getMessage());
	}
	
	@Test
	@Transactional
	void testLoginWrongPassword() {
		
		LoginRequest request = LoginRequest.builder()
				               .username("olga.jesic")
				               .password("oolga.jesic")
				               .build();
		
		ErrorResponseDto errorResponse = restTamplete.postForObject("/login", request, ErrorResponseDto.class);
		
		assertNotNull(errorResponse.getMessage());
	}	
	
	@Test
	@Transactional
	void testLoginNoUsername() {
		String message = "Username se mora uneti";
		LoginRequest request = LoginRequest.builder()
				               .username(null)
				               .password("olga.jesic")
				               .build();
		
		ErrorResponseDto errorResponse = restTamplete.postForObject("/login", request, ErrorResponseDto.class);
		
		assertNotNull(errorResponse.getMessage());
		assertTrue(errorResponse.getError().equalsIgnoreCase(message));
	}	
	
	@Test
	@Transactional
	void testLoginNoPassword() {
		String message = "Password se mora uneti";
		LoginRequest request = LoginRequest.builder()
				               .username("olga.jesic")
				               .password(null)
				               .build();
		
		ErrorResponseDto errorResponse = restTamplete.postForObject("/login", request, ErrorResponseDto.class);
		
		assertNotNull(errorResponse.getMessage());
		assertTrue(errorResponse.getError().equalsIgnoreCase(message));
		
	}	

}
