package com.example.springboot.service;

import java.util.Map;

import com.example.springboot.domain.LoginRequest;
import com.example.springboot.domain.LoginResponse;
import com.example.springboot.domain.User;

public interface LoginService {
	
	LoginResponse login(LoginRequest request);
	
	Map<String, User> getLoggedUsers();	
	
	User getUser(String sessionId);
}
