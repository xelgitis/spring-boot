package com.example.springboot.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserRequest {

	private String address;
	private String name;	
	private String email;
	
}
