package com.example.springboot.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.domain.Vacation;
import com.example.springboot.domain.VacationRequest;
import com.example.springboot.domain.VacationResponse;
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
		logger.debug("Zahtev za pregled odmora usera = {} ", username);
		if (vacationMapper.findVacationByUniqueUsername(username) != null) {
			return vacationMapper.findVacationByUniqueUsername(username);	
		} else {
			logger.info("Odmor za usera = {} ne postoji ", username);
			return new Vacation();
		}
	}	

	@Override
	public VacationResponse updateVacation(Date startDate, int duration, String username) {
		vacationMapper.updateVacationByUniqueUsername(startDate, duration, username);
		return new VacationResponse(VacationResponse.Status.SUCCESS, "Odmor uspesno updejtovan za usera " + username);
	}

	@Override
	public VacationResponse deleteVacation(String username) {
		vacationMapper.deleteVacationByUniqueUsername(username);	
		return new VacationResponse(VacationResponse.Status.SUCCESS, "Odmor uspesno obrisan za usera " + username);
	}
	
    public void createVac(VacationRequest request, String username) {
        Vacation vacation = new Vacation (request.getStartDate(), request.getDuration(), 'N', username);
        
        logger.info("Kreiran je sledeci odmor = {} ", vacation.toString());        
        vacationMapper.create(vacation);
    }

}
