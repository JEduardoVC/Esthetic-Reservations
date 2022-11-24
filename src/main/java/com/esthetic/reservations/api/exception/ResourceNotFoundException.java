package com.esthetic.reservations.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends UtilException {

    public ResourceNotFoundException(String resourceName, String reason, String fieldName, String fieldValue) {
        super(resourceName, reason, fieldName, fieldValue);
    }
    
    public ResourceNotFoundException(String resourceName, String reason) {
        super(resourceName, reason);
    }
}
