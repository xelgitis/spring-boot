package com.example.springboot.service;

import java.util.HashMap;

import com.example.springboot.domain.LoginRequest;
import com.example.springboot.domain.LoginResponse;
import com.example.springboot.domain.Role;
import com.example.springboot.domain.User;
import com.example.springboot.mapper.UserMapper;

public interface LoginService {
	
	LoginResponse login(LoginRequest request);
	
	HashMap<String, User> getLoggedUsers();	
	
	String getUsername(String sessionId);
	
	Role getRole(String sessionId);	

}
