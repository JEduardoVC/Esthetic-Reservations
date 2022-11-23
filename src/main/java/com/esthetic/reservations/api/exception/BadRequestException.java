package com.esthetic.reservations.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestException extends UtilException {

    public BadRequestException(String resourceName, String reason, String fieldName, String fieldValue) {
        super(resourceName, reason, fieldName, fieldValue);
    }
    
    public BadRequestException(String resourceName, String reason) {
        super(resourceName, reason);
    }
}
