package com.vborodin.exchangerates.exception;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;


public class ApiErrorMessage implements Serializable {
    @JsonProperty
    private String status;
    @JsonProperty
    private String error;
    @JsonProperty
    private String message;

    public ApiErrorMessage() {
    }

    public ApiErrorMessage(String status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
