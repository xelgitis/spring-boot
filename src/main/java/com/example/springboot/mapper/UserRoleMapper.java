package com.example.springboot.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.springboot.domain.UserRole;


@Mapper
public interface UserRoleMapper {

	UserRole getUserRole(Long userId);
	
	void createRole(Long userId, Long roleId);
	
	void deleteUserRole(Long id);
	
}
