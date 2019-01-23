package com.vborodin.exchangerates.exception;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RequestMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ExchangeRateControllerAdvice extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = {ApiException.class})
	protected ResponseEntity<Object> handleConflict(Exception ex, WebRequest request) {
		HttpStatus status = ((ApiException) ex).getStatus();
		ApiErrorMessage errorMessage = new ApiErrorMessage();
	    errorMessage.setStatus(String.valueOf(status.value()));
	    errorMessage.setError(status.getReasonPhrase());
	    errorMessage.setMessage(ex.getMessage());

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=UTF-8");

	    return new ResponseEntity<>(errorMessage, headers, status);
	}
	
}