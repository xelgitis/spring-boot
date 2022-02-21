package com.example.springboot.service;

import java.util.List;

import com.example.springboot.domain.Vacation;
import com.example.springboot.domain.VacationResponse;

public interface VacationService {
	
	VacationResponse createVacation(Vacation vacation);
	
	List <Vacation> getVacation(String username); 

	void updateVacation(Vacation vacation);
	
	void approveVacation(Vacation vacation);

	void deleteVacation(String username);
	
	void deleteVacationById(String username, Long id);
}
