package com.example.springboot.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.springboot.domain.Role;

@Mapper
public interface RoleMapper {
	
	Role   getRole(String role);
	
	Role   getRolebyId(Long id);

}
