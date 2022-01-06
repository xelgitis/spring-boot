package com.example.springboot.service;

import com.example.springboot.domain.RegistrationRequest;
import com.example.springboot.domain.RegistrationResponse;

public interface RegistrationService {
	
    RegistrationResponse register(RegistrationRequest request);

}
