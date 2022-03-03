package com.example.springboot.service;

import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.domain.LoginRequest;
import com.example.springboot.domain.LoginResponse;
import com.example.springboot.domain.User;
import com.example.springboot.exeption.Status;
import com.example.springboot.exeption.VacationAppException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {
	
	private final Map<String, User> loggedUsers = new ConcurrentHashMap<>();	
	
	@Autowired 
	private UserService userService;	
	
	public LoginResponse login(LoginRequest request) {
		log.info("Provera username/pass za korisnika: {}", request.getUsername());
		
		User user = userService.findUser(request.getUsername(), request.getPassword());
		
    	String sessionId = UUID.randomUUID().toString();
    	user.setLoginTime(new Date());

		loggedUsers.put(sessionId, user);
		
		expireSessions();
		
		return new LoginResponse(user.getUsername(), sessionId, Status.SUCCESS, "Login za korisnika uspesan");
	}	
	
	@Override
	public Map<String, User> getLoggedUsers() {
		return Collections.unmodifiableMap(loggedUsers);
	}

	@Override
	public User getLoggedUser(String sessionId) {	
		
		if (loggedUsers.get(sessionId) == null) throw new VacationAppException(Status.USER_NOT_LOGGED_IN);		
		return loggedUsers.get(sessionId);
	}

	@Override
	public void expireSessions() {
		
		Date loginTime, currentTime = new Date();
		Iterator<Map.Entry<String, User>> itr = loggedUsers.entrySet().iterator();
		
        while(itr.hasNext())
        {
             Map.Entry<String, User> entry = itr.next();             
             //log.debug("sessionID: {} loginTime: {} " , entry.getKey(), entry.getValue().getLoginTime());
             loginTime = entry.getValue().getLoginTime();
             if (currentTime.getTime() - loginTime.getTime() > 60000) loggedUsers.remove(entry.getKey()); //ako je proslo vise od 60s od kada se korisnik ulogovao
        }
	}

}
