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
		
		body.put("message", ex.getMessage());	
		body.put("status",  ex.getStatus());

		return new ResponseEntity<>(body, httpStatusValue(ex.getStatus()));
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
    
    public HttpStatus httpStatusValue(Status tmp) {    	
    	switch (tmp) {
    	    case OK:                return HttpStatus.OK;
    	    case USERNAME_TAKEN:    return HttpStatus.CONFLICT;
    	    case EMAIL_TAKEN   :    return HttpStatus.CONFLICT;    	    
    	    case GENERIC_ERROR:     return HttpStatus.BAD_REQUEST;
    	    case USER_NOT_FOUND:    return HttpStatus.NOT_FOUND;
    	    case WRONG_PASSWORD:    return HttpStatus.UNAUTHORIZED;
    		default:                return HttpStatus.BAD_REQUEST;
    	}	
    }
}
