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
import com.example.springboot.mapper.UserMapper;
import com.example.springboot.mapper.VacationMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class VacationServiceImpl implements VacationService {
	
    @Autowired
    private UserMapper userMapper;	
	
    @Autowired
    private VacationMapper vacationMapper; 
    
	@Override
	@Transactional
	public VacationResponse createVacation(Vacation vacation) {
		log.debug("Zahtev za kreiranje odmora: {} usera = {} ", vacation.getUsername());
		
		userMapper.findUser(vacation.getUsername())
		.orElseThrow(() -> new VacationAppException(Status.USER_NOT_FOUND));		
				
		vacationMapper.create(vacation);
		return new VacationResponse(Status.SUCCESS, "Uspesno kreiran odmor za usera " + vacation.getUsername());		

	}  
	
	@Override
	@Transactional
	public List <Vacation> getVacation(String username) {		
		
		userMapper.findUser(username)
		.orElseThrow(() -> new VacationAppException(Status.USER_NOT_FOUND));
		
		if (CollectionUtils.isEmpty(vacationMapper.findVacation(username))) throw new VacationAppException(Status.VACATION_NOT_PRESENT);	
			
		return vacationMapper.findVacation(username);			  	
	}	

	@Override
	@Transactional
	public void updateVacation(Vacation vacation) {
		
		userMapper.findUser(vacation.getUsername())
		.orElseThrow(() -> new VacationAppException(Status.USER_NOT_FOUND));	
		
		if (CollectionUtils.isEmpty(vacationMapper.findVacation(vacation.getUsername()))) throw new VacationAppException(Status.VACATION_NOT_PRESENT);
			
		vacationMapper.updateVacation(vacation);		
	}

	@Override
	@Transactional
	public void deleteVacation(String username) {
		
		userMapper.findUser(username)
		.orElseThrow(() -> new VacationAppException(Status.USER_NOT_FOUND));	
		
		if (CollectionUtils.isEmpty(vacationMapper.findVacation(username))) throw new VacationAppException(Status.VACATION_NOT_PRESENT);

		vacationMapper.deleteVacation(username);		
	}
}
