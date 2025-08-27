package com.dawasakhi.backend.exception;

import com.dawasakhi.backend.config.AppConstants;
import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends DawaSureException {

    public ResourceNotFoundException(String message) {
        super(message, AppConstants.ERROR_RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s: %s", resourceName, fieldName, fieldValue),
              AppConstants.ERROR_RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    public ResourceNotFoundException(String message, Object details) {
        super(message, AppConstants.ERROR_RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND, details);
    }
}