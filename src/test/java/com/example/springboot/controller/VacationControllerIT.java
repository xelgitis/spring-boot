package com.example.springboot.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;
import org.junit.jupiter.api.BeforeEach;
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
import com.example.springboot.domain.RegistrationRequest;
import com.example.springboot.domain.RegistrationResponse;
import com.example.springboot.domain.Vacation;
import com.example.springboot.domain.VacationRequest;
import com.example.springboot.domain.VacationResponse;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class VacationControllerIT {
	
	Calendar c1 = Calendar.getInstance();
	
	private String sessionID;
	private String uri;
	private String uriUpdate;
	private Long id;
	private String loggedUser = "bogdan.blazic"; //change the loggedUser between admin and regular user
	private String username;  
	
	@Autowired
	TestRestTemplate restTamplete;
	
	//try all testcases logged as admin and logged as regular user	
	@BeforeEach
	public void initialSetUp(){
		//kreiranje administratora i regular usera	
		RegistrationRequest request1 = RegistrationRequest.builder()
                					   .username("bogdan.blazic")
                                       .password("bogdan.blazic")
                                       .address("Ivana Blagojevica 14")
                                       .name("Bogdan Blazic")
                                       .email("bogdan.blazic@mozzartbet.com")
                                       .role("administrator")
                                       .build();

       ResponseEntity<RegistrationResponse> response1 = restTamplete.postForEntity("/register", new HttpEntity<>(request1), RegistrationResponse.class);
       assertEquals(HttpStatus.CREATED, response1.getStatusCode());			
		
	   RegistrationRequest request2 = RegistrationRequest.builder()
					                  .username("olga.jesic")
					                  .password("olga.jesic")
					                  .address("Ivana Blagojevica 14")
					                  .name("Olga Jesic")
					                  .email("olga.jesic@mozzartbet.com")
					                  .role("user")
					                  .build();
			
	    ResponseEntity<RegistrationResponse> response2 = restTamplete.postForEntity("/register", new HttpEntity<>(request2), RegistrationResponse.class);
		assertEquals(HttpStatus.CREATED, response2.getStatusCode());						
		
		//logovanje
		LoginRequest request = LoginRequest.builder()
				               .username(loggedUser)
				               .password(loggedUser)
				               .build();
	
		LoginResponse logedUser = restTamplete.postForObject("/login", request, LoginResponse.class);
		sessionID = logedUser.getSessionId();	
	}	
	
	public void initializeVacations(String username) {
		uri = "/vacation/"+ sessionID + "?user=" + username;
		
		c1.set(Calendar.YEAR,  2022);
		c1.set(Calendar.MONTH, 7); 
		c1.set(Calendar.DATE,  30);		
		VacationRequest request1 = VacationRequest.builder()
				                  .startDate(c1.getTime())
				                  .duration(15)
				                  .approval("N")
				                  .build();
	
		ResponseEntity<VacationResponse> response1 = restTamplete.postForEntity(uri, new HttpEntity<>(request1), VacationResponse.class);
		assertEquals(HttpStatus.CREATED, response1.getStatusCode());	
		
		c1.set(Calendar.YEAR,  2022);
		c1.set(Calendar.MONTH, 9); 
		c1.set(Calendar.DATE,  15);		
		VacationRequest request2 = VacationRequest.builder()
				                  .startDate(c1.getTime())
				                  .duration(13)
				                  .approval("N")
				                  .build();
	
		ResponseEntity<VacationResponse> response2 = restTamplete.postForEntity(uri, new HttpEntity<>(request2), VacationResponse.class);
		assertEquals(HttpStatus.CREATED, response2.getStatusCode());			
	}

	//ulogovati se kao admin i kreirati odmor sebi
	@Test
	@Transactional
	void testCreateVacation() {
		username = "bogdan.blazic";
		uri = "/vacation/"+ sessionID + "?user=" + username;		
		c1.set(Calendar.YEAR,  2022);
		c1.set(Calendar.MONTH, 7); 
		c1.set(Calendar.DATE,  30);		
		VacationRequest request = VacationRequest.builder()
				                  .startDate(c1.getTime())
				                  .duration(15)
				                  .approval("N")
				                  .build();
	
		ResponseEntity<VacationResponse> response = restTamplete.postForEntity(uri, new HttpEntity<>(request), VacationResponse.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());	
	}	
	
	//ulogovati se kao admin i kreirati odmor nekom drugom
	@Test
	@Transactional
	void testCreateVacationOtherUser() {
		username = "olga.jesic";
		uri = "/vacation/"+ sessionID + "?user=" + username;		
		c1.set(Calendar.YEAR,  2022);
		c1.set(Calendar.MONTH, 7); 
		c1.set(Calendar.DATE,  30);		
		VacationRequest request = VacationRequest.builder()
				                  .startDate(c1.getTime())
				                  .duration(15)
				                  .approval("N")
				                  .build();
	
		ResponseEntity<VacationResponse> response = restTamplete.postForEntity(uri, new HttpEntity<>(request), VacationResponse.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());	
	}	
	
	//ulogovati se kao admin i kreirati odmor nepostojecem korisniku
	@Test
	@Transactional
	void testCreateVacationInvalidUser() {
		username = "oolga.jesic";
		uri = "/vacation/"+ sessionID + "?user=" + username;		
		c1.set(Calendar.YEAR,  2022);
		c1.set(Calendar.MONTH, 7); 
		c1.set(Calendar.DATE,  30);		
		VacationRequest request = VacationRequest.builder()
				                  .startDate(c1.getTime())
				                  .duration(15)
				                  .approval("N")
				                  .build();
	
		ResponseEntity<VacationResponse> response = restTamplete.postForEntity(uri, new HttpEntity<>(request), VacationResponse.class);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());	
	}		

	//ulogovati se kao admin i procitati svoj vacation
	@Test
	@Transactional
	void testGetVacation() {
		//prvo kreirati sebi 2 odmora
		username = "bogdan.blazic";
		initializeVacations(username);
		ResponseEntity<Vacation[]> response = restTamplete.getForEntity(uri, Vacation[].class);
		Vacation[] vacations = response.getBody();
		assertNotNull(vacations);	
	}
	
	//ulogovati se kao admin i procitati odmor drugog usera
	@Test
	@Transactional
	void testGetVacationOtherUser() {
		//prvo kreirati useru 2 odmora
		username = "olga.jesic";
		initializeVacations(username);	
		ResponseEntity<Vacation[]> response = restTamplete.getForEntity(uri, Vacation[].class);
		Vacation[] vacations = response.getBody();
		assertNotNull(vacations);	
	}	

	//ulogovati se kao admin i procitati odmor nepostojeceg usera
	@Test
	@Transactional
	void testGetVacationInvalidUser() {
		//prvo kreirati useru 2 odmora
		username = "olga.jesic";
		initializeVacations(username);
		
		//invalid username
		username = "oolga.jesic";
		uri = "/vacation/"+ sessionID + "?user=" + username;		
		
		ResponseEntity<ErrorResponseDto> response = restTamplete.getForEntity(uri, ErrorResponseDto.class);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}		
	
	@Test
	@Transactional
	void testUpdateVacation() {
		//prvo kreirati useru 2 odmora
		username = "bogdan.blazic";
		initializeVacations(username);
		
		ResponseEntity<Vacation[]> response = restTamplete.getForEntity(uri, Vacation[].class);
		Vacation[] vacations = response.getBody();		
		id = vacations[0].getId(); //uzeti prvi odmor za updejt
		
		VacationRequest vacationRequest = VacationRequest.builder()
                                         .duration(22)
                                         .build();
		uriUpdate = "/vacation/"+ sessionID + "/" + id + "/" + "?user=" + username;
		System.out.println("URI for update: " + uriUpdate.toString());
		restTamplete.put(uriUpdate, vacationRequest);
		/*ResponseEntity<Vacation[]> response = restTamplete.getForEntity(uri, Vacation[].class);
		Vacation[] vacations = response.getBody();
		Vacation vacation = null;
		for (int i = 0; i < vacations.length; i++) {
			if (vacations[i].getId() == id) {
				vacation = vacations[i];
				break;
			}
		}
		assertTrue(vacation.getDuration() == 22);	*/
	}
	
	@Test
	@Transactional
	void testUpdateVacationOtherUser() {
		//prvo kreirati useru 2 odmora
		username = "olga.jesic";
		initializeVacations(username);
		
		ResponseEntity<Vacation[]> response = restTamplete.getForEntity(uri, Vacation[].class);
		Vacation[] vacations = response.getBody();		
		id = vacations[0].getId(); //uzeti prvi odmor za updejt
		
		VacationRequest vacationRequest = VacationRequest.builder()
                                         .duration(22)
                                         .build();
		uriUpdate = "/vacation/"+ sessionID + "/" + id + "/" + "?user=" + username;
		System.out.println("URI for update: " + uriUpdate.toString());
		restTamplete.put(uriUpdate, vacationRequest);
		/*ResponseEntity<Vacation[]> response = restTamplete.getForEntity(uri, Vacation[].class);
		Vacation[] vacations = response.getBody();
		Vacation vacation = null;
		for (int i = 0; i < vacations.length; i++) {
			if (vacations[i].getId() == id) {
				vacation = vacations[i];
				break;
			}
		}
		assertTrue(vacation.getDuration() == 22);	*/
	}	
	
	@Test
	@Transactional
	void testUpdateVacationInvalidUser() {
		//prvo kreirati useru 2 odmora
		username = "olga.jesic";
		initializeVacations(username);
		
		ResponseEntity<Vacation[]> response = restTamplete.getForEntity(uri, Vacation[].class);
		Vacation[] vacations = response.getBody();		
		id = vacations[0].getId(); //uzeti prvi odmor za updejt
		
		VacationRequest vacationRequest = VacationRequest.builder()
                                         .duration(22)
                                         .build();
		username = "oolga.jesic";
		uriUpdate = "/vacation/"+ sessionID + "/" + id + "/" + "?user=" + username;
		System.out.println("URI for update: " + uriUpdate.toString());
		restTamplete.put(uriUpdate, vacationRequest);
		/*ResponseEntity<Vacation[]> response = restTamplete.getForEntity(uri, Vacation[].class);
		Vacation[] vacations = response.getBody();
		Vacation vacation = null;
		for (int i = 0; i < vacations.length; i++) {
			if (vacations[i].getId() == id) {
				vacation = vacations[i];
				break;
			}
		}
		assertTrue(vacation.getDuration() == 22);	*/
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
	void testCreateVacationInvalidUserLoggedRegular() {
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
	void testGetVacationInvalidUserLoggedRegular() {
		String message = "korisnik sa ovim username-om ne postoji u bazi";
		ErrorResponseDto errorResponse = restTamplete.getForObject(uri, ErrorResponseDto.class);
		assertTrue(errorResponse.getMessage().equalsIgnoreCase(message));
	}
	
	@Test
	@Transactional
	void testUpdateVacationInvalidUserLoggedRegular() {
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
