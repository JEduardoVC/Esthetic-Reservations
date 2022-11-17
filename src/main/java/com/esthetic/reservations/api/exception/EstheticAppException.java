package com.esthetic.reservations.api.exception;

import org.springframework.http.HttpStatus;

public class EstheticAppException extends RuntimeException {

    private HttpStatus httpStatus;
    private String message;

    public EstheticAppException() {
        super();
    }

    public EstheticAppException(HttpStatus httpStatus, String message) {
        super();
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
