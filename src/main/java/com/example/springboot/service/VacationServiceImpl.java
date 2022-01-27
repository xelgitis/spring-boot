package com.example.springboot.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.domain.ResponseStatus;
import com.example.springboot.domain.Vacation;
import com.example.springboot.domain.VacationRequest;
import com.example.springboot.domain.VacationResponse;
import com.example.springboot.exeption.GenericResponse;
import com.example.springboot.exeption.VacationAppException;
import com.example.springboot.mapper.VacationMapper;

@Service
public class VacationServiceImpl implements VacationService {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
    @Autowired
    private VacationMapper vacationMapper; 
    
	@Override
	public VacationResponse createVacation(VacationRequest request, String username) {
		logger.info("Zahtev za kreiranje odmora: {} usera = {} ", request.toString(), username);
		createVac(request, username);
		return new VacationResponse(ResponseStatus.SUCCESS, "Uspesno kreiran odmor za usera " + username);
	}  
	
	@Override
	public Vacation getVacation(String username) {
		Vacation vacation = vacationMapper.findVacation(username);
		if (vacation != null) {
			return vacation;	
		} else {
			throw new VacationAppException("Odmor za korisnika sa username-om =" + username + " ne postoji u bazi", GenericResponse.GENERIC_ERROR);
		}
	}	

	@Override
	public VacationResponse updateVacation(VacationRequest request, String username) {
		Vacation vacation = vacationMapper.findVacation(username);
		if (vacation != null) {
			vacationMapper.updateVacation(request.getStartDate(), request.getDuration(), username);
			return new VacationResponse(ResponseStatus.SUCCESS, "Odmor uspesno updejtovan za usera " + username);
		} else {
			throw new VacationAppException("Odmor za korisnika sa username-om =" + username + " ne postoji u bazi", GenericResponse.GENERIC_ERROR);
		}
	}

	@Override
	public VacationResponse deleteVacation(String username) {
		Vacation vacation = vacationMapper.findVacation(username);
		if (vacation != null) {
			vacationMapper.deleteVacation(username);	
			return new VacationResponse(ResponseStatus.SUCCESS, "Odmor uspesno obrisan za usera " + username);
		} else {
			throw new VacationAppException("Odmor za korisnika sa username-om =" + username + " ne postoji u bazi", GenericResponse.GENERIC_ERROR);
		}
	}
	
    public void createVac(VacationRequest request, String username) {
    	Vacation vacation = Vacation.builder().startDate(request.getStartDate()).duration(request.getDuration()).approval('N').username(username).build();
    	vacationMapper.create(vacation);  
    }

}
