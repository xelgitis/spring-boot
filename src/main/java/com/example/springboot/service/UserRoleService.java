package com.example.springboot.service;

import com.example.springboot.domain.Role;
import com.example.springboot.domain.User;

public interface UserRoleService {
	
	Role getUserRole(User user);
	
	void deleteRole(Long id);

}
