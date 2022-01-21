package com.example.springboot.service;

import java.util.HashMap;

import com.example.springboot.domain.LoginRequest;
import com.example.springboot.domain.LoginResponse;
import com.example.springboot.domain.User;
import com.example.springboot.mapper.UserMapper;

public interface LoginService {
	
	LoginResponse login(LoginRequest request);
	
	UserMapper getMapper();
	
	//PasswordGeneratorService getPasswordGeneratorService();	
	
	HashMap<String, User> getLoggedUsers();	
	
	String getUsernameBySessionID(String sessionId);
	
	String getRoleBySessionID(String sessionId);
	
	boolean isAdmin(String role);
}
