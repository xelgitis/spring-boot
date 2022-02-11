package com.example.springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springboot.exeption.Status;
import com.example.springboot.exeption.VacationAppException;
import com.example.springboot.domain.RegistrationResponse;
import com.example.springboot.domain.Role;
import com.example.springboot.domain.User;
import com.example.springboot.mapper.RoleMapper;
import com.example.springboot.mapper.UserMapper;
import com.example.springboot.mapper.UserRoleMapper;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class UserSeviceImpl implements UserService {
    
	@Autowired 
	private PasswordGeneratorService passwordGeneratorService;	
	
	@Autowired 
	private UserRoleService userRoleService;
	
	@Autowired
	private VacationService vacationService;
	
    @Autowired
    private RoleMapper roleMapper;	    
    
    @Autowired
    private UserRoleMapper userRoleMapper;    
	
    @Autowired
    private UserMapper userMapper;    

	@Override
	@Transactional
	public RegistrationResponse registerUser(User user) {

		userMapper.create(user);
		Long roleId = roleMapper.getRole(user.getRole().getName()).getId();
        log.debug("roleId = {} ", roleId);	
        
        userRoleMapper.createRole(user.getId(), roleId);		
		
		return new RegistrationResponse(user.getUsername(), Status.SUCCESS, "Registracija uspesna");
	}	

	@Override
	public User getUser(String username) {
		log.debug("Zahtev za pregled usera = {} ", username);

		return userMapper.findUser(username)
		       .orElseThrow(() -> new VacationAppException(Status.USER_NOT_FOUND));		
	}	

	@Override
	public void updateUser(User userForUpdate) {		
	
		userMapper.findUser(userForUpdate.getUsername())
	    .orElseThrow(() -> new VacationAppException(Status.USER_NOT_FOUND));
		
		userMapper.updateUser(userForUpdate.getUsername(), userForUpdate.getAddress(), userForUpdate.getName(), userForUpdate.getEmail());
	}	

	@Override
	@Transactional
	public void deleteUser(String username) {
		
		User user = userMapper.findUser(username)
	                .orElseThrow(() -> new VacationAppException(Status.USER_NOT_FOUND));		

		vacationService.deleteVacation(username);		
		userRoleService.deleteRole(user.getId()); //first need to remove raw from user_role table
		userMapper.deleteUser(username);
	}		

	@Override
	@Transactional
	public User findUser(String username, String password) {
		
		User user = userMapper.findUser(username)
					.orElseThrow(() -> new VacationAppException(Status.USER_NOT_FOUND));
		
		user.setPassword(password);
		
		passwordGeneratorService.checkPassword(user);
		
		Role role = userRoleService.getUserRole(user);
		
		user.setRole(role);
		log.debug("Dohvacen user: {}", user.toString());		
		
		return user;
	}

}
