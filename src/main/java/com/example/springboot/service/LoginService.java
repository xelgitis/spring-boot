package com.example.springboot.service;

import java.util.HashMap;

import com.example.springboot.domain.LoginRequest;
import com.example.springboot.domain.LoginResponse;
import com.example.springboot.domain.Role;
import com.example.springboot.domain.User;

public interface LoginService {
	
	LoginResponse login(LoginRequest request);
	
	HashMap<String, User> getLoggedUsers();	
	
	User getUser(String sessionId);
	
	String getUsername(String sessionId);
	
	Role getRole(String sessionId);	

}
