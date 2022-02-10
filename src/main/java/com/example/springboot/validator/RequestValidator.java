package com.example.springboot.validator;


import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.example.springboot.domain.RegistrationRequest;
import com.example.springboot.domain.Role;
import com.example.springboot.domain.User;
import com.example.springboot.domain.Vacation;
import com.example.springboot.exeption.Status;
import com.example.springboot.exeption.VacationAppException;
import com.example.springboot.mapper.UserMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RequestValidator { 
	
    @Autowired
    private UserMapper userMapper;	
	
	public void validate(RegistrationRequest request) {
		log.info("Zahtev za validaciju novog korisnika={} ", request);
      
        request.setPassword(request.getPassword().trim());
        request.setUsername(request.getUsername().trim());
        
        log.debug("Provera da li korisnik postoji u bazi sa sledecim username-om = {} ", request.getUsername());    
        
        userMapper.findUser(request.getUsername()).ifPresent(u -> {
        	throw new VacationAppException(Status.USERNAME_TAKEN);
        });       
  
        log.debug("Provera da li postoji korisnik u bazi sa istim email-om = {} ", request.getEmail());
        validateEmail(request.getEmail());
        
	}
	
    public void validateEmail(String email) throws VacationAppException {    	
        userMapper.findUserByEmail(email).ifPresent(u -> {
        	throw new VacationAppException(Status.EMAIL_TAKEN);
        });
 
    }
    	
	public void validatePrivilages(User loggedUser, String user) {
		
		String role = loggedUser.getRole().getName();		
		if (!Role.isAdmin(role)) {		
			if (!loggedUser.getUsername().contentEquals(user)) {
				log.info("Nije dozvoljeno regular korisniku da gleda-azurira-brise podatke za drugog korisnika");
				throw new VacationAppException(Status.USER_DOES_NOT_HAVE_PRIVLEGES);				
			}
		}
	}
	
	public void validateVacationData(Vacation vacation) {
		
		Date current = new Date();
		
		if (vacation.getStartDate().before(current)) throw new VacationAppException(Status.VACATION_DATE_IN_THE_PAST);
		if (vacation.getDuration() <= 0) throw new VacationAppException(Status.INVALID_LENGTH);

	}
	
}
