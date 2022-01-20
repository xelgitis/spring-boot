package com.example.springboot.service;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.creator.UserCreator;
import com.example.springboot.domain.UserRequest;
import com.example.springboot.domain.UserResponse;
import com.example.springboot.domain.User;
import com.example.springboot.mapper.UserMapper;

@Service
public class UserSeviceImpl implements UserService {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
    @Autowired
    private UserMapper userMapper;

	@Override
	public User getUser(String username) {
		return userMapper.findUserByUniqueUsername(username);
	}	

	@Override
	public UserResponse updateUser(String username, String address, String name, String email) {
		logger.debug("Update usera sa sl parametrima: username = {}, address = {}, name = {}, email = {} ", username, address, name, email);
		userMapper.updateUser(username, address, name, email);	
		//TODO: check how to solve this, when user does not exist
		return new UserResponse(username, UserResponse.Status.SUCCESSFUL, "User uspesno updejtovan");
	}	

	@Override
	public UserResponse deleteUser(String username) {
		userMapper.deleteByUsername(username);
		//TODO: check how to solve this, when user does not exist
		return new UserResponse(username, UserResponse.Status.SUCCESSFUL, "User uspesno obrisan");
	}	


}
