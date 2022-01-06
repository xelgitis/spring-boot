package com.example.springboot.creator;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.springboot.domain.RegistrationRequest;
import com.example.springboot.domain.User;
import com.example.springboot.service.PasswordGeneratorService;


@Component
public class UserCreator {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private PasswordGeneratorService passwordGenerator;
	
	public User create(RegistrationRequest request) {
		logger.debug("Zahtev za kreiranje novog korisnika={} ", request);
		
		String passwordSalt   = passwordGenerator.createPasswordSalt();
		String hashedPassword = passwordGenerator.provideHashedPassword(request.getPassword(), passwordSalt);
		
		logger.debug("Korisnik ima sledeci password_salt={} ", passwordSalt);
		logger.debug("Korisnik ima sledeci hashed password={} ", hashedPassword);
		
		return new User(request.getUsername(), request.getPassword(), passwordSalt, hashedPassword, request.getAddress(), request.getName(), request.getEmail(), request.getRole(), new Date());
	}	

}
