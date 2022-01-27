package com.example.springboot.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegistrationResponse {
	
	String          username;
	ResponseStatus  status;
	String          message;
	
}
