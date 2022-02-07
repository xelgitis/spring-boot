package com.example.springboot.exeption;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

	@ExceptionHandler(VacationAppException.class)
	public ResponseEntity<Object> handleException(VacationAppException ex, WebRequest request) {
		Map<String, Object> body = new LinkedHashMap<>();
		
		body.put("message", setMessage(ex.getStatus()));	
		body.put("status",  ex.getStatus());

		return new ResponseEntity<>(body, ex.getStatus().getHttpStatus());
	}
	
	//koristi se za anotacije iz requesta
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex, HttpHeaders headers, 
        HttpStatus status, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", ex.getMessage());        

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());

        body.put("error", errors.get(0));    	

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }	
    
    public String setMessage(Status tmp) {    	
    	switch (tmp) {
    	    case SUCCESS:              return "uspesna obrada";
    	    case USERNAME_TAKEN:       return "korinsicko ime vec postoji u bazi";
    	    case EMAIL_TAKEN   :       return "korisnik sa ovim email-om vec postoji u bazi";    	    
    	    case GENERIC_ERROR:        return "greska prilikom obrade zahteva - ulogovanom korisniku nije dozvoljeno da kreira-gleda-azurira-brise podatke za drugog korisnika";
    	    case USER_NOT_FOUND:       return "korisnik sa ovim username-om ne postoji u bazi";
    	    case WRONG_PASSWORD:       return "korisnik je uneo pogresnu sifru";
    	    case WRONG_FORMAT_DATA:    return "korisnik je uneo nevalidne podatke";
    	    case VACATION_NOT_PRESENT: return "Odmor za korisnika ne postoji u bazi"; 
    		default:                   return "generic error";
    	}	
    }
}
