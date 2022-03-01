package com.example.springboot.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

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

import com.example.springboot.creator.ConversionUtils;
import com.example.springboot.domain.ErrorResponseDto;
import com.example.springboot.domain.LoginRequest;
import com.example.springboot.domain.LoginResponse;
import com.example.springboot.domain.RegistrationRequest;
import com.example.springboot.domain.RegistrationResponse;
import com.example.springboot.domain.User;
import com.example.springboot.domain.Vacation;
import com.example.springboot.domain.VacationRequest;
import com.example.springboot.domain.VacationResponse;
import com.example.springboot.service.LoginService;
import com.example.springboot.service.UserService;
import com.example.springboot.service.VacationService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class VacationControllerIT {
	
	Calendar c1 = Calendar.getInstance();
	
	SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
	
	private String sessionID;
	private String uri = "/vacation/{sessionID}/?user={user}";
	private String uriUpdate = "/vacation/{sessionID}/{id}?user={user}";
	private Long id;
	private String username;  	
	private Map<String, String> params = new HashMap<>();
		
	@Autowired
	TestRestTemplate restTamplete;
	
	@Autowired
	UserService userService;	
	
	@Autowired
	LoginService loginService;
	
	@Autowired
	VacationService vacationService;	
	
	@Autowired
	private ConversionUtils converter;
	
	//try all testcases logged as admin and logged as regular user	
	@BeforeEach
	public void setUp(){
		//kreiranje administratora i regular usera	
		RegistrationRequest administratorRegistrationRequest  = RegistrationRequest.builder()
                					   							.username("bogdan.blazic")
                					   							.password("bogdan.blazic")
                					   							.address("Ivana Blagojevica 14")
                					   							.name("Bogdan Blazic")
                					   							.email("bogdan.blazic@mozzartbet.com")
                					   							.role("administrator")
                					   							.build();

       //ResponseEntity<RegistrationResponse> administratorRegistrationResponse = restTamplete.postForEntity("/register", new HttpEntity<>(administratorRegistrationRequest), RegistrationResponse.class);
		User user = converter.convertRegistrationRequest(administratorRegistrationRequest);
		RegistrationResponse administratorRegistrationResponse = userService.registerUser(user);
		assertEquals(HttpStatus.OK, administratorRegistrationResponse.getStatus().getHttpStatus());			
		
	   RegistrationRequest regularRegistrationRequest = RegistrationRequest.builder()
					                  					.username("olga.jesic")
					                  					.password("olga.jesic")
					                  					.address("Ivana Blagojevica 14")
					                  					.name("Olga Jesic")
					                  					.email("olga.jesic@mozzartbet.com")
					                  					.role("user")
					                  					.build();
			
	    //ResponseEntity<RegistrationResponse> regularRegistrationResponse = restTamplete.postForEntity("/register", new HttpEntity<>(regularRegistrationRequest), RegistrationResponse.class);
	   user = converter.convertRegistrationRequest(regularRegistrationRequest);	
	   RegistrationResponse regularRegistrationResponse = userService.registerUser(user);
	   assertEquals(HttpStatus.OK, regularRegistrationResponse.getStatus().getHttpStatus());
	}	
	
	public void login(String loggedUser) {		
		//logovanje
		LoginRequest request = LoginRequest.builder()
				               .username(loggedUser)
				               .password(loggedUser)
				               .build();
	
		LoginResponse logedUser = restTamplete.postForObject("/login", request, LoginResponse.class);
		sessionID = logedUser.getSessionId();		
		params.put("sessionID", sessionID);	
	}
	
	public void initializeVacations(String username) {
		
		c1.set(Calendar.YEAR,  2022);
		c1.set(Calendar.MONTH, 7); 
		c1.set(Calendar.DATE,  30);		
		VacationRequest request1 = VacationRequest.builder()
				                  .startDate(c1.getTime())
				                  .duration(15)
				                  .approval("N")
				                  .build();
	
		ResponseEntity<VacationResponse> response1 = restTamplete.postForEntity(uri, new HttpEntity<>(request1), VacationResponse.class, params);
		assertEquals(HttpStatus.CREATED, response1.getStatusCode());	
		
		c1.set(Calendar.YEAR,  2022);
		c1.set(Calendar.MONTH, 9); 
		c1.set(Calendar.DATE,  15);		
		VacationRequest request2 = VacationRequest.builder()
				                  .startDate(c1.getTime())
				                  .duration(13)
				                  .approval("N")
				                  .build();
	
		ResponseEntity<VacationResponse> response2 = restTamplete.postForEntity(uri, new HttpEntity<>(request2), VacationResponse.class, params);
		assertEquals(HttpStatus.CREATED, response2.getStatusCode());			
	}

	//ulogovati se kao admin i kreirati odmor sebi
	@Test
	@Transactional
	void testCreateVacation() {
		login("bogdan.blazic");
		username = "bogdan.blazic";
		params.put("user", username);		
	
		c1.set(Calendar.YEAR,  2022);
		c1.set(Calendar.MONTH, 7); 
		c1.set(Calendar.DATE,  30);		
		VacationRequest request = VacationRequest.builder()
				                  .startDate(c1.getTime())
				                  .duration(15)
				                  .approval("N")
				                  .build();
	
		ResponseEntity<VacationResponse> response = restTamplete.postForEntity(uri, new HttpEntity<>(request), VacationResponse.class, params);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());	
	}	
	
	//ulogovati se kao admin i kreirati odmor nekom drugom
	@Test
	@Transactional
	void testCreateVacationOtherUser() {
		login("bogdan.blazic");
		username = "olga.jesic";
		params.put("user", username);		
		
		c1.set(Calendar.YEAR,  2022);
		c1.set(Calendar.MONTH, 7); 
		c1.set(Calendar.DATE,  30);		
		VacationRequest request = VacationRequest.builder()
				                  .startDate(c1.getTime())
				                  .duration(15)
				                  .approval("N")
				                  .build();
	
		ResponseEntity<VacationResponse> response = restTamplete.postForEntity(uri, new HttpEntity<>(request), VacationResponse.class, params);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());	
	}	
	
	//ulogovati se kao admin i kreirati odmor nepostojecem korisniku
	@Test
	@Transactional
	void testCreateVacationInvalidUser() {
		login("bogdan.blazic");
		username = "oolga.jesic";
		params.put("user", username);
	
		c1.set(Calendar.YEAR,  2022);
		c1.set(Calendar.MONTH, 7); 
		c1.set(Calendar.DATE,  30);		
		VacationRequest request = VacationRequest.builder()
				                  .startDate(c1.getTime())
				                  .duration(15)
				                  .approval("N")
				                  .build();
	
		ResponseEntity<VacationResponse> response = restTamplete.postForEntity(uri, new HttpEntity<>(request), VacationResponse.class, params);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());	
	}		

	//ulogovati se kao admin i procitati svoj vacation
	@Test
	@Transactional
	void testGetVacation() {
		login("bogdan.blazic");
		//prvo kreirati sebi 2 odmora
		username = "bogdan.blazic";
		params.put("user", username);
		initializeVacations(username);		
		ResponseEntity<Vacation[]> response = restTamplete.getForEntity(uri, Vacation[].class, params);
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
		params.put("user", username);
		initializeVacations(username);	
		ResponseEntity<Vacation[]> response = restTamplete.getForEntity(uri, Vacation[].class, params);
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
		params.put("user", username);
		initializeVacations(username);
		
		//invalid username
		username = "oolga.jesic";
		params.put("user", username);
		
		ResponseEntity<ErrorResponseDto> response = restTamplete.getForEntity(uri, ErrorResponseDto.class, params);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}		
	
	//ulogovati se kao admin i uraditi updejt svog odmora
	@Test
	@Transactional
	void testUpdateVacation() throws ParseException {
		login("bogdan.blazic");
		//prvo kreirati useru 2 odmora
		username = "bogdan.blazic";
		params.put("user", username);
		initializeVacations(username);
		
		ResponseEntity<Vacation[]> response = restTamplete.getForEntity(uri, Vacation[].class, params);
		Vacation[] vacations = response.getBody();		
		id = vacations[0].getId(); //uzeti prvi odmor za updejt
		
		VacationRequest vacationRequest = VacationRequest.builder()
				                          .startDate(dateFormatter.parse("2023-01-01"))
                                          .duration(22)            
                                          .build();
		
		params.put("id", id.toString());

		restTamplete.put(uriUpdate, vacationRequest, params);
		response = restTamplete.getForEntity(uri, Vacation[].class, params);

		Vacation vacation = Stream.of(response.getBody())
							.filter(updatedVacation -> updatedVacation.getId().equals(id))
							.findFirst()
							.get();
        assertTrue(vacation.getDuration() == 22);
	}
	
	//ulogovati se kao admin i uraditi updejt odmora drugog usera
	@Test
	@Transactional
	void testUpdateVacationOtherUser() throws ParseException {
		login("bogdan.blazic");
		//prvo kreirati useru 2 odmora
		username = "olga.jesic";
		params.put("user", username);
		initializeVacations(username);
		
		ResponseEntity<Vacation[]> response = restTamplete.getForEntity(uri, Vacation[].class, params);
		Vacation[] vacations = response.getBody();		
		id = vacations[0].getId(); //uzeti prvi odmor za updejt
		
		VacationRequest vacationRequest = VacationRequest.builder()
				                          .startDate(dateFormatter.parse("2023-01-01"))
                                          .duration(55)            
                                          .build();
		
		params.put("id", id.toString());

		restTamplete.put(uriUpdate, vacationRequest, params);
		response = restTamplete.getForEntity(uri, Vacation[].class, params);

		Vacation vacation = Stream.of(response.getBody())
							.filter(updatedVacation -> updatedVacation.getId().equals(id))
							.findFirst()
							.get();
        assertTrue(vacation.getDuration() == 55);
	}	
	
	//ulogovati se kao admin i uraditi updejt odmora nepostojeceg usera
	@Test
	@Transactional
	void testUpdateVacationInvalidUser() throws ParseException {
		login("bogdan.blazic");
		//prvo kreirati useru 2 odmora
		username = "olga.jesic";
		params.put("user", username);
		initializeVacations(username);
		
		ResponseEntity<Vacation[]> response = restTamplete.getForEntity(uri, Vacation[].class, params);
		Vacation[] vacations = response.getBody();		
		id = vacations[0].getId(); //uzeti prvi odmor za updejt
		
		VacationRequest vacationRequest = VacationRequest.builder()
				                         .startDate(dateFormatter.parse("2023-01-01"))
                                         .duration(22)
                                         .build();
		
		username = "oolga.jesic";
		params.put("id", id.toString());
		params.put("user", username);

		restTamplete.put(uriUpdate, vacationRequest, params);
		
		ResponseEntity<ErrorResponseDto> vacationResponse = restTamplete.getForEntity(uri, ErrorResponseDto.class, params);
		assertEquals(HttpStatus.NOT_FOUND, vacationResponse.getStatusCode());		
	}	

	//ulogovati se kao admin i obrisati svoj odmor
	@Test
	@Transactional
	void testDeleteVacation() {
		login("bogdan.blazic");
		//prvo kreirati useru 2 odmora
		username = "bogdan.blazic";
		params.put("user", username);
		initializeVacations(username);
		restTamplete.delete(uri, params);
		ResponseEntity<ErrorResponseDto> response = restTamplete.getForEntity(uri, ErrorResponseDto.class, params);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
	
	//ulogovati se kao admin i obrisati tudji odmor
	@Test
	@Transactional
	void testDeleteVacationOtherUser() {
		login("bogdan.blazic");
		//prvo kreirati useru 2 odmora
		username = "olga.jesic";
		params.put("user", username);
		initializeVacations(username);
		restTamplete.delete(uri, params);
		ResponseEntity<ErrorResponseDto> response = restTamplete.getForEntity(uri, ErrorResponseDto.class, params);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}	
	
	//ulogovati se kao admin i obrisati odmor nepostojeceg korisnika
	@Test
	@Transactional
	void testDeleteVacationInvalidUser() {
		login("bogdan.blazic");
		//prvo kreirati useru 2 odmora
		username = "olga.jesic";
		params.put("user", username);
		initializeVacations(username);
		username = "oolga.jesic";
		params.put("user", username);
		restTamplete.delete(uri, params);
		ResponseEntity<ErrorResponseDto> response = restTamplete.getForEntity(uri, ErrorResponseDto.class, params);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}	
	
	//ulogovati se kao regular i kreirati odmor sebi
	@Test
	@Transactional
	void testCreateVacationLoggedRegular() {
		login("olga.jesic");
		username = "olga.jesic";
		params.put("user", username);
		c1.set(Calendar.YEAR,  2022);
		c1.set(Calendar.MONTH, 7); 
		c1.set(Calendar.DATE,  30);		
		VacationRequest request = VacationRequest.builder()
				                  .startDate(c1.getTime())
				                  .duration(15)
				                  .approval("N")
				                  .build();
	
		ResponseEntity<VacationResponse> response = restTamplete.postForEntity(uri, new HttpEntity<>(request), VacationResponse.class, params);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());	
	}	
	
	//ulogovati se kao regular i kreirati odmor nekom drugom
	@Test
	@Transactional
	void testCreateVacationLoggedRegularOtherUser() {
		login("olga.jesic");
		username = "bogdan.blazic";
		params.put("user", username);
		c1.set(Calendar.YEAR,  2022);
		c1.set(Calendar.MONTH, 7); 
		c1.set(Calendar.DATE,  30);		
		VacationRequest request = VacationRequest.builder()
				                  .startDate(c1.getTime())
				                  .duration(15)
				                  .approval("N")
				                  .build();
	
		ResponseEntity<VacationResponse> response = restTamplete.postForEntity(uri, new HttpEntity<>(request), VacationResponse.class, params);
		assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());	
	}		
	
	//ulogovati se kao regular i procitati svoj vacation
	@Test
	@Transactional
	void testGetVacationLoggedRegular() {
		login("olga.jesic");
		//prvo kreirati sebi 2 odmora
		username = "olga.jesic";
		params.put("user", username);
		initializeVacations(username);
		ResponseEntity<Vacation[]> response = restTamplete.getForEntity(uri, Vacation[].class, params);
		Vacation[] vacations = response.getBody();
		assertNotNull(vacations);	
	}
	
	//ulogovati se kao regular i procitati tudji vacation
	@Test
	@Transactional
	void testGetVacationOtherUserLoggedRegular() {
		login("olga.jesic");
		username = "bogdan.blazic";
		params.put("user", username);
		ResponseEntity<ErrorResponseDto> response = restTamplete.getForEntity(uri, ErrorResponseDto.class, params);
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
		params.put("user", username);
		initializeVacations(username);
		restTamplete.delete(uri, params);
		ResponseEntity<ErrorResponseDto> response = restTamplete.getForEntity(uri, ErrorResponseDto.class, params);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}	
	
	//ulogovati se kao regular i obrisati tudji odmor
	@Test
	@Transactional
	void testDeleteVacationOtherUserLoggedRegular() {
		login("olga.jesic");
		username = "bogdan.blazic";
		params.put("user", username);
		restTamplete.delete(uri, params);
		ResponseEntity<ErrorResponseDto> response = restTamplete.getForEntity(uri, ErrorResponseDto.class, params);
		assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
	}	
	
	
}
