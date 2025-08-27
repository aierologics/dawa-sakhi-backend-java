package com.dawasakhi.backend.exception;

import com.dawasakhi.backend.config.AppConstants;
import org.springframework.http.HttpStatus;

public class AuthenticationException extends DawaSureException {

    public AuthenticationException(String message) {
        super(message, AppConstants.ERROR_AUTHENTICATION, HttpStatus.UNAUTHORIZED);
    }

    public AuthenticationException(String message, Object details) {
        super(message, AppConstants.ERROR_AUTHENTICATION, HttpStatus.UNAUTHORIZED, details);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause, AppConstants.ERROR_AUTHENTICATION, HttpStatus.UNAUTHORIZED);
    }
}