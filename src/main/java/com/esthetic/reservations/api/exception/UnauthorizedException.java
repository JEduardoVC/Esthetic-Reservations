package com.esthetic.reservations.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends UtilException {

    public UnauthorizedException(String resourceName, String reason, String fieldName, String fieldValue) {
        super(resourceName, reason, fieldName, fieldValue);
    }
}
