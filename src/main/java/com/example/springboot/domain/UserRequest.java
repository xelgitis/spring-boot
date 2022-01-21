package com.example.springboot.domain;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRequest {

	private String address;
	private String name;	
	private String email;
	
}
