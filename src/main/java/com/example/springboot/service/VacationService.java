package com.example.springboot.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.springboot.domain.User;
import com.example.springboot.domain.Vacation;
import com.example.springboot.domain.VacationRequest;
import com.example.springboot.domain.VacationResponse;

public interface VacationService {
	
	VacationResponse createVacation(User loggedUser, VacationRequest request, String username);
	
	Vacation getVacation(User loggedUser, String username);
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	void updateVacation(User loggedUser, VacationRequest request, String username);
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	void deleteVacation(User loggedUser, String username);

}
