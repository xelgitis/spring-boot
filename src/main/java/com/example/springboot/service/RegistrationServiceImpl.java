package com.example.springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.creator.UserCreator;
import com.example.springboot.domain.RegistrationRequest;
import com.example.springboot.domain.RegistrationResponse;
import com.example.springboot.domain.Role;
import com.example.springboot.domain.User;
import com.example.springboot.exeption.Status;
import com.example.springboot.exeption.VacationAppException;
import com.example.springboot.mapper.RoleMapper;
import com.example.springboot.mapper.UserMapper;
import com.example.springboot.mapper.UserRoleMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RegistrationServiceImpl implements RegistrationService {
	
    @Autowired
    private UserMapper userMapper;	
    
    @Autowired
    private RoleMapper roleMapper;	    
    
    @Autowired
    private UserRoleMapper userRoleMapper;    

    @Autowired
    protected UserCreator userCreator;	

	@Override
	public RegistrationResponse register(RegistrationRequest request) {
		registerUser(request);
		registerUserRole(request);
		return new RegistrationResponse(request.getUsername(), Status.SUCCESS, "Registracija uspesna");
	}
	
    public void registerUser(RegistrationRequest request) {
        User user = userCreator.create(request);
        
        log.debug("Kreiran je sledeci user = {} ", user.toString());        
        userMapper.create(user);
    }  
    
    public void registerUserRole(RegistrationRequest request) {
    	
    	User user =  userMapper.findUser(request.getUsername())
    			     .orElseThrow(() -> new VacationAppException(Status.USER_NOT_FOUND));
        
    	Role role = new Role();
        role.setRole(request.getRole());
        
        log.debug("Dohvacen je sledeci user = {} ", user.toString());    
 
        Long roleId = roleMapper.getRole(request.getRole()).getId();
        log.debug("roleId = {} ", roleId);
        
        userRoleMapper.createRole(user.getId(), roleId);
    }     
 

}
