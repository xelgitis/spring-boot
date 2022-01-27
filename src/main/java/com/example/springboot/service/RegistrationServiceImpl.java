package com.example.springboot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.creator.UserCreator;
import com.example.springboot.domain.RegistrationRequest;
import com.example.springboot.domain.RegistrationResponse;
import com.example.springboot.domain.ResponseStatus;
import com.example.springboot.domain.Role;
import com.example.springboot.domain.User;
import com.example.springboot.mapper.RoleMapper;
import com.example.springboot.mapper.UserMapper;
import com.example.springboot.mapper.UserRoleMapper;

@Service
public class RegistrationServiceImpl implements RegistrationService {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
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
		return new RegistrationResponse(request.getUsername(), ResponseStatus.SUCCESS, "Registracija uspesna");
	}
	
    public void registerUser(RegistrationRequest request) {
        User user = userCreator.create(request);
        
        logger.debug("Kreiran je sledeci user = {} ", user.toString());        
        userMapper.create(user);
    }  
    
    public void registerUserRole(RegistrationRequest request) {
        User user = userMapper.findUser(request.getUsername());
        Role role = new Role();
        role.setRole(request.getRole());
        
        logger.debug("Dohvacen je sledeci user = {} ", user.toString());    
 
        Long roleId = roleMapper.getRole(request.getRole()).getId();
        logger.debug("roleId = {} ", roleId);
        
        userRoleMapper.createRole(user.getId(), roleId);
    }     
 

}
