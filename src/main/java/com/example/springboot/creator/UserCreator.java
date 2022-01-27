package com.example.springboot.creator;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.springboot.domain.RegistrationRequest;
import com.example.springboot.domain.User;
import com.example.springboot.service.PasswordGeneratorService;

import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
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
		
		return User.builder()
				    .username(request.getUsername())
				    .password(request.getPassword())
				    .passwordSalt(passwordSalt)
				    .hashedPassword(hashedPassword)
				    .address(request.getAddress())
				    .name(request.getName())
				    .email(request.getEmail())
				    .registrationTime(new Date()).build();
	}	

}
