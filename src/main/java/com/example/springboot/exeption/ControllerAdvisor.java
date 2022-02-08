package com.example.springboot.exeption;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
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
		
		ResourceBundle bundle = ResourceBundle.getBundle("messages");
		String message = bundle.getString(ex.getStatus().jsonValue());
		
		body.put("message", message);	
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
}
