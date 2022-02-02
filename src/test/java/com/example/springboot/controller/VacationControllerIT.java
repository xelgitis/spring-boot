package com.example.springboot.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.example.springboot.domain.LoginRequest;
import com.example.springboot.domain.LoginResponse;
import com.example.springboot.domain.Vacation;
import com.example.springboot.domain.VacationRequest;
import com.example.springboot.domain.VacationResponse;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Slf4j
class VacationControllerIT {
	
	private String sessionID;
	private String uri;
	private String username   = "oolga.jesic"; //change the username to test CRUD of vacations for different users
	
	@Autowired
	TestRestTemplate restTamplete;
	
	Calendar c1 = Calendar.getInstance();
	
	//try all testcases logged as admin and logged as regular user	
	@BeforeEach
	public void initialLogin(){
		LoginRequest request = LoginRequest.builder()
				               .username("bogdan.blazic")
				               .password("bogdan.blazic")
				               .build();
		LoginResponse logedUser = restTamplete.postForObject("/login", request, LoginResponse.class);
		sessionID = logedUser.getSessionId();	
		uri = "/vacation/"+ sessionID + "?user=" + username;
	}	

	@Test
	void testCreateVacation() {
		c1.set(Calendar.YEAR,  2022);
		c1.set(Calendar.MONTH, 7); //month numbers start from zero
		c1.set(Calendar.DATE,  30);		
		VacationRequest request = VacationRequest.builder()
				                  .startDate(c1.getTime())
				                  .duration(19)
				                  .build();
	
		VacationResponse newVacation = restTamplete.postForObject(uri, request, VacationResponse.class);
		assertNotNull(newVacation);
	}		
	
	@Test
	void testGetVacation() {
		Vacation vacation = restTamplete.getForObject(uri, Vacation.class);
		log.info("Informacije o trazenom odmoru: {} ", vacation.toString());
		assertNotNull(vacation);	
	}

	@Test
	void testUpdateVacation() {
		Vacation vacation = restTamplete.getForObject(uri, Vacation.class);
		vacation.setDuration(15);		
		restTamplete.put(uri, vacation);
		vacation = restTamplete.getForObject(uri, Vacation.class);
		assertTrue(vacation.getDuration() == 15);	
	}

	@Test
	void testDeleteVacation() {
		restTamplete.delete(uri);
		Vacation vacation = restTamplete.getForObject(uri, Vacation.class);
		assertNull(vacation.getId());
	}
	
	//invalid test scenarios - change the username to the one which does not exist in the DB	
	//or make login of regular user and username to be somebody else
	@Test
	void testCreateVacationInvalidUser() {
		String message = "korisnik sa ovim username-om ne postoji u bazi";
		c1.set(Calendar.YEAR,  2022);
		c1.set(Calendar.MONTH, 7); //month numbers start for zero
		c1.set(Calendar.DATE,  30);
		VacationRequest request = VacationRequest.builder()
                				  .startDate(c1.getTime())
                                  .duration(19)
                                  .build();
	
		VacationResponse newVacation = restTamplete.postForObject(uri, request, VacationResponse.class);
		assertTrue(newVacation.getMessage().equalsIgnoreCase(message));
	}
	
	@Test
	@Transactional
	void testGetVacationInvalidUser() {
		Vacation vacation = restTamplete.getForObject(uri, Vacation.class);
		assertNull(vacation.getId());
	}
	
	@Test
	@Transactional
	void testUpdateVacationInvalidUser() {
		VacationRequest request = VacationRequest.builder()
                                 .duration(33)
                                 .build();	
		restTamplete.put(uri, request);
		Vacation vacation = restTamplete.getForObject(uri, Vacation.class);
		assertNull(vacation.getId());
	}
	
	@Test
	@Transactional
	void testDeleteVacationInvalidUser() {
		restTamplete.delete(uri);
		Vacation vacation = restTamplete.getForObject(uri, Vacation.class);
		assertNull(vacation.getId());
	}	
}
