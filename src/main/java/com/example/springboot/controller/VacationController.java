package com.example.springboot.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.domain.Vacation;
import com.example.springboot.domain.VacationRequest;
import com.example.springboot.domain.VacationResponse;
import com.example.springboot.service.LoginService;
import com.example.springboot.service.VacationService;

import javaslang.control.Try;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import javax.validation.Valid;

@RestController
@RequestMapping("/vacation")
public class VacationController {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
    @Autowired
    private LoginService loginService;
	
    @Autowired
    private VacationService vacationService;
    
    //TODO: ask wheter we can come to the situation where logged user is regular one, and it tries to see the vacation of another user
    //would that be even possible
    
	@GetMapping(path="/{sessionID}",produces = APPLICATION_JSON_VALUE)
	public Vacation getVacation(@PathVariable String sessionID, @RequestParam(value="user") String user) {
		
		String username = loginService.getUsernameBySessionID(sessionID);
		String role     = loginService.getRoleBySessionID(sessionID);
		logger.debug("GetMapping VacationController - ulogovani korisnik={}, role={} ", username, role);
		
		if (loginService.isAdmin(role)) {
			return vacationService.getVacation(user);	
		} else {			
			return vacationService.getVacation(username);
		}		
	}
	
	//TODO: add exception handling
	@PostMapping(path="/{sessionID}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public VacationResponse create(@PathVariable String sessionID, @Valid @RequestBody VacationRequest request, @RequestParam(value="user") String user) {
		
		VacationResponse vacationResponse;
		String username = loginService.getUsernameBySessionID(sessionID);
		String role     = loginService.getRoleBySessionID(sessionID);
		logger.debug("PostMapping VacationController - ulogovani korisnik={}, role={} ", username, role);
		
		if (loginService.isAdmin(role)) {
			vacationResponse = vacationService.createVacation(request, user);	
		} else {
			vacationResponse =  vacationService.createVacation(request, username);
		}			
		
        logger.info("Odgovor servisa za kreiranje odmora - response: {}", vacationResponse);
        return vacationResponse;
	}	
	
	@PutMapping(path="/{sessionID}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)	
	public VacationResponse updateVacation(@PathVariable String sessionID, @Valid @RequestBody VacationRequest request, @RequestParam(value="user") String user) {
		
		String username = loginService.getUsernameBySessionID(sessionID);
		String role     = loginService.getRoleBySessionID(sessionID);
		logger.debug("PutMapping VacationController - ulogovani korisnik={} ", username);
		
		if (loginService.isAdmin(role)) {
			return vacationService.updateVacation(request.getStartDate(), request.getDuration(), user);		
		} else {
			return vacationService.updateVacation(request.getStartDate(), request.getDuration(), username);
		}
	}	
	
	@DeleteMapping(path="/{sessionID}")
	public VacationResponse deleteUser(@PathVariable String sessionID, @RequestParam(value="user") String user) {
		
		String username = loginService.getUsernameBySessionID(sessionID);
		String role     = loginService.getRoleBySessionID(sessionID);
		logger.debug("DeleteMapping VacationController - ulogovani korisnik={} role={} ", username, role);
		
		if (loginService.isAdmin(role)) {
			return vacationService.deleteVacation(user);			
		} else {
		    return vacationService.deleteVacation(username);
		}		
	}	

}
