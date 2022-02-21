package com.example.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.domain.UserRequest;
import com.example.springboot.creator.ConversionUtils;
import com.example.springboot.domain.User;
import com.example.springboot.service.LoginService;
import com.example.springboot.service.UserService;
import com.example.springboot.validator.Validator;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private LoginService loginService;	
	
    @Autowired
    private UserService userService;
    
	@Autowired
	private ConversionUtils converter; 
	
	@Autowired
	private Validator validator;	
    
    //get information about user with specific username
	@GetMapping(path="/{sessionID}",produces = APPLICATION_JSON_VALUE)
	public User getUser(@PathVariable String sessionID, @RequestParam(value="user") String user) {
		
		User loggedUser = loginService.getLoggedUser(sessionID);
		validator.validatePrivilages(loggedUser, user);
		return userService.getUser(user);
	
	}
	
    @ResponseStatus(HttpStatus.NO_CONTENT)
	@PutMapping(path="/{sessionID}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)	
	public void updateUser(@PathVariable String sessionID, @RequestBody UserRequest request, @RequestParam(value="user") String user) {
		
    	User userForUpdate = converter.convertUserRequest(request, user);
		User loggedUser    = loginService.getLoggedUser(sessionID);
		validator.validatePrivilages(loggedUser, user);
		userService.updateUser(userForUpdate);
	}
	
    @ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping(path="/{sessionID}", produces = APPLICATION_JSON_VALUE)
	public void deleteUser(@PathVariable String sessionID, @RequestParam(value="user") String user) {		
		
		User loggedUser = loginService.getLoggedUser(sessionID);	
		validator.validatePrivilages(loggedUser, user);
		userService.deleteUser(user);
	}
	
}
