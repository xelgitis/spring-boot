package com.example.springboot.domain;


import com.example.springboot.exeption.Status;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
	
	private String username;	
	private String sessionId;	
	private Status status;
	private String message;
	
}
