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
import com.example.springboot.domain.UserResponse;
import com.example.springboot.exeption.Status;
import com.example.springboot.exeption.VacationAppException;
import com.example.springboot.domain.Role;
import com.example.springboot.domain.User;
import com.example.springboot.service.LoginService;
import com.example.springboot.service.UserService;

import lombok.extern.slf4j.Slf4j;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    @Autowired
    private LoginService loginService;	
	
    @Autowired
    private UserService userService;
    
    //TODO: consider changing some of prints to debug level
    
    //get information about user with specific username
	@GetMapping(path="/{sessionID}",produces = APPLICATION_JSON_VALUE)
	public User getUser(@PathVariable String sessionID, @RequestParam(value="user") String user) {
		String username = loginService.getUsername(sessionID);
		Role   role     = loginService.getRole(sessionID);

		if (role.isAdmin(role.getRole())) {
			return userService.getUser(user);			
		} else {
			checkRequieredData(username, user);
			return userService.getUser(username);
		}		
	}
	
    //update user address, or name, or email or all
	@PutMapping(path="/{sessionID}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)	
	public UserResponse updateUser(@PathVariable String sessionID, @RequestBody UserRequest request, @RequestParam(value="user") String user) {
		String username = loginService.getUsername(sessionID);
		Role   role     = loginService.getRole(sessionID);
		
		if (role.isAdmin(role.getRole())) {
			return userService.updateUser(user, request);		
		} else {
			checkRequieredData(username, user);
			return userService.updateUser(username, request);
		}
	}
	
	@DeleteMapping(path="/{sessionID}", produces = APPLICATION_JSON_VALUE)
	public UserResponse deleteUser(@PathVariable String sessionID, @RequestParam(value="user") String user) {		
		String username = loginService.getUsername(sessionID);
		Role   role     = loginService.getRole(sessionID);
		
		if (role.isAdmin(role.getRole())) {			
			return userService.deleteUser(user);
		} else {
			checkRequieredData(username, user);				
			return userService.deleteUser(username);
		}
	}
	
	public void checkRequieredData(String username, String user) {
		if (!username.contentEquals(user)) {
			log.info("Nije dozvoljeno regular korisniku da gleda-azurira-brise podatke za drugog korisnika");
			throw new VacationAppException(Status.GENERIC_ERROR);
		}		
	}
	
}
