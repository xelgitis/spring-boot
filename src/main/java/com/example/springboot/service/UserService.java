package com.example.springboot.service;


import com.example.springboot.domain.User;
import com.example.springboot.domain.UserRequest;
import com.example.springboot.domain.UserResponse;

public interface UserService {

    User getUser(String username);
    
    UserResponse updateUser(String username, UserRequest request);

    UserResponse deleteUser(String username);
}

