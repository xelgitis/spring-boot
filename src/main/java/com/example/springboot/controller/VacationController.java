package com.example.springboot.controller;

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

import com.example.springboot.domain.Role;
import com.example.springboot.domain.User;
import com.example.springboot.domain.Vacation;
import com.example.springboot.domain.VacationRequest;
import com.example.springboot.domain.VacationResponse;
import com.example.springboot.exeption.Status;
import com.example.springboot.exeption.VacationAppException;
import com.example.springboot.service.LoginService;
import com.example.springboot.service.VacationService;

import lombok.extern.slf4j.Slf4j;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("/vacation")
public class VacationController {
	
    @Autowired
    private LoginService loginService;
	
    @Autowired
    private VacationService vacationService;
    
    
	@GetMapping(path="/{sessionID}",produces = APPLICATION_JSON_VALUE)
	public Vacation getVacation(@PathVariable String sessionID, @RequestParam(value="user") String user) {
		
		User loggedUser = loginService.getUser(sessionID);
		return vacationService.getVacation(loggedUser, user);	
	}
	
	//TODO: add exception handling
	@PostMapping(path="/{sessionID}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public VacationResponse create(@PathVariable String sessionID, @Valid @RequestBody VacationRequest request, @RequestParam(value="user") String user) {
		
		VacationResponse vacationResponse;
		User loggedUser = loginService.getUser(sessionID);
		vacationResponse = vacationService.createVacation(loggedUser, request, user);
	
        log.info("Odgovor servisa za kreiranje odmora - response: {}", vacationResponse);
        return vacationResponse;
	}	
	
	@PutMapping(path="/{sessionID}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)	
	public void updateVacation(@PathVariable String sessionID, @Valid @RequestBody VacationRequest request, @RequestParam(value="user") String user) {
		
		User loggedUser = loginService.getUser(sessionID);
		vacationService.updateVacation(loggedUser, request, user);		
	}	
	
	@DeleteMapping(path="/{sessionID}")
	public void deleteUser(@PathVariable String sessionID, @RequestParam(value="user") String user) {
		
		User loggedUser = loginService.getUser(sessionID);
		vacationService.deleteVacation(loggedUser, user);	
	}	

}
