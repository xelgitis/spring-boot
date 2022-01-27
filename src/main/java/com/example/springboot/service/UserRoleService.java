package com.example.springboot.service;

import com.example.springboot.domain.UserRole;

public interface UserRoleService {
	
	UserRole getUserRole(Long id);
	
	void deleteRole(Long id);

}
