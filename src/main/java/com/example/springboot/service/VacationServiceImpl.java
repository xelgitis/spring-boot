package com.example.springboot.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
		return new VacationResponse(VacationResponse.Status.SUCCESS, "Uspesno kreiran odmor za usera " + username);
	}  
	
	@Override
	public Vacation getVacation(String username) {
		if (vacationMapper.findVacationByUniqueUsername(username) != null) {
			return vacationMapper.findVacationByUniqueUsername(username);	
		} else {
			throw new VacationAppException("Odmor za korisnika sa username-om =" + username + " ne postoji u bazi", GenericResponse.GENERIC_ERROR);
		}
	}	

	@Override
	public VacationResponse updateVacation(Date startDate, int duration, String username) {
		if (vacationMapper.findVacationByUniqueUsername(username) != null) {
			vacationMapper.updateVacationByUniqueUsername(startDate, duration, username);
			return new VacationResponse(VacationResponse.Status.SUCCESS, "Odmor uspesno updejtovan za usera " + username);
		} else {
			throw new VacationAppException("Odmor za korisnika sa username-om =" + username + " ne postoji u bazi", GenericResponse.GENERIC_ERROR);
		}
	}

	@Override
	public VacationResponse deleteVacation(String username) {
		if (vacationMapper.findVacationByUniqueUsername(username) != null) {
			vacationMapper.deleteVacationByUniqueUsername(username);	
			return new VacationResponse(VacationResponse.Status.SUCCESS, "Odmor uspesno obrisan za usera " + username);
		} else {
			throw new VacationAppException("Odmor za korisnika sa username-om =" + username + " ne postoji u bazi", GenericResponse.GENERIC_ERROR);
		}
	}
	
    public void createVac(VacationRequest request, String username) {
        Vacation vacation = new Vacation (null, request.getStartDate(), request.getDuration(), 'N', username);        
        vacationMapper.create(vacation);
    }

}
