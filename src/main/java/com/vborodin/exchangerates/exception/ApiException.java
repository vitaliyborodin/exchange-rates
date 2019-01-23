package com.vborodin.exchangerates.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	private final HttpStatus status;

	public ApiException(String message) {
		super(message);
		this.status = HttpStatus.INTERNAL_SERVER_ERROR;
	}
	
	public ApiException(String message, HttpStatus status) {
		super(message);	
		this.status = status != null ? status : HttpStatus.INTERNAL_SERVER_ERROR;
	}
	
	public ApiException(String message, Throwable cause) {
		super(message, cause);
		this.status = HttpStatus.INTERNAL_SERVER_ERROR;
	}

	public HttpStatus getStatus() {
		return status;
	}
	
}
