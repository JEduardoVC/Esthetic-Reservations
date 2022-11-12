package com.esthetic.reservations.api.exception;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.esthetic.reservations.api.dto.ErrorDetailsDTO;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetailsDTO> handleResourceNotFoundException(ResourceNotFoundException exception,
            WebRequest webRequest) {

        ErrorDetailsDTO errorDetailsDTO = new ErrorDetailsDTO(new Date(), exception.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetailsDTO, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorDetailsDTO> handleConflictException(ConflictException exception,
            WebRequest webRequest) {

        ErrorDetailsDTO errorDetailsDTO = new ErrorDetailsDTO(new Date(), exception.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetailsDTO, HttpStatus.CONFLICT);

    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorDetailsDTO> handleUnauthorizedException(UnauthorizedException exception,
            WebRequest webRequest) {

        ErrorDetailsDTO errorDetailsDTO = new ErrorDetailsDTO(new Date(), exception.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetailsDTO, HttpStatus.UNAUTHORIZED);

    }

    @ExceptionHandler(EstheticAppException.class)
    public ResponseEntity<ErrorDetailsDTO> handleEstheticAppException(EstheticAppException exception,
            WebRequest webRequest) {

        ErrorDetailsDTO errorDetailsDTO = new ErrorDetailsDTO(new Date(), exception.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetailsDTO, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetailsDTO> handleGlobalException(Exception exception,
            WebRequest webRequest) {

        ErrorDetailsDTO errorDetailsDTO = new ErrorDetailsDTO(new Date(), exception.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetailsDTO, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> errors = new HashMap<>();
        Map<String, String> validationErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            validationErrors.put(fieldName, message);
        });
        errors.put("validationErrors", validationErrors);
        errors.put("timestamp", new Date());
        errors.put("exception", ex.getMessage());
        errors.put("details", request.getDescription(false));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

}
