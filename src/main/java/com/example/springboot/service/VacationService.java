package com.example.springboot.service;

import java.util.List;

import com.example.springboot.domain.Vacation;
import com.example.springboot.domain.VacationResponse;

public interface VacationService {
	
	VacationResponse createVacation(Vacation vacation);
	
	List <Vacation> getVacation(String username); 

	void updateVacation(Vacation vacation);

	void deleteVacation(String username);
}
