package com.example.springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.exeption.Status;
import com.example.springboot.exeption.VacationAppException;
import com.example.springboot.domain.Role;
import com.example.springboot.domain.User;
import com.example.springboot.domain.UserRequest;
import com.example.springboot.mapper.UserMapper;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class UserSeviceImpl implements UserService {
	
	@Autowired 
	private UserRoleService userRoleService;
	
    @Autowired
    private UserMapper userMapper;

	@Override
	public User getUser(User loggedUser, String username) {
		log.debug("Zahtev za pregled usera = {} ", username);
		
		String role = loggedUser.getRole().getRole();
		
		if (Role.isAdmin(role)) {
			return userMapper.findUser(username)
		           .orElseThrow(() -> new VacationAppException(Status.USER_NOT_FOUND));
		} else {
			checkRequieredData(loggedUser.getUsername(), username);
			return userMapper.findUser(username)
		           .orElseThrow(() -> new VacationAppException(Status.USER_NOT_FOUND));
		}
	}	

	@Override
	public void updateUser(User loggedUser, String user, UserRequest request) {
		String role = loggedUser.getRole().getRole();		
		
		if (Role.isAdmin(role)) {
			   userMapper.findUser(user)
		       .orElseThrow(() -> new VacationAppException(Status.USER_NOT_FOUND));
			   
			   userMapper.updateUser(user, request.getAddress(), request.getName(), request.getEmail());			   
		} else {
			checkRequieredData(loggedUser.getUsername(), user);
			userMapper.findUser(user)
		    .orElseThrow(() -> new VacationAppException(Status.USER_NOT_FOUND));
			
			userMapper.updateUser(user, request.getAddress(), request.getName(), request.getEmail());
		}		
	}	

	@Override
	public void deleteUser(User loggedUser, String username) {
		String role = loggedUser.getRole().getRole();

		if (Role.isAdmin(role)) {
			User user = userMapper.findUser(username)
            .orElseThrow(() -> new VacationAppException(Status.USER_NOT_FOUND));
			
			userRoleService.deleteRole(user.getId());	//first need to remove raw from user_role table
			userMapper.deleteUser(username);
		} else {
			checkRequieredData(loggedUser.getUsername(), username);
			User user = userMapper.findUser(username)
		                .orElseThrow(() -> new VacationAppException(Status.USER_NOT_FOUND));
					
			userRoleService.deleteRole(user.getId());	//first need to remove raw from user_role table
			userMapper.deleteUser(username);			
		}
	}		
	
	public void checkRequieredData(String username, String user) {
		if (!username.contentEquals(user)) {
			log.info("Nije dozvoljeno regular korisniku da gleda-azurira-brise podatke za drugog korisnika");
			throw new VacationAppException(Status.GENERIC_ERROR);
		}		
	}	

}
