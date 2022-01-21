package com.example.springboot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.creator.UserCreator;
import com.example.springboot.domain.RegistrationRequest;
import com.example.springboot.domain.RegistrationResponse;
import com.example.springboot.domain.User;
import com.example.springboot.mapper.UserMapper;

@Service
public class RegistrationServiceImpl implements RegistrationService {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
    @Autowired
    private UserMapper userMapper;	

    @Autowired
    protected UserCreator userCreator;	

	@Override
	public RegistrationResponse register(RegistrationRequest request) {
		registerUser(request);
		return new RegistrationResponse(request.getUsername(), RegistrationResponse.Status.SUCCESSFUL, "Registracija uspesna");
	}
	
    public void registerUser(RegistrationRequest request) {
        User user = userCreator.create(request);
        
        logger.debug("Kreiran je sledeci user = {} ", user.toString());        
        userMapper.create(user);
    }

}
