package com.example.springboot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.example.springboot.domain.Vacation;
import com.example.springboot.domain.VacationResponse;
import com.example.springboot.exeption.Status;
import com.example.springboot.exeption.VacationAppException;
import com.example.springboot.mapper.VacationMapper;
import com.example.springboot.validator.Validator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class VacationServiceImpl implements VacationService {
	
	@Autowired
	private UserService userService;	
	
    @Autowired
    private VacationMapper vacationMapper; 
    
	@Autowired
	private Validator validator;    
    
	@Override
	@Transactional
	public VacationResponse createVacation(Vacation vacation) {
		log.debug("Zahtev za kreiranje odmora usera = {} ", vacation.getUsername());
		
		validator.validateVacationData(vacation);
		
		userService.getUser(vacation.getUsername());
				
		vacationMapper.create(vacation);
		return new VacationResponse(Status.SUCCESS, "Uspesno kreiran odmor za usera " + vacation.getUsername());		

	}  
	
	@Override
	@Transactional
	public List <Vacation> getVacation(String username) {		
		
		userService.getUser(username);
		
		if (CollectionUtils.isEmpty(vacationMapper.findVacation(username))) throw new VacationAppException(Status.VACATION_NOT_PRESENT);	
			
		return vacationMapper.findVacation(username);			  	
	}	

	@Override
	@Transactional
	public void updateVacation(Vacation vacation) {
		
		validator.validateVacationData(vacation);
		
		userService.getUser(vacation.getUsername());	
		
		//if (CollectionUtils.isEmpty(vacationMapper.findVacation(vacation.getUsername()))) throw new VacationAppException(Status.VACATION_NOT_PRESENT);
			
		int result = vacationMapper.updateVacation(vacation);		
		if (result == 0) throw new VacationAppException(Status.VACATION_NOT_PRESENT);
	}

	@Override
	@Transactional
	public void deleteVacation(String username) {
		
		userService.getUser(username);	

		vacationMapper.deleteVacation(username);		
	}

	@Override
	public void deleteVacationById(String username, Long id) {
		
		userService.getUser(username);	

		vacationMapper.deleteVacationById(username, id);	
		
	}

	@Override
	public void approveVacation(Vacation vacation) {
		
		userService.getUser(vacation.getUsername());	
		
		int result = vacationMapper.updateVacation(vacation);		
		if (result == 0) throw new VacationAppException(Status.VACATION_NOT_PRESENT);
		
	}
}
