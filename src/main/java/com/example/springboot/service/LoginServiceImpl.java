package com.example.springboot.service;

import java.util.HashMap;
import java.util.UUID;

import com.example.springboot.domain.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.domain.LoginRequest;
import com.example.springboot.domain.LoginResponse;
import com.example.springboot.domain.User;
import com.example.springboot.exeption.Status;
import com.example.springboot.exeption.VacationAppException;
import com.example.springboot.mapper.UserMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {
	
	HashMap<String, User> loggedUsers;	
	
	@Autowired 
	private UserService userService;	
	
	public LoginResponse login(LoginRequest request) {
		log.info("Provera username/pass za korisnika: {}", request.getUsername());
		
		User user = userService.findUser(request.getUsername(), request.getPassword());
		
    	String sessionId = UUID.randomUUID().toString();
		if(loggedUsers == null) loggedUsers = new HashMap<>();
		loggedUsers.put(sessionId, user);
		
		return new LoginResponse(user.getUsername(), sessionId, Status.SUCCESS, "Login za korisnika uspesan");
	}	
	
	@Override
	public HashMap<String, User> getLoggedUsers() {
		return loggedUsers;
	}

	@Override
	public User getUser(String sessionId) {		
		return loggedUsers.get(sessionId);
	}

}
