package com.example.springboot.service;

import com.example.springboot.domain.User;

public interface PasswordGeneratorService {
	
	String provideHashedPassword(String password, String salt);

	String createPasswordSalt();

	void checkPassword(User user);	

}
