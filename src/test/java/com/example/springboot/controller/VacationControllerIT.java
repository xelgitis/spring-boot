package com.example.springboot.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

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
import com.example.springboot.domain.Vacation;
import com.example.springboot.domain.VacationRequest;
import com.example.springboot.domain.VacationResponse;

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

	//ulogovati se kao admin i kreirati odmor sebi
	@Test
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
	void testGetVacation() {
		login("bogdan.blazic");
		username = "bogdan.blazic";
		params.put("user", username);	
		ResponseEntity<Vacation[]> response = restTamplete.getForEntity(uri, Vacation[].class, params);
		Vacation[] vacations = response.getBody();
		assertNotNull(vacations);	
	}
	
	//ulogovati se kao admin i procitati odmor drugog usera
	@Test
	void testGetVacationOtherUser() {
		login("bogdan.blazic");
		username = "olga.jesic";
		params.put("user", username);
		ResponseEntity<Vacation[]> response = restTamplete.getForEntity(uri, Vacation[].class, params);
		Vacation[] vacations = response.getBody();
		assertNotNull(vacations);	
	}	

	//ulogovati se kao admin i procitati odmor nepostojeceg usera
	@Test
	void testGetVacationInvalidUser() {
		login("bogdan.blazic");
		username = "olga.jesic";
		params.put("user", username);
		
		//invalid username
		username = "oolga.jesic";
		params.put("user", username);
		
		ResponseEntity<ErrorResponseDto> response = restTamplete.getForEntity(uri, ErrorResponseDto.class, params);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}		
	
	//ulogovati se kao admin i uraditi updejt svog odmora
	@Test
	void testUpdateVacation() throws ParseException {
		login("bogdan.blazic");
		username = "bogdan.blazic";
		params.put("user", username);
		
		ResponseEntity<Vacation[]> response = restTamplete.getForEntity(uri, Vacation[].class, params);
		Vacation[] vacations = response.getBody();		
		id = vacations[0].getId(); //uzeti prvi odmor za updejt
		
		VacationRequest vacationRequest = VacationRequest.builder()
				                          .startDate(dateFormatter.parse("2023-01-01"))
                                          .duration(99)            
                                          .build();
		
		params.put("id", id.toString());

		restTamplete.put(uriUpdate, vacationRequest, params);
		response = restTamplete.getForEntity(uri, Vacation[].class, params);

		Vacation vacation = Stream.of(response.getBody())
							.filter(updatedVacation -> updatedVacation.getId().equals(id))
							.findFirst()
							.get();
        assertTrue(vacation.getDuration() == 99);
	}
	
	//ulogovati se kao admin i uraditi updejt odmora drugog usera
	@Test
	void testUpdateVacationOtherUser() throws ParseException {
		login("bogdan.blazic");
		username = "olga.jesic";
		params.put("user", username);
		
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
	void testUpdateVacationInvalidUser() throws ParseException {
		login("bogdan.blazic");
		username = "olga.jesic";
		params.put("user", username);
		
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
	void testDeleteVacation() {
		login("bogdan.blazic");
		username = "bogdan.blazic";
		params.put("user", username);
		//prvo kreirati useru odmor za brisanje
		c1.set(Calendar.YEAR,  2023);
		c1.set(Calendar.MONTH, 5); 
		c1.set(Calendar.DATE,  13);		
		VacationRequest request = VacationRequest.builder()
                				  .startDate(c1.getTime())
                				  .duration(88)
                				  .approval("N")
                				  .build();
		
		ResponseEntity<VacationResponse> response = restTamplete.postForEntity(uri, new HttpEntity<>(request), VacationResponse.class, params);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());	
		
		ResponseEntity<Vacation[]> responseVacation = restTamplete.getForEntity(uri, Vacation[].class, params);
		Vacation[] vacations = responseVacation.getBody();	
		
		//TODO: change to streams
		for (int i = 0; i < vacations.length; i++) {
			if (vacations[i].getDuration() == 88) {
				id =  vacations[i].getId();
				break;
			}
		}

		params.put("id", id.toString());				
		restTamplete.delete(uriUpdate, params);
	}
	
	//ulogovati se kao admin i obrisati tudji odmor
	@Test
	void testDeleteVacationOtherUser() {
		login("bogdan.blazic");
		username = "olga.jesic";
		params.put("user", username);
		//prvo kreirati useru odmor za brisanje
		c1.set(Calendar.YEAR,  2023);
		c1.set(Calendar.MONTH, 5); 
		c1.set(Calendar.DATE,  13);		
		VacationRequest request = VacationRequest.builder()
                				  .startDate(c1.getTime())
                				  .duration(88)
                				  .approval("N")
                				  .build();
		
		ResponseEntity<VacationResponse> response = restTamplete.postForEntity(uri, new HttpEntity<>(request), VacationResponse.class, params);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());	
		
		ResponseEntity<Vacation[]> responseVacation = restTamplete.getForEntity(uri, Vacation[].class, params);
		Vacation[] vacations = responseVacation.getBody();	
		
		//TODO: change to streams
		for (int i = 0; i < vacations.length; i++) {
			if (vacations[i].getDuration() == 88) {
				id =  vacations[i].getId();
				break;
			}
		}

		params.put("id", id.toString());				
		restTamplete.delete(uriUpdate, params);
	}	
	
	//ulogovati se kao admin i obrisati odmor nepostojeceg korisnika
	@Test
	void testDeleteVacationInvalidUser() {
		login("bogdan.blazic");
		username = "olga.jesic";
		params.put("user", username);
		//prvo kreirati useru odmor za brisanje
		c1.set(Calendar.YEAR,  2023);
		c1.set(Calendar.MONTH, 5); 
		c1.set(Calendar.DATE,  13);		
		VacationRequest request = VacationRequest.builder()
                				  .startDate(c1.getTime())
                				  .duration(88)
                				  .approval("N")
                				  .build();
		
		ResponseEntity<VacationResponse> response = restTamplete.postForEntity(uri, new HttpEntity<>(request), VacationResponse.class, params);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());	
		
		ResponseEntity<Vacation[]> responseVacation = restTamplete.getForEntity(uri, Vacation[].class, params);
		Vacation[] vacations = responseVacation.getBody();	
		
		//TODO: change to streams
		for (int i = 0; i < vacations.length; i++) {
			if (vacations[i].getDuration() == 88) {
				id =  vacations[i].getId();
				break;
			}
		}

		params.put("id", id.toString());	
		username = "oolga.jesic";
		params.put("user", username);		
		restTamplete.delete(uriUpdate, params);
	}	
	
	//ulogovati se kao regular i kreirati odmor sebi
	@Test
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
	void testGetVacationLoggedRegular() {
		login("olga.jesic");
		username = "olga.jesic";
		params.put("user", username);
		ResponseEntity<Vacation[]> response = restTamplete.getForEntity(uri, Vacation[].class, params);
		Vacation[] vacations = response.getBody();
		assertNotNull(vacations);	
	}
	
	//ulogovati se kao regular i procitati tudji vacation
	@Test
	void testGetVacationOtherUserLoggedRegular() {
		login("olga.jesic");
		username = "bogdan.blazic";
		params.put("user", username);
		ResponseEntity<ErrorResponseDto> response = restTamplete.getForEntity(uri, ErrorResponseDto.class, params);
		assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
	}	
	
	//ulogovati se kao regular i uraditi updejt svog odmora
	@Test
	void testUpdateVacationLoggedRegular() throws ParseException {
		login("olga.jesic");
		username = "olga.jesic";
		params.put("user", username);
		
		ResponseEntity<Vacation[]> response = restTamplete.getForEntity(uri, Vacation[].class, params);
		Vacation[] vacations = response.getBody();		
		id = vacations[0].getId(); //uzeti prvi odmor za updejt
		
		VacationRequest vacationRequest = VacationRequest.builder()
				                          .startDate(dateFormatter.parse("2023-01-01"))
                                          .duration(44)            
                                          .build();
		
		params.put("id", id.toString());

		restTamplete.put(uriUpdate, vacationRequest, params);
		response = restTamplete.getForEntity(uri, Vacation[].class, params);

		Vacation vacation = Stream.of(response.getBody())
							.filter(updatedVacation -> updatedVacation.getId().equals(id))
							.findFirst()
							.get();
        assertTrue(vacation.getDuration() == 44);
	}
	
	//ulogovati se kao regular i obrisati svoj odmor
	@Test
	void testDeleteVacationLoggedRegular() {
		login("olga.jesic");
		username = "olga.jesic";
		params.put("user", username);
		
		//prvo kreirati useru odmor za brisanje
		c1.set(Calendar.YEAR,  2023);
		c1.set(Calendar.MONTH, 5); 
		c1.set(Calendar.DATE,  13);		
		VacationRequest request = VacationRequest.builder()
                				  .startDate(c1.getTime())
                				  .duration(77)
                				  .approval("N")
                				  .build();
		
		ResponseEntity<VacationResponse> response = restTamplete.postForEntity(uri, new HttpEntity<>(request), VacationResponse.class, params);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());	
		
		ResponseEntity<Vacation[]> responseVacation = restTamplete.getForEntity(uri, Vacation[].class, params);
		Vacation[] vacations = responseVacation.getBody();	
		
		//TODO: change to streams
		for (int i = 0; i < vacations.length; i++) {
			if (vacations[i].getDuration() == 77) {
				id =  vacations[i].getId();
				break;
			}
		}		
		
		params.put("id", id.toString());	
		restTamplete.delete(uri, params);
	}	
	
	//ulogovati se kao regular i obrisati tudji odmor
	@Test
	void testDeleteVacationOtherUserLoggedRegular() {
		login("olga.jesic");
		username = "bogdan.blazic";
		params.put("user", username);
		restTamplete.delete(uri, params);
		ResponseEntity<ErrorResponseDto> response = restTamplete.getForEntity(uri, ErrorResponseDto.class, params);
		assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
	}		
	
}
