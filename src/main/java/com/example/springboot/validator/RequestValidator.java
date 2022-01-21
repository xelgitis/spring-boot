package com.example.springboot.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.springboot.domain.RegistrationRequest;
import com.example.springboot.domain.User;
import com.example.springboot.exeption.GenericResponse;
import com.example.springboot.exeption.VacationAppException;
import com.example.springboot.mapper.UserMapper;

@Component
public class RequestValidator { 
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
    @Autowired
    private UserMapper userMapper;	
	
	public void validate(RegistrationRequest request) {
		logger.debug("Zahtev za validaciju novog korisnika={} ", request);
      
        //maybe add this if user accidentally add spaces within its username and password
        //request.setPassword(request.getPassword().trim());
        //request.setUsername(request.getUsername().trim());
        
        logger.debug("Provera da li korisnik postoji u bazi sa sledecim username-om = {} ", request.getUsername());
        User user = userMapper.findUserByUniqueUsername(request.getUsername());
        if (user != null) {
            throw new VacationAppException("korisnicko ime vec postoji u bazi", GenericResponse.USERNAME_TAKEN);
        }     
        
        logger.debug("Provera da li postoji korisnik u bazi sa istim email-om = {} ", request.getEmail());
        validateEmail(request.getEmail());
        
	}
	
    public void validateEmail(String email) throws VacationAppException {
    	User user = userMapper.findUserByUniqueEmail(email);
        if (user != null) {
            throw new VacationAppException("korisnik sa ovim email-om vec postoji u bazi", GenericResponse.EMAIL_TAKEN);
        }
    }

}
