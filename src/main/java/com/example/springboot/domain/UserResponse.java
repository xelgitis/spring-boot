package com.example.springboot.domain;


import com.example.springboot.exeption.Status;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {
	
	String  username;
	Status  status;
	String  message;
	
}
