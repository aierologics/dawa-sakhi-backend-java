package com.dawasakhi.backend.exception;

import com.dawasakhi.backend.config.AppConstants;
import org.springframework.http.HttpStatus;

public class ValidationException extends DawaSureException {

    public ValidationException(String message) {
        super(message, AppConstants.ERROR_VALIDATION, HttpStatus.BAD_REQUEST);
    }

    public ValidationException(String message, Object details) {
        super(message, AppConstants.ERROR_VALIDATION, HttpStatus.BAD_REQUEST, details);
    }
}