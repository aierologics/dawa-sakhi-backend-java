package com.dawasakhi.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class SendOtpRequest {

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Phone number must be a valid 10-digit Indian mobile number")
    private String phoneNumber;

    private String otpType = "LOGIN"; // LOGIN, REGISTRATION, FORGOT_PASSWORD, etc.

    public SendOtpRequest() {
    }

    public SendOtpRequest(String phoneNumber, String otpType) {
        this.phoneNumber = phoneNumber;
        this.otpType = otpType;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getOtpType() {
        return otpType;
    }

    public void setOtpType(String otpType) {
        this.otpType = otpType;
    }

    @Override
    public String toString() {
        return "SendOtpRequest{" +
                "phoneNumber='" + phoneNumber + '\'' +
                ", otpType='" + otpType + '\'' +
                '}';
    }
}