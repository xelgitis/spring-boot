package com.example.springboot.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.springboot.domain.User;

@Mapper
public interface UserMapper {
	
	//create new user
	void create(User user);
	
	//check in the db if user with this username exist 
	User findUserByUniqueUsername(String username);
	
	//check in the db exist user with this email
	User findUserByUniqueEmail(String username);
	
	//return all users from db
	List<User> findAllUsers();
	
	//update user 
	void updateUser(String username, String address, String name, String email);	
	
	//delete user by username
	void deleteByUsername(String username);
	
	//find user password by username
	User findPasswordDataByUsername(String username);

}
