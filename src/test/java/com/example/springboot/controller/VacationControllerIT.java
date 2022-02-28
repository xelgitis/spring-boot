package com.example.springboot.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;

import org.junit.jupiter.api.BeforeAll;
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
	//private static String loggedUser, loggedUsr; //change the loggedUser between admin and regular user
	private String username;  
	
	@Autowired
	TestRestTemplate restTamplete;
	
/*	@BeforeAll
	public static void loggedUserSetUp(){
		System.out.println("Passed parameter: " + loggedUsr);
		loggedUser = loggedUsr;	
		System.out.println("Final parameter: " + loggedUser);
	} */

	
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
	}	
	
	public void login(String loggedUser) {		
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
		login("bogdan.blazic");
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
		login("bogdan.blazic");
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
		login("bogdan.blazic");
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
		login("bogdan.blazic");
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
		login("bogdan.blazic");
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
		login("bogdan.blazic");
		//prvo kreirati useru 2 odmora
		username = "olga.jesic";
		initializeVacations(username);
		
		//invalid username
		username = "oolga.jesic";
		uri = "/vacation/"+ sessionID + "?user=" + username;		
		
		ResponseEntity<ErrorResponseDto> response = restTamplete.getForEntity(uri, ErrorResponseDto.class);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}		
	
	//ulogovati se kao admin i uraditi updejt svog odmora
	@Test
	@Transactional
	void testUpdateVacation() {
		login("bogdan.blazic");
		//prvo kreirati useru 2 odmora
		username = "bogdan.blazic";
		initializeVacations(username);
		
		ResponseEntity<Vacation[]> response = restTamplete.getForEntity(uri, Vacation[].class);
		Vacation[] vacations = response.getBody();		
		id = vacations[0].getId(); //uzeti prvi odmor za updejt
		
		VacationRequest vacationRequest = VacationRequest.builder()
                                         .duration(22)
                                         .build();
		
		System.out.println("sessionID: " + sessionID + " id: " + id + " username: " + username);
		
		uriUpdate = "/vacation/{sessionID}/{id}/?user={username}";
		System.out.println("URI for update: " + uriUpdate.toString());

		restTamplete.put(uriUpdate, vacationRequest, sessionID, id, username);
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
	
	//ulogovati se kao admin i uraditi updejt odmora drugog usera
	@Test
	@Transactional
	void testUpdateVacationOtherUser() {
		login("bogdan.blazic");
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
	
	//ulogovati se kao admin i uraditi updejt odmora nepostojeceg usera
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

	//ulogovati se kao admin i obrisati svoj odmor
	@Test
	@Transactional
	void testDeleteVacation() {
		login("bogdan.blazic");
		//prvo kreirati useru 2 odmora
		username = "bogdan.blazic";
		initializeVacations(username);
		uri = "/vacation/"+ sessionID + "?user=" + username;
		restTamplete.delete(uri);
		ResponseEntity<ErrorResponseDto> response = restTamplete.getForEntity(uri, ErrorResponseDto.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
	
	//ulogovati se kao admin i obrisati tudji odmor
	@Test
	@Transactional
	void testDeleteVacationOtherUser() {
		login("bogdan.blazic");
		//prvo kreirati useru 2 odmora
		username = "olga.jesic";
		initializeVacations(username);
		uri = "/vacation/"+ sessionID + "?user=" + username;
		restTamplete.delete(uri);
		ResponseEntity<ErrorResponseDto> response = restTamplete.getForEntity(uri, ErrorResponseDto.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}	
	
	//ulogovati se kao admin i obrisati odmor nepostojeceg korisnika
	@Test
	@Transactional
	void testDeleteVacationInvalidUser() {
		login("bogdan.blazic");
		//prvo kreirati useru 2 odmora
		username = "olga.jesic";
		initializeVacations(username);
		username = "oolga.jesic";
		uri = "/vacation/"+ sessionID + "?user=" + username;
		restTamplete.delete(uri);
		ResponseEntity<ErrorResponseDto> response = restTamplete.getForEntity(uri, ErrorResponseDto.class);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}	
	
	//ulogovati se kao regular i kreirati odmor sebi
	@Test
	@Transactional
	void testCreateVacationLoggedRegular() {
		login("olga.jesic");
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
	
	//ulogovati se kao regular i kreirati odmor nekom drugom
	@Test
	@Transactional
	void testCreateVacationLoggedRegularOtherUser() {
		login("olga.jesic");
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
		assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());	
	}		
	
	//ulogovati se kao regular i procitati svoj vacation
	@Test
	@Transactional
	void testGetVacationLoggedRegular() {
		login("olga.jesic");
		//prvo kreirati sebi 2 odmora
		username = "olga.jesic";
		initializeVacations(username);
		ResponseEntity<Vacation[]> response = restTamplete.getForEntity(uri, Vacation[].class);
		Vacation[] vacations = response.getBody();
		assertNotNull(vacations);	
	}
	
	//ulogovati se kao regular i procitati tudji vacation
	@Test
	@Transactional
	void testGetVacationOtherUserLoggedRegular() {
		login("olga.jesic");
		uri = "/vacation/"+ sessionID + "?user=" + username;
		ResponseEntity<ErrorResponseDto> response = restTamplete.getForEntity(uri, ErrorResponseDto.class);
		assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
	}	
	
	//ulogovati se kao regular i uraditi updejt svog odmora
	@Test
	@Transactional
	void testUpdateVacationLoggedRegular() {
		login("olga.jesic");
		//prvo kreirati useru 2 odmora
		username = "olga.jesic";
		initializeVacations(username);
		uri = "/vacation/"+ sessionID + "?user=" + username;
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
	
	//ulogovati se kao regular i obrisati svoj odmor
	@Test
	@Transactional
	void testDeleteVacationLoggedRegular() {
		login("olga.jesic");
		//prvo kreirati useru 2 odmora
		username = "olga.jesic";
		initializeVacations(username);
		uri = "/vacation/"+ sessionID + "?user=" + username;
		restTamplete.delete(uri);
		ResponseEntity<ErrorResponseDto> response = restTamplete.getForEntity(uri, ErrorResponseDto.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}	
	
	//ulogovati se kao regular i obrisati tudji odmor
	@Test
	@Transactional
	void testDeleteVacationInvalidUserLoggedRegular() {
		login("olga.jesic");
		username = "bogdan.blazic";
		uri = "/vacation/"+ sessionID + "?user=" + username;
		restTamplete.delete(uri);
		ResponseEntity<ErrorResponseDto> response = restTamplete.getForEntity(uri, ErrorResponseDto.class);
		assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
	}	
	
	
}
