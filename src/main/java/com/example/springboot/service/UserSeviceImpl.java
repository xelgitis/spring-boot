package com.example.springboot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.domain.UserResponse;
import com.example.springboot.exeption.Status;
import com.example.springboot.exeption.VacationAppException;
import com.example.springboot.domain.ResponseStatus;
import com.example.springboot.domain.User;
import com.example.springboot.domain.UserRequest;
import com.example.springboot.mapper.UserMapper;

import java.util.Optional;

@Service
public class UserSeviceImpl implements UserService {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired 
	private UserRoleService userRoleService;
	
    @Autowired
    private UserMapper userMapper;

	@Override
	public User getUser(String username) {
		logger.debug("Zahtev za pregled usera = {} ", username);

		return userMapper.findUser(username)
			              .orElseThrow(() -> new VacationAppException("Korisnik sa username-om =" + username + " ne postoji u bazi", Status.USER_NOT_FOUND));
	}	

	@Override
	public UserResponse updateUser(String username, UserRequest request) {
		
		userMapper.findUser(username)
		.orElseThrow(() -> new VacationAppException("Korisnik sa username-om =" + username + " ne postoji u bazi", Status.USER_NOT_FOUND));
		
		userMapper.updateUser(username, request.getAddress(), request.getName(), request.getEmail());
		return new UserResponse(username, ResponseStatus.SUCCESS, "User uspesno updejtovan");			
	}	

	@Override
	public UserResponse deleteUser(String username) {
		User user = userMapper.findUser(username)
		            .orElseThrow(() -> new VacationAppException("Korisnik sa username-om =" + username + " ne postoji u bazi", Status.USER_NOT_FOUND));
		
    	userRoleService.deleteRole(user.getId());	//first need to remove raw from user_role table
		userMapper.deleteUser(username);
		return new UserResponse(username, ResponseStatus.SUCCESS, "User uspesno obrisan");			
	
	}	

}
