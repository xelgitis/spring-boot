package com.example.springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springboot.domain.Role;
import com.example.springboot.domain.User;
import com.example.springboot.mapper.RoleMapper;
import com.example.springboot.mapper.UserRoleMapper;

@Service
public class UserRoleServiceImpl implements UserRoleService {
	
    @Autowired
    private UserRoleMapper userRoleMapper;
    
    @Autowired
    private RoleMapper roleMapper;

	@Override
	public void deleteRole(Long id) {
		userRoleMapper.deleteUserRole(id);		
	}

	@Override
	@Transactional
	public Role getUserRole(User user) {				
		Long id = userRoleMapper.getUserRole(user.getId()).getRoleId();
		return roleMapper.getRolebyId(id);
	} 

}
