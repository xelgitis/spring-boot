package com.example.springboot.service;

import java.util.HashMap;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.domain.LoginRequest;
import com.example.springboot.domain.LoginResponse;
import com.example.springboot.domain.User;
import com.example.springboot.mapper.UserMapper;

@Service
public class LoginServiceImpl implements LoginService {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	HashMap<String, User> loggedUsers;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired 
	private PasswordGeneratorService passwordGeneratorService;
	
	@Override 
	public UserMapper getMapper() {
		return userMapper;
	}	
	
	@Override 
	public PasswordGeneratorService getPasswordGeneratorService() {
		return passwordGeneratorService;
	}	
	
	public LoginResponse login(LoginRequest request) {
		logger.info("Provera username/pass za korisnika: {}", request.getUsername());
		
		if (request.getUsername() == null || request.getPassword() == null) {
			return new LoginResponse(LoginResponse.Status.WRONG_FORMAT_DATA, "Username ili šifra nisu uneseni");
		}
		
		User user = getMapper().findPasswordDataByUsername(request.getUsername());
		
		if (user == null) {
			logger.error("Username ne postoji u bazi");
			return new LoginResponse(LoginResponse.Status.ERROR, "Username ne postoji u bazi");
		}		
		
		user.setPassword(request.getPassword());
		logger.info("Dohvacen user: {}", user.toString());
		try {
			getPasswordGeneratorService().checkPassword(user);
			logger.info("Provera passworda uspesna za username {}", user.getUsername());
		} catch (Exception e) {
			logger.error("Nije pronadjen korisnik za username i password, username: {}", request.getUsername());
			return handleFailedLoginAttempts(user);
		}		

		String sessionId = UUID.randomUUID().toString();
		if(loggedUsers == null) loggedUsers = new HashMap<>();
		loggedUsers.put(sessionId, user);
		
		return new LoginResponse(user.getUsername(), sessionId, LoginResponse.Status.SUCCESS, "Login za korisnika uspesan");
	}	
	
	LoginResponse handleFailedLoginAttempts(User user) {
		return new LoginResponse(LoginResponse.Status.WRONG_PASSWORD, "Korisnik je uneo pogresnu sifru");
	}

	@Override
	public HashMap<String, User> getLoggedUsers() {
		return loggedUsers;
	}

	@Override
	public String getUsernameBySessionID(String sessionId) {		
		return loggedUsers.get(sessionId).getUsername();
	}

	@Override
	public String getRoleBySessionID(String sessionId) {
		return loggedUsers.get(sessionId).getRole();
	}

	@Override
	public boolean isAdmin(String role) {
		if (role.equalsIgnoreCase("administrator")) return true;
		else return false;
	}	

}
