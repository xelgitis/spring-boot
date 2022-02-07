package com.example.springboot.controller;

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
import com.example.springboot.domain.User;
import com.example.springboot.service.LoginService;
import com.example.springboot.service.UserService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private LoginService loginService;	
	
    @Autowired
    private UserService userService;
    
    //get information about user with specific username
	@GetMapping(path="/{sessionID}",produces = APPLICATION_JSON_VALUE)
	public User getUser(@PathVariable String sessionID, @RequestParam(value="user") String user) {
		
		User loggedUser = loginService.getUser(sessionID);
		return userService.getUser(loggedUser, user);
	
	}
	
    //update user address, or name, or email or all
	@PutMapping(path="/{sessionID}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)	
	public void updateUser(@PathVariable String sessionID, @RequestBody UserRequest request, @RequestParam(value="user") String user) {
		
		User loggedUser = loginService.getUser(sessionID);
		userService.updateUser(loggedUser, user, request);
	}
	
	@DeleteMapping(path="/{sessionID}", produces = APPLICATION_JSON_VALUE)
	public void deleteUser(@PathVariable String sessionID, @RequestParam(value="user") String user) {		
		
		User loggedUser = loginService.getUser(sessionID);		
		userService.deleteUser(loggedUser, user);
	}
	
}
