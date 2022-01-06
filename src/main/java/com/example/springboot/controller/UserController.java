package com.example.springboot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.domain.UserRequest;
import com.example.springboot.domain.UserResponse;
import com.example.springboot.domain.User;
import com.example.springboot.service.LoginService;
import com.example.springboot.service.UserService;

import javaslang.control.Try;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
    @Autowired
    private LoginService loginService;	
	
    @Autowired
    private UserService userService;
    
    //TODO: consider to add check for case when user is not admin and we still returns user info, maybe we should add returning error
    //TODO: consider changing some of prints to debug level
    
    //get information about user with specific username
	@GetMapping(path="/{sessionID}",produces = APPLICATION_JSON_VALUE)
	public User getUser(@PathVariable String sessionID, @RequestParam(value="user") String user) {

		String username = loginService.getUsernameBySessionID(sessionID);
		String role     = loginService.getRoleBySessionID(sessionID);
		logger.info("GetMapping UserController - ulogovani korisnik={}, role={} ", username, role);

		if (loginService.isAdmin(role)) {
			return userService.getUser(user);			
		} else {
			return userService.getUser(username);
		}		
	}
	
    //update user address, or name, or email or all
	@PutMapping(path="/{sessionID}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)	
	public UserResponse updateUser(@PathVariable String sessionID, @RequestBody UserRequest request, @RequestParam(value="user") String user) {
		
		String username = loginService.getUsernameBySessionID(sessionID);
		String role     = loginService.getRoleBySessionID(sessionID);
		
		logger.info("PutMapping UserController - ulogovani korisnik={} , role={} ", username, role);
		
		if (loginService.isAdmin(role)) {
			return userService.updateUser(user, request.getAddress(), request.getName(), request.getEmail());		
		} else {
			return userService.updateUser(username, request.getAddress(), request.getName(), request.getEmail());
		}
	}
	
	@DeleteMapping(path="/{sessionID}", produces = APPLICATION_JSON_VALUE)
	public UserResponse deleteUser(@PathVariable String sessionID, @RequestParam(value="user") String user) {
		
		String username = loginService.getUsernameBySessionID(sessionID);
		String role     = loginService.getRoleBySessionID(sessionID);
		logger.info("DeleteMapping UserController - ulogovani korisnik={} role={} ", username, role);
		
		if (loginService.isAdmin(role)) {
			return userService.deleteUser(user);
		} else {
			return userService.deleteUser(username);
		}
	}
	
}
