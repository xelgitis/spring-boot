package com.example.springbooot.schedulers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.example.springboot.service.LoginService;

public class SessionScheduler {
	
	@Autowired
	LoginService   loginService;
	
	@Scheduled
	public void checkSessions() {
		loginService.expireSessions();
	}		
    
}
