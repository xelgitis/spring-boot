package com.example.springboot.dbloader;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.example.springboot.domain.RegistrationResponse;
import com.example.springboot.domain.Role;
import com.example.springboot.domain.User;
import com.example.springboot.domain.Vacation;
import com.example.springboot.domain.VacationResponse;
import com.example.springboot.service.UserService;
import com.example.springboot.service.VacationService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TestDBLoader implements CommandLineRunner {
	
	
	@Autowired
	UserService userService;	
	
	@Autowired
	VacationService vacationService;	

	@Override
	public void run(String... args) throws Exception {
		
		Calendar c1 = Calendar.getInstance();
		Role adminRole = Role.builder()
				    	.name("administrator")
				    	.build();
		
		Role userRole = Role.builder()
		    			.name("user")
		    			.build();		
		
		log.info("Creating initial DB data");
		
		//kreiranje administratora i regular usera	
		User administratorRegistration  = User.builder()
	                					  .username("bogdan.blazic")
	                					  .password("bogdan.blazic")
	                					  .address("Ivana Blagojevica 14")
	                					  .name("Bogdan Blazic")
	                					  .email("bogdan.blazic@mozzartbet.com")
	                					  .role(adminRole)
	                					  .build();

		RegistrationResponse administratorResponse = userService.registerUser(administratorRegistration);
		assertEquals(HttpStatus.OK, administratorResponse.getStatus().getHttpStatus());			
			
		User regularRegistration = User.builder()
						           .username("olga.jesic")
						           .password("olga.jesic")
						           .address("Ivana Blagojevica 14")
						           .name("Olga Jesic")
						           .email("olga.jesic@mozzartbet.com")
						           .role(userRole)
						           .build();
				
		RegistrationResponse regularResponse = userService.registerUser(regularRegistration);
		assertEquals(HttpStatus.OK, regularResponse.getStatus().getHttpStatus());
		

		c1.set(Calendar.YEAR,  2023);
		c1.set(Calendar.MONTH, 7); 
		c1.set(Calendar.DATE,  30);		
		Vacation vacation = Vacation.builder()
					        .startDate(c1.getTime())
					        .duration(15)
					        .approval("N")
					        .username("bogdan.blazic")
					        .build();
		
		VacationResponse vacationResponse = vacationService.createVacation(vacation);		
		assertEquals(HttpStatus.OK, vacationResponse.getStatus().getHttpStatus());	
			
		c1.set(Calendar.YEAR,  2023);
		c1.set(Calendar.MONTH, 9); 
		c1.set(Calendar.DATE,  15);		
		vacation = Vacation.builder()
		           .startDate(c1.getTime())
		           .duration(9)
		           .approval("N")
		           .username("bogdan.blazic")
		           .build();
		
		vacationResponse = vacationService.createVacation(vacation);		
		assertEquals(HttpStatus.OK, vacationResponse.getStatus().getHttpStatus());	
		
		c1.set(Calendar.YEAR,  2023);
		c1.set(Calendar.MONTH, 11); 
		c1.set(Calendar.DATE,  9);		
		vacation = Vacation.builder()
				   .startDate(c1.getTime())
				   .duration(13)
				   .approval("N")
				   .username("olga.jesic")
				   .build();
		
		vacationResponse = vacationService.createVacation(vacation);		
		assertEquals(HttpStatus.OK, vacationResponse.getStatus().getHttpStatus());	
			
		c1.set(Calendar.YEAR,  2023);
		c1.set(Calendar.MONTH, 5); 
		c1.set(Calendar.DATE,  15);		
		vacation = Vacation.builder()
		           .startDate(c1.getTime())
		           .duration(7)
		           .approval("N")
		           .username("olga.jesic")
		           .build();
		
		vacationResponse = vacationService.createVacation(vacation);		
		assertEquals(HttpStatus.OK, vacationResponse.getStatus().getHttpStatus());			
		
	}
}
