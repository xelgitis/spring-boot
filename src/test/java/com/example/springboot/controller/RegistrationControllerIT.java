package com.example.springboot.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
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

		RegistrationRequest request = RegistrationRequest.builder().username("olga.jesic").password("olga.jesic").address("Ivana Blagojevica 14").name("Olga Jesic").email("olga.jesic@mozzartbet.com").role("user").build();
		
		RegistrationResponse regularResponse = restTamplete.postForObject("/register", request, RegistrationResponse.class);
		
		assertNotNull(regularResponse.getUsername());	
	}
	
	@Test
	@Transactional	
	void testRegisterInvalidUsername() {
		String message = "username nije unet";
		
		RegistrationRequest request = RegistrationRequest.builder().username(null).password("olga.jesic").address("Ivana Blagojevica 14").name("Olga Jesic").email("olga.jesic@mozzartbet.com").role("user").build();
		
		ErrorResponseDto errorResponse = restTamplete.postForObject("/register", request, ErrorResponseDto.class);
		
		assertNotNull(errorResponse.getError());
		assertTrue(errorResponse.getError().equalsIgnoreCase(message));
			
	}	
	
	@Test
	@Transactional	
	void testRegisterInvalidPassword() {
		String message = "password nije unet";
		
		RegistrationRequest request = RegistrationRequest.builder().username("olga.jesic").password(null).address("Ivana Blagojevica 14").name("Olga Jesic").email("olga.jesic@mozzartbet.com").role("user").build();
		
		ErrorResponseDto errorResponse = restTamplete.postForObject("/register", request, ErrorResponseDto.class);
		
		assertNotNull(errorResponse.getError());
		assertTrue(errorResponse.getError().equalsIgnoreCase(message));
	}	
	
	@Test
	@Transactional	
	void testRegisterExistingUsername() {
		String message = "korisnicko ime vec postoji u bazi";
		
		RegistrationRequest request = RegistrationRequest.builder().username("olga.jesic").password("olga.jesic").address("Ivana Blagojevica 14").name("Olga Jesic").email("olga.jesic@mozzartbet.com").role("user").build();
		
		ErrorResponseDto errorResponse = restTamplete.postForObject("/register", request, ErrorResponseDto.class);
		
		assertNotNull(errorResponse.getStatus());
		assertTrue(errorResponse.getMessage().equalsIgnoreCase(message));
	}	
	
	@Test
	@Transactional	
	void testRegisterExistingEmail() {
		String message = "korisnik sa ovim email-om vec postoji u bazi";
		RegistrationRequest request = RegistrationRequest.builder().username("milos.milosevic").password("milos.milosevic").address("Ivana Blagojevica 14").name("Olga Jesic").email("olga.jesic@mozzartbet.com").role("user").build();
		
		ErrorResponseDto errorResponse = restTamplete.postForObject("/register", request, ErrorResponseDto.class);
		
		assertNotNull(errorResponse.getStatus());	
		assertTrue(errorResponse.getMessage().equalsIgnoreCase(message));
	}	

}
