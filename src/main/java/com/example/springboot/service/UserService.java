package com.example.springboot.service;


import com.example.springboot.domain.User;
import com.example.springboot.domain.UserResponse;

public interface UserService {

    User getUser(String username);
    
    UserResponse updateUser(String username, String address, String name, String email);

    UserResponse deleteUser(String username);
}

