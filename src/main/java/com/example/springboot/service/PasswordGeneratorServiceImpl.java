package com.example.springboot.service;

import java.util.Random;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import com.example.springboot.domain.User;
import com.example.springboot.exeption.LoginException;

@Service
public class PasswordGeneratorServiceImpl implements PasswordGeneratorService {
	
	private static final String SALTCHARS_TEMP_PASSWORD = "1234567890";
	private static final String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	private Random random = new Random();	

	@Override
	public String provideHashedPassword(String password, String passwordSalt) {
		return DigestUtils.sha1Hex(password + passwordSalt);
	}

	@Override
	public String createPasswordSalt() {
		StringBuilder salt = new StringBuilder();
		while (salt.length() < 32) {
			int index = (int) (random.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		return salt.toString();
	}

	@Override
	public String generateTemporaryPassword() {
		StringBuilder password = new StringBuilder();
		while(password.length() < 8) {
			int index = (int) (random.nextFloat() * SALTCHARS_TEMP_PASSWORD.length());
			password.append(SALTCHARS_TEMP_PASSWORD.charAt(index));
		}
		return password.toString();
	}

	@Override
	public void checkPassword(User user) {
		if (!provideHashedPassword(user.getPassword(), user.getPasswordSalt()).equals(user.getHashedPassword())) {
			throw new LoginException("Uneta pogresna sifra za korisnika ciji je username=" + user.getUsername());
		}		
	}

}
