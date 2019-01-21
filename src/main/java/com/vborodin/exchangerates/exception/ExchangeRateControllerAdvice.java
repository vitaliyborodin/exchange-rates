package com.vborodin.exchangerates.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExchangeRateControllerAdvice extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = {ApiException.class})
	protected ResponseEntity<Object> handleConflict(Exception ex, WebRequest request) {
		HttpStatus status = ((ApiException) ex).getStatus();
		ApiErrorMessage errorMessage = new ApiErrorMessage();
	    errorMessage.setStatus(String.valueOf(status.value()));
	    errorMessage.setError(status.getReasonPhrase());
	    errorMessage.setMessage(ex.getMessage());
	    
	    return new ResponseEntity<>(errorMessage, status);
	}
	
}