package com.example.springboot.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.domain.ResponseStatus;
import com.example.springboot.domain.Vacation;
import com.example.springboot.domain.VacationRequest;
import com.example.springboot.domain.VacationResponse;
import com.example.springboot.exeption.Status;
import com.example.springboot.exeption.VacationAppException;
import com.example.springboot.mapper.UserMapper;
import com.example.springboot.mapper.VacationMapper;

@Service
public class VacationServiceImpl implements VacationService {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
    @Autowired
    private UserMapper userMapper;	
	
    @Autowired
    private VacationMapper vacationMapper; 
    
	@Override
	public VacationResponse createVacation(VacationRequest request, String username) {
		logger.debug("Zahtev za kreiranje odmora: {} usera = {} ", request.toString(), username);
		
		userMapper.findUser(username)
		.orElseThrow(() -> new VacationAppException("Korisnik sa username-om =" + username + " ne postoji u bazi", Status.USER_NOT_FOUND));
		
		createVac(request, username);
		return new VacationResponse(ResponseStatus.SUCCESS, "Uspesno kreiran odmor za usera " + username);
	}  
	
	@Override
	public Vacation getVacation(String username) {
		
		return vacationMapper.findVacation(username)
				.orElseThrow(() -> new VacationAppException("Odmor za korisnika sa username-om =" + username + " ne postoji u bazi", Status.GENERIC_ERROR));
	}	

	@Override
	public VacationResponse updateVacation(VacationRequest request, String username) {
		
		vacationMapper.findVacation(username)
				       .orElseThrow(() -> new VacationAppException("Odmor za korisnika sa username-om =" + username + " ne postoji u bazi", Status.GENERIC_ERROR));
				
    	vacationMapper.updateVacation(request.getStartDate(), request.getDuration(), username);
		return new VacationResponse(ResponseStatus.SUCCESS, "Odmor uspesno updejtovan za usera " + username);
	}

	@Override
	public VacationResponse deleteVacation(String username) {
		
		vacationMapper.findVacation(username)
	    .orElseThrow(() -> new VacationAppException("Odmor za korisnika sa username-om =" + username + " ne postoji u bazi", Status.GENERIC_ERROR));

		vacationMapper.deleteVacation(username);	
		return new VacationResponse(ResponseStatus.SUCCESS, "Odmor uspesno obrisan za usera " + username);
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
