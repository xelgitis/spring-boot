package com.example.springboot.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class VacationControllerIT {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private String sessionID;
	private String uri;
	private String username = "marija.petrovic"; //change the username to test CRUD of vacations for different users
	
	@Autowired
	TestRestTemplate restTamplete;
	
	Calendar c1 = Calendar.getInstance();
	
	//try all testcases logged as admin and logged as regular user	
	@BeforeEach
	public void initialLogin(){
		LoginRequest request = LoginRequest.builder().username("marija.petrovic").password("marija.petrovic").build();
		LoginResponse logedUser = restTamplete.postForObject("/login", request, LoginResponse.class);
		sessionID = logedUser.getSessionId();	
		uri = "/vacation/"+ sessionID + "?user=" + username;
	}	

	@Test
	void testCreateVacation() {		
		VacationRequest request = new VacationRequest();
		c1.set(Calendar.YEAR,  2022);
		c1.set(Calendar.MONTH, 7); //month numbers start for zero
		c1.set(Calendar.DATE,  30);
		request.setStartDate(c1.getTime());
		request.setDuration(19);
	
		VacationResponse newVacation = restTamplete.postForObject(uri, request, VacationResponse.class);
		assertNotNull(newVacation);
	}		
	
	@Test
	void testGetVacation() {
		Vacation vacation = restTamplete.getForObject(uri, Vacation.class);
		logger.info("Informacije o trazenom odmoru: {} ", vacation.toString());
		assertNotNull(vacation);	
	}

	@Test
	void testUpdateVacation() {
		Vacation vacation = restTamplete.getForObject(uri, Vacation.class);
		vacation.setDuration(15);		
		restTamplete.put(uri, vacation);
		assertNotNull(vacation);	
	}

	@Test
	void testDeleteUser() {
		restTamplete.delete(uri);
	}
	
	//invalid test scenarios - change the username to the one which does not exist in the DB	
	@Test
	void testCreateInvalidUser() {		
		VacationRequest request = new VacationRequest();
		c1.set(Calendar.YEAR,  2022);
		c1.set(Calendar.MONTH, 7); //month numbers start for zero
		c1.set(Calendar.DATE,  30);
		request.setStartDate(c1.getTime());
		request.setDuration(19);
	
		VacationResponse newVacation = restTamplete.postForObject(uri, request, VacationResponse.class);
		assertNull(newVacation);
	}
	
	@Test
	@Transactional
	void testGetInvalidUser() {
		Vacation vacation = restTamplete.getForObject(uri, Vacation.class);
		assertNull(vacation);	
	}
	
	@Test
	@Transactional
	void testUpdateInvalidUser() {
		Vacation vacation = restTamplete.getForObject(uri, Vacation.class);		
		//user.setAddress("Luja Adamica 17");		
		restTamplete.put(uri, vacation);
		assertNull(vacation);
	}
}
