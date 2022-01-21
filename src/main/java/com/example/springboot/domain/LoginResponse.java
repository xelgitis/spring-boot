package com.example.springboot.domain;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
	
	public enum Status {
		SUCCESS,
		ERROR,
		WRONG_FORMAT_DATA,
		WRONG_PASSWORD
	}

	private String username;	
	private String sessionId;	
	private Status status;
	private String message;
	
}
