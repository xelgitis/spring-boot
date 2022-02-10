package com.example.springboot.service;


import com.example.springboot.domain.RegistrationResponse;
import com.example.springboot.domain.User;

public interface UserService {
	
	RegistrationResponse registerUser(User user);

    User getUser(String username);
    
    User findUser(String username, String password);   

    void updateUser(User userForUpdate);

    void deleteUser(String username);
}

