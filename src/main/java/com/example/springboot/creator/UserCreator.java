package com.example.springboot.creator;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.springboot.domain.RegistrationRequest;
import com.example.springboot.domain.User;
import com.example.springboot.service.PasswordGeneratorService;

import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class UserCreator {
	
	@Autowired
	private PasswordGeneratorService passwordGenerator;
	
	public User create(RegistrationRequest request) {

		log.debug("Zahtev za kreiranje novog korisnika={} ", request);
		
		String passwordSalt   = passwordGenerator.createPasswordSalt();
		String hashedPassword = passwordGenerator.provideHashedPassword(request.getPassword(), passwordSalt);
		
		log.debug("Korisnik ima sledeci password_salt={} ", passwordSalt);
		log.debug("Korisnik ima sledeci hashed password={} ", hashedPassword);
		
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
