package com.dawasakhi.backend.util;

import com.dawasakhi.backend.config.AppConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
public class OtpUtil {

    private static final Logger logger = LoggerFactory.getLogger(OtpUtil.class);
    private static final int OTP_LENGTH = 6;
    private static final int MAX_ATTEMPTS = 3;
    private static final int MIN_INTERVAL_SECONDS = 30;

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final Random random;

    public OtpUtil(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.random = new Random();
    }

    public static class OtpData {
        private String otp;
        private LocalDateTime createdAt;
        private int attempts;
        private boolean verified;

        public OtpData() {}

        public OtpData(String otp) {
            this.otp = otp;
            this.createdAt = LocalDateTime.now();
            this.attempts = 0;
            this.verified = false;
        }

        // Getters and Setters
        public String getOtp() { return otp; }
        public void setOtp(String otp) { this.otp = otp; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
        public int getAttempts() { return attempts; }
        public void setAttempts(int attempts) { this.attempts = attempts; }
        public boolean isVerified() { return verified; }
        public void setVerified(boolean verified) { this.verified = verified; }
    }

    public static class OtpResult {
        private boolean success;
        private String error;
        private String otp;
        private int attemptsRemaining;
        private int waitTime;

        public OtpResult(boolean success) {
            this.success = success;
        }

        public OtpResult(boolean success, String error) {
            this.success = success;
            this.error = error;
        }

        // Getters and Setters
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
        public String getOtp() { return otp; }
        public void setOtp(String otp) { this.otp = otp; }
        public int getAttemptsRemaining() { return attemptsRemaining; }
        public void setAttemptsRemaining(int attemptsRemaining) { this.attemptsRemaining = attemptsRemaining; }
        public int getWaitTime() { return waitTime; }
        public void setWaitTime(int waitTime) { this.waitTime = waitTime; }
    }

    public String generateOtp() {
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }

    public OtpResult storeOtp(String phoneNumber, String otpType) {
        try {
            // Check if we can request new OTP
            OtpResult canRequestResult = canRequestNewOtp(phoneNumber, otpType);
            if (!canRequestResult.isSuccess()) {
                return canRequestResult;
            }

            String otp = generateOtp();
            OtpData otpData = new OtpData(otp);
            
            String key = buildOtpKey(phoneNumber, otpType);
            String otpDataJson = objectMapper.writeValueAsString(otpData);
            
            redisTemplate.opsForValue().set(key, otpDataJson, AppConstants.CACHE_TTL_OTP, TimeUnit.SECONDS);
            
            OtpResult result = new OtpResult(true);
            result.setOtp(otp);
            return result;
            
        } catch (JsonProcessingException e) {
            logger.error("Error storing OTP: {}", e.getMessage());
            return new OtpResult(false, "Failed to generate OTP");
        }
    }

    public OtpResult verifyOtp(String phoneNumber, String providedOtp, String otpType) {
        try {
            String key = buildOtpKey(phoneNumber, otpType);
            Object otpDataObj = redisTemplate.opsForValue().get(key);
            
            if (otpDataObj == null) {
                return new OtpResult(false, "OTP expired or not found");
            }
            
            OtpData otpData = objectMapper.readValue(otpDataObj.toString(), OtpData.class);
            
            if (otpData.isVerified()) {
                return new OtpResult(false, "OTP already used");
            }
            
            if (otpData.getAttempts() >= MAX_ATTEMPTS) {
                invalidateOtp(phoneNumber, otpType);
                return new OtpResult(false, "Maximum OTP attempts exceeded");
            }
            
            // Increment attempts
            otpData.setAttempts(otpData.getAttempts() + 1);
            String updatedOtpDataJson = objectMapper.writeValueAsString(otpData);
            redisTemplate.opsForValue().set(key, updatedOtpDataJson, AppConstants.CACHE_TTL_OTP, TimeUnit.SECONDS);
            
            if (!otpData.getOtp().equals(providedOtp)) {
                OtpResult result = new OtpResult(false, "Invalid OTP");
                result.setAttemptsRemaining(MAX_ATTEMPTS - otpData.getAttempts());
                return result;
            }
            
            // Mark as verified
            otpData.setVerified(true);
            updatedOtpDataJson = objectMapper.writeValueAsString(otpData);
            redisTemplate.opsForValue().set(key, updatedOtpDataJson, AppConstants.CACHE_TTL_OTP, TimeUnit.SECONDS);
            
            return new OtpResult(true);
            
        } catch (JsonProcessingException e) {
            logger.error("Error verifying OTP: {}", e.getMessage());
            return new OtpResult(false, "OTP verification failed");
        }
    }

    public void invalidateOtp(String phoneNumber, String otpType) {
        try {
            String key = buildOtpKey(phoneNumber, otpType);
            redisTemplate.delete(key);
        } catch (Exception e) {
            logger.error("Error invalidating OTP: {}", e.getMessage());
        }
    }

    public long getRemainingOtpTime(String phoneNumber, String otpType) {
        try {
            String key = buildOtpKey(phoneNumber, otpType);
            Long ttl = redisTemplate.getExpire(key, TimeUnit.SECONDS);
            return ttl != null && ttl > 0 ? ttl : 0;
        } catch (Exception e) {
            logger.error("Error getting OTP TTL: {}", e.getMessage());
            return 0;
        }
    }

    public OtpResult canRequestNewOtp(String phoneNumber, String otpType) {
        try {
            String key = buildOtpKey(phoneNumber, otpType);
            Object otpDataObj = redisTemplate.opsForValue().get(key);
            
            if (otpDataObj == null) {
                return new OtpResult(true);
            }
            
            OtpData otpData = objectMapper.readValue(otpDataObj.toString(), OtpData.class);
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime createdAt = otpData.getCreatedAt();
            
            long secondsDiff = java.time.Duration.between(createdAt, now).getSeconds();
            
            if (secondsDiff < MIN_INTERVAL_SECONDS) {
                OtpResult result = new OtpResult(false, 
                    String.format("Please wait %d seconds before requesting new OTP", 
                    MIN_INTERVAL_SECONDS - secondsDiff));
                result.setWaitTime((int)(MIN_INTERVAL_SECONDS - secondsDiff));
                return result;
            }
            
            return new OtpResult(true);
            
        } catch (JsonProcessingException e) {
            logger.error("Error checking OTP request eligibility: {}", e.getMessage());
            return new OtpResult(true); // Allow request if error occurs
        }
    }

    private String buildOtpKey(String phoneNumber, String otpType) {
        return AppConstants.CACHE_OTP + otpType + ":" + phoneNumber;
    }

    public boolean isValidOtpType(String otpType) {
        return otpType != null && (
            otpType.equals(AppConstants.OTP_REGISTRATION) ||
            otpType.equals(AppConstants.OTP_LOGIN) ||
            otpType.equals(AppConstants.OTP_FORGOT_PASSWORD) ||
            otpType.equals(AppConstants.OTP_PHONE_VERIFICATION) ||
            otpType.equals(AppConstants.OTP_DELIVERY_CONFIRMATION)
        );
    }
}