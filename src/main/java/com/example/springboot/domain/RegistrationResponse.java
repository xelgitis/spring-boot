package com.example.springboot.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegistrationResponse {
	
	public enum Status {
		SUCCESSFUL,
		ERROR
	}	
	
	String  username;
	Status  status;
	String  message;
	
}
