package com.dawasakhi.backend.exception;

import com.dawasakhi.backend.config.AppConstants;
import org.springframework.http.HttpStatus;

public class DawaSureException extends RuntimeException {
    
    private final String errorCode;
    private final HttpStatus httpStatus;
    private final Object details;

    public DawaSureException(String message) {
        super(message);
        this.errorCode = AppConstants.ERROR_INTERNAL_SERVER;
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        this.details = null;
    }

    public DawaSureException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        this.details = null;
    }

    public DawaSureException(String message, String errorCode, HttpStatus httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.details = null;
    }

    public DawaSureException(String message, String errorCode, HttpStatus httpStatus, Object details) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.details = details;
    }

    public DawaSureException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = AppConstants.ERROR_INTERNAL_SERVER;
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        this.details = null;
    }

    public DawaSureException(String message, Throwable cause, String errorCode, HttpStatus httpStatus) {
        super(message, cause);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.details = null;
    }

    // Getters
    public String getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public Object getDetails() {
        return details;
    }
}