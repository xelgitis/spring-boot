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
import com.example.springboot.exeption.GenericResponse;
import com.example.springboot.exeption.VacationAppException;
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
	
	public LoginResponse login(LoginRequest request) {
		logger.info("Provera username/pass za korisnika: {}", request.getUsername());
		
		User user = getMapper().findPasswordDataByUsername(request.getUsername());
		
		if (user == null) {
			logger.error("Username ne postoji u bazi");
			throw new VacationAppException("Korisnik sa username-om = " + request.getUsername() + " ne postoji u bazi", GenericResponse.USER_NOT_FOUND);
		}		
		
		user.setPassword(request.getPassword());
		logger.debug("Dohvacen user: {}", user.toString());

		passwordGeneratorService.checkPassword(user);

		String sessionId = UUID.randomUUID().toString();
		if(loggedUsers == null) loggedUsers = new HashMap<>();
		loggedUsers.put(sessionId, user);
		
		return new LoginResponse(user.getUsername(), sessionId, LoginResponse.Status.SUCCESS, "Login za korisnika uspesan");
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
