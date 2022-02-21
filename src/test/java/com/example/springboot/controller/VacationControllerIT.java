package com.example.springboot.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import com.example.springboot.domain.ErrorResponseDto;
import com.example.springboot.domain.LoginRequest;
import com.example.springboot.domain.LoginResponse;
import com.example.springboot.domain.Vacation;
import com.example.springboot.domain.VacationRequest;
import com.example.springboot.domain.VacationResponse;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class VacationControllerIT {
	
	private String sessionID;
	private String uri;
	private String uriUpdate;
	private Long   id = 88l;
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
		System.out.println("URI: " + uri.toString());
	}	

	@Test
	void testCreateVacation() {
		c1.set(Calendar.YEAR,  2022);
		c1.set(Calendar.MONTH, 7); //month numbers start from zero
		c1.set(Calendar.DATE,  30);		
		VacationRequest request = VacationRequest.builder()
				                  .startDate(c1.getTime())
				                  .duration(21)
				                  .approval("N")
				                  .build();
	
		VacationResponse newVacation = restTamplete.postForObject(uri, request, VacationResponse.class);
		assertNotNull(newVacation);
	}	

	
	@Test
	void testGetVacation() {
		ResponseEntity<Vacation[]> response = restTamplete.getForEntity(uri, Vacation[].class);
		Vacation[] vacations = response.getBody();
		assertNotNull(vacations);	
	}

	@Test
	void testUpdateVacation() {
		VacationRequest vacationRequest = VacationRequest.builder()
                                         .duration(22)
                                         .build();
		uriUpdate = "/vacation/"+ sessionID + "/" + id + "/" + "?user=" + username;
		//uriUpdate = "/vacation/"+ sessionID + "/" + id + "?user=" + username;
		System.out.println("URI for update: " + uriUpdate.toString());
		restTamplete.put(uriUpdate, vacationRequest);
		ResponseEntity<Vacation[]> response = restTamplete.getForEntity(uri, Vacation[].class);
		Vacation[] vacations = response.getBody();
		Vacation vacation = null;
		for (int i = 0; i < vacations.length; i++) {
			if (vacations[i].getId() == id) {
				vacation = vacations[i];
				break;
			}
		}
		assertTrue(vacation.getDuration() == 22);	
	}

	@Test
	void testDeleteVacation() {
		restTamplete.delete(uri);
		ResponseEntity<Vacation[]> response = restTamplete.getForEntity(uri, Vacation[].class);
		Vacation[] vacations = response.getBody();
		assertNull(vacations);
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
		String message = "korisnik sa ovim username-om ne postoji u bazi";
		ErrorResponseDto errorResponse = restTamplete.getForObject(uri, ErrorResponseDto.class);
		assertTrue(errorResponse.getMessage().equalsIgnoreCase(message));
	}
	
	@Test
	@Transactional
	void testUpdateVacationInvalidUser() {
		String message = "korisnik sa ovim username-om ne postoji u bazi";
		VacationRequest request = VacationRequest.builder()
                                 .duration(33)
                                 .build();
		uriUpdate = "/vacation/"+ sessionID + "/" + id + "/" + "?user=" + username;
		System.out.println("URI for update: " + uriUpdate.toString());
		restTamplete.put(uriUpdate, request);
		uri = "/vacation/"+ sessionID + "?user=" + username;		
		ErrorResponseDto errorResponse = restTamplete.getForObject(uri, ErrorResponseDto.class);
		assertTrue(errorResponse.getMessage().equalsIgnoreCase(message));
	}
	
	@Test
	@Transactional
	void testDeleteVacationInvalidUser() {
		restTamplete.delete(uri);
	}	
}
