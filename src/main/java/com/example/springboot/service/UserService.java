package com.example.springboot.service;


import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.springboot.domain.User;
import com.example.springboot.domain.UserRequest;
import org.springframework.http.HttpStatus;

public interface UserService {

    User getUser(User loggedUser, String username);
   
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void updateUser(User loggedUser, String user, UserRequest request);

    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteUser(User loggedUser, String username);
}

