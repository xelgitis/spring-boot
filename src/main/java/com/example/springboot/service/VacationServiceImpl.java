package com.example.springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.domain.Vacation;
import com.example.springboot.domain.VacationRequest;
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
	public VacationResponse createVacation(VacationRequest request, String username) {
		log.debug("Zahtev za kreiranje odmora: {} usera = {} ", request.toString(), username);
		
		userMapper.findUser(username)
		.orElseThrow(() -> new VacationAppException(Status.USER_NOT_FOUND));
		
		createVac(request, username);
		return new VacationResponse(Status.SUCCESS, "Uspesno kreiran odmor za usera " + username);
	}  
	
	@Override
	public Vacation getVacation(String username) {
		
		userMapper.findUser(username)
		.orElseThrow(() -> new VacationAppException(Status.USER_NOT_FOUND));
		
		return vacationMapper.findVacation(username)
				.orElseThrow(() -> new VacationAppException(Status.VACATION_NOT_PRESENT));
	}	

	@Override
	public VacationResponse updateVacation(VacationRequest request, String username) {
		
		userMapper.findUser(username)
		.orElseThrow(() -> new VacationAppException(Status.USER_NOT_FOUND));
		
		vacationMapper.findVacation(username)
				       .orElseThrow(() -> new VacationAppException(Status.VACATION_NOT_PRESENT));
				
    	vacationMapper.updateVacation(request.getStartDate(), request.getDuration(), username);
		return new VacationResponse(Status.SUCCESS, "Odmor uspesno updejtovan za usera " + username);
	}

	@Override
	public VacationResponse deleteVacation(String username) {
		
		userMapper.findUser(username)
		.orElseThrow(() -> new VacationAppException(Status.USER_NOT_FOUND));
		
		vacationMapper.findVacation(username)
	    .orElseThrow(() -> new VacationAppException(Status.VACATION_NOT_PRESENT));

		vacationMapper.deleteVacation(username);	
		return new VacationResponse(Status.SUCCESS, "Odmor uspesno obrisan za usera " + username);
	}
	
    public void createVac(VacationRequest request, String username) {
    	Vacation vacation = Vacation.builder()
    			           .startDate(request.getStartDate())
    			           .duration(request.getDuration())
    			           .approval('N')
    			           .username(username)
    			           .build();
    	
    	vacationMapper.create(vacation);  
    }

}
