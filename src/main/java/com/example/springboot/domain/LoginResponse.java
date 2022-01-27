package com.example.springboot.domain;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
	
	private String username;	
	private String sessionId;	
	private ResponseStatus status;
	private String message;
	
}
