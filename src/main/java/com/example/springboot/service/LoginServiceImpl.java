package com.example.springboot.service;

import java.util.HashMap;
import java.util.UUID;

import com.example.springboot.domain.Role;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.domain.LoginRequest;
import com.example.springboot.domain.LoginResponse;
import com.example.springboot.domain.User;
import com.example.springboot.exeption.Status;
import com.example.springboot.exeption.VacationAppException;
import com.example.springboot.mapper.RoleMapper;
import com.example.springboot.mapper.UserMapper;

@Service
public class LoginServiceImpl implements LoginService {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	HashMap<String, User> loggedUsers;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private RoleMapper roleMapper;	
	
	@Autowired 
	private PasswordGeneratorService passwordGeneratorService;
	
	@Autowired 
	private UserRoleService userRoleService;	
	
	public LoginResponse login(LoginRequest request) {
		logger.info("Provera username/pass za korisnika: {}", request.getUsername());
		
		User user = userMapper.findUser(request.getUsername())
				    .orElseThrow(() -> new VacationAppException(Status.USER_NOT_FOUND));
		
		user.setPassword(request.getPassword());		

		passwordGeneratorService.checkPassword(user);
		
		Long roleId = userRoleService.getUserRole(user.getId()).getRoleId();
		logger.debug("roleId: {}", roleId);
		
		Role role   = roleMapper.getRolebyId(roleId);
		logger.debug("roleId: {}", role.getRole());
		
		user.setRole(role);
		logger.debug("Dohvacen user: {}", user.toString());

		String sessionId = UUID.randomUUID().toString();
		if(loggedUsers == null) loggedUsers = new HashMap<>();
		loggedUsers.put(sessionId, user);
		
		return new LoginResponse(user.getUsername(), sessionId, Status.SUCCESS, "Login za korisnika uspesan");
	}	
	
	@Override
	public HashMap<String, User> getLoggedUsers() {
		return loggedUsers;
	}

	@Override
	public String getUsername(String sessionId) {		
		return loggedUsers.get(sessionId).getUsername();
	}

	@Override
	public Role getRole(String sessionId) {
		Role role = loggedUsers.get(sessionId).getRole();
        return role;
	}

}
