package com.example.springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.domain.UserRole;
import com.example.springboot.mapper.UserRoleMapper;

@Service
public class UserRoleServiceImpl implements UserRoleService {
	
    @Autowired
    private UserRoleMapper userRoleMapper;

	@Override
	public void deleteRole(Long id) {
		userRoleMapper.deleteUserRole(id);		
	}

	@Override
	public UserRole getUserRole(Long id) {
		return userRoleMapper.getUserRole(id);
	} 

}
