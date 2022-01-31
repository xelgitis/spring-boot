package com.example.springboot.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.example.springboot.domain.User;
import com.example.springboot.domain.Vacation;

@Mapper
public interface UserMapper {
	
	//create new user
	void create(User user);
	
	//find user by username
	Optional <User> findUser(String username);	
	
	//update user 
	void updateUser(String username, String address, String name, String email);
	
	//delete user by username
	void deleteUser(String username);	
	
	//check in the db exist user with this email
	Optional <User> findUserByEmail(String username);
	
	//return all users from db
	List<User> findAllUsers();
}
