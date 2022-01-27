package com.example.springboot.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role {
	
	private Long id;
	private String role;	
	
	public boolean isAdmin(String role) {
		
		if (role.equalsIgnoreCase("administrator")) return true;
		else return false;	
		
	}
}
