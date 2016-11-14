package org.example.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice 
public class ExceptionHandlerController {
	@ExceptionHandler(Exception.class)  
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)  
    public String handleException(Exception ex) {  
        return ex.getMessage();  
    }  
  
    @ExceptionHandler(RuntimeException.class)  
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)  
    public String handleRuntimeException(RuntimeException ex) {  
        return ex.getMessage();  
    }  
}
