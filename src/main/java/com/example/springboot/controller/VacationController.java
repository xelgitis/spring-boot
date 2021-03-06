package com.example.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.creator.ConversionUtils;
import com.example.springboot.domain.User;
import com.example.springboot.domain.Vacation;
import com.example.springboot.domain.VacationRequest;
import com.example.springboot.domain.VacationResponse;
import com.example.springboot.exeption.Status;
import com.example.springboot.exeption.VacationAppException;
import com.example.springboot.service.LoginService;
import com.example.springboot.service.VacationService;
import com.example.springboot.validator.Validator;

import lombok.extern.slf4j.Slf4j;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("/vacation")
public class VacationController {
	
    @Autowired
    private LoginService loginService;
	
    @Autowired
    private VacationService vacationService;    
	
	@Autowired
	private ConversionUtils converter; 
	
	@Autowired
	private Validator validator;	
    
    
	@GetMapping(path="/{sessionID}",produces = APPLICATION_JSON_VALUE)
	public List <Vacation> getVacation(@PathVariable String sessionID, @RequestParam(value="user") String user) {
		User loggedUser = loginService.getLoggedUser(sessionID);
		validator.validatePrivilages(loggedUser, user);
		return vacationService.getVacation(user);	
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(path="/{sessionID}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public VacationResponse createVacation(@PathVariable String sessionID, @Valid @RequestBody VacationRequest request, @RequestParam(value="user") String user) {

		User loggedUser = loginService.getLoggedUser(sessionID);
		Vacation vacation = converter.convertVacationRequest(request, user);
		validator.validatePrivilages(loggedUser, user);
		VacationResponse vacationResponse = vacationService.createVacation(vacation);
	
        log.debug("Odgovor servisa za kreiranje odmora - response: {}", vacationResponse);
        return vacationResponse;
	}	
		
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PutMapping(path="/{sessionID}/{id}", consumes = APPLICATION_JSON_VALUE)	
	public void updateVacation(@PathVariable(name = "sessionID") String sessionID, @PathVariable(name = "id") Long id, @Valid @RequestBody VacationRequest request, @RequestParam(name = "user") String user) {
		User loggedUser = loginService.getLoggedUser(sessionID);
		log.debug("loggedUser {}", loggedUser);
		Vacation vacation = converter.convertVacationRequest(request, user);
		log.debug("vacation {}", vacation);
		vacation.setId(id);
		validator.validatePrivilages(loggedUser, user);
		vacationService.updateVacation(vacation);		
	}	
		
	@ResponseStatus(HttpStatus.OK)
	@DeleteMapping(path="/{sessionID}")
	public void deleteVacation(@PathVariable String sessionID, @RequestParam(value="user") String user) {
		User loggedUser = loginService.getLoggedUser(sessionID);
		validator.validatePrivilages(loggedUser, user);
		vacationService.deleteVacation(user);	
	}	
	
	@ResponseStatus(HttpStatus.OK)
	@DeleteMapping(path="/{sessionID}/{id}")
	public void deleteVacationById(@PathVariable String sessionID, @PathVariable Long id, @RequestParam(value="user") String user) {
		User loggedUser = loginService.getLoggedUser(sessionID);
		validator.validatePrivilages(loggedUser, user);
		vacationService.deleteVacationById(user, id);	
	}		
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PatchMapping(path="/{sessionID}/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)	
	public void approveVacation(@PathVariable String sessionID, @PathVariable Long id, @Valid @RequestBody VacationRequest request, @RequestParam(value="user") String user) {
		User loggedUser = loginService.getLoggedUser(sessionID);
		if (!validator.isAdmin(loggedUser)) throw new VacationAppException(Status.USER_NOT_ADMIN);
		Vacation vacation = converter.convertVacationRequest(request, user);
		vacation.setId(id);		
		vacationService.approveVacation(vacation);		
	}	

}
