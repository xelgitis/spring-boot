package com.example.springboot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.domain.UserResponse;
import com.example.springboot.exeption.GenericResponse;
import com.example.springboot.exeption.VacationAppException;
import com.example.springboot.domain.ResponseStatus;
import com.example.springboot.domain.User;
import com.example.springboot.domain.UserRequest;
import com.example.springboot.mapper.UserMapper;

@Service
public class UserSeviceImpl implements UserService {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired 
	private UserRoleService userRoleService;
	
    @Autowired
    private UserMapper userMapper;

	@Override
	public User getUser(String username) {
		logger.info("Zahtev za pregled usera = {} ", username);
		User user = userMapper.findUser(username);
		if (user != null) return userMapper.findUser(username);
		else throw new VacationAppException("Korisnik sa username-om =" + username + " ne postoji u bazi", GenericResponse.USER_NOT_FOUND);
	}	

	@Override
	public UserResponse updateUser(String username, UserRequest request) {
		User user = userMapper.findUser(username);
		if (user != null) {
			userMapper.updateUser(username, request.getAddress(), request.getName(), request.getEmail());
			return new UserResponse(username, ResponseStatus.SUCCESS, "User uspesno updejtovan");			
		}
		else {
			throw new VacationAppException("Korisnik sa username-om =" + username + " ne postoji u bazi", GenericResponse.USER_NOT_FOUND);
		}
	}	

	@Override
	public UserResponse deleteUser(String username) {
		User user = userMapper.findUser(username);			
		if (user != null) {
			userRoleService.deleteRole(user.getId());	//first need to remove raw from user_role table
			userMapper.deleteUser(username);
			return new UserResponse(username, ResponseStatus.SUCCESS, "User uspesno obrisan");			
		} else {
			throw new VacationAppException("Korisnik sa username-om =" + username + " ne postoji u bazi", GenericResponse.USER_NOT_FOUND);
		}		
	}	

}
