package com.dawasakhi.backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class PasswordLoginRequest {

    @NotBlank(message = "Phone number or email is required")
    private String identifier; // Can be phone number or email

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 50, message = "Password must be between 6 and 50 characters")
    private String password;

    public PasswordLoginRequest() {
    }

    public PasswordLoginRequest(String identifier, String password) {
        this.identifier = identifier;
        this.password = password;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Helper methods to determine identifier type
    public boolean isEmail() {
        return identifier != null && identifier.contains("@");
    }

    public boolean isPhoneNumber() {
        return identifier != null && identifier.matches("^[6-9]\\d{9}$");
    }

    @Override
    public String toString() {
        return "PasswordLoginRequest{" +
                "identifier='" + identifier + '\'' +
                ", password='****'" +
                '}';
    }
}