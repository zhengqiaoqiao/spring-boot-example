package org.example.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice 
public class ExceptionHandlerController {
	@ExceptionHandler(Exception.class)  
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)  
    @ResponseBody  
    public String handleException(Exception ex) {  
        return ex.getMessage();  
    }  
  
    @ExceptionHandler(RuntimeException.class)  
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)  
    @ResponseBody  
    public String handleRuntimeException(RuntimeException ex) {  
        return ex.getMessage();  
    }  
}
