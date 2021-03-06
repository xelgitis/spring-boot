package com.example.springboot.validator;

import java.util.Date;
import org.springframework.stereotype.Component;
import com.example.springboot.domain.Role;
import com.example.springboot.domain.User;
import com.example.springboot.domain.Vacation;
import com.example.springboot.exeption.Status;
import com.example.springboot.exeption.VacationAppException;


import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class Validator { 
    	
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
	
	public boolean isAdmin(User loggedUser) {
		
		String role = loggedUser.getRole().getName();		
		return (Role.isAdmin(role));
	}
	
}
