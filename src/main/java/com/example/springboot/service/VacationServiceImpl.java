package com.example.springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.domain.Role;
import com.example.springboot.domain.User;
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
	public VacationResponse createVacation(User loggedUser, VacationRequest request, String username) {
		log.debug("Zahtev za kreiranje odmora: {} usera = {} ", request.toString(), username);
		String role = loggedUser.getRole().getRole();
		
		if (Role.isAdmin(role)) {
			userMapper.findUser(username)
			.orElseThrow(() -> new VacationAppException(Status.USER_NOT_FOUND));
		
			createVac(request, username);
			return new VacationResponse(Status.SUCCESS, "Uspesno kreiran odmor za usera " + username);
		} else {
			checkRequieredData(loggedUser.getUsername(), username);
			
			userMapper.findUser(username)
			.orElseThrow(() -> new VacationAppException(Status.USER_NOT_FOUND));
		
			createVac(request, username);
			return new VacationResponse(Status.SUCCESS, "Uspesno kreiran odmor za usera " + username);			
		}
	}  
	
	@Override
	public Vacation getVacation(User loggedUser, String username) {		
		
		String role = loggedUser.getRole().getRole();
		
		if (Role.isAdmin(role)) {			
			userMapper.findUser(username)
			.orElseThrow(() -> new VacationAppException(Status.USER_NOT_FOUND));
			
			return vacationMapper.findVacation(username)
				   .orElseThrow(() -> new VacationAppException(Status.VACATION_NOT_PRESENT));
		} else {	
			checkRequieredData(loggedUser.getUsername(), username);
			
			userMapper.findUser(username)
			.orElseThrow(() -> new VacationAppException(Status.USER_NOT_FOUND));
			
			return vacationMapper.findVacation(username)
				   .orElseThrow(() -> new VacationAppException(Status.VACATION_NOT_PRESENT));
		}
	}	

	@Override
	public void updateVacation(User loggedUser, VacationRequest request, String username) {
		
		String role = loggedUser.getRole().getRole();
		
		if (Role.isAdmin(role)) {
			userMapper.findUser(username)
			.orElseThrow(() -> new VacationAppException(Status.USER_NOT_FOUND));
		
			vacationMapper.findVacation(username)
			.orElseThrow(() -> new VacationAppException(Status.VACATION_NOT_PRESENT));
				
			vacationMapper.updateVacation(request.getStartDate(), request.getDuration(), username);
		} else {			
			checkRequieredData(loggedUser.getUsername(), username);
			
			userMapper.findUser(username)
			.orElseThrow(() -> new VacationAppException(Status.USER_NOT_FOUND));
		
			vacationMapper.findVacation(username)
			.orElseThrow(() -> new VacationAppException(Status.VACATION_NOT_PRESENT));
				
			vacationMapper.updateVacation(request.getStartDate(), request.getDuration(), username);			
		}
	}

	@Override
	public void deleteVacation(User loggedUser, String username) {
		String role = loggedUser.getRole().getRole();
		
		if (Role.isAdmin(role)) {
			userMapper.findUser(username)
			.orElseThrow(() -> new VacationAppException(Status.USER_NOT_FOUND));
		
			vacationMapper.findVacation(username)
			.orElseThrow(() -> new VacationAppException(Status.VACATION_NOT_PRESENT));

			vacationMapper.deleteVacation(username);
		} else {
			checkRequieredData(loggedUser.getUsername(), username);
			
			userMapper.findUser(username)
			.orElseThrow(() -> new VacationAppException(Status.USER_NOT_FOUND));
		
			vacationMapper.findVacation(username)
			.orElseThrow(() -> new VacationAppException(Status.VACATION_NOT_PRESENT));

			vacationMapper.deleteVacation(username);			
		}
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
    
	public void checkRequieredData(String username, String user) {
		if (!username.contentEquals(user)) {
			log.info("Nije dozvoljeno regular korisniku da gleda-azurira-brise podatke za drugog korisnika");
			throw new VacationAppException(Status.GENERIC_ERROR);
		}		
	}    

}
