package com.dawasakhi.backend.service;

import com.dawasakhi.backend.config.AppConstants;
import com.dawasakhi.backend.dto.request.LoginRequest;
import com.dawasakhi.backend.dto.request.PasswordLoginRequest;
import com.dawasakhi.backend.dto.request.SendOtpRequest;
import com.dawasakhi.backend.dto.response.AuthResponse;
import com.dawasakhi.backend.dto.response.UserResponse;
import com.dawasakhi.backend.entity.User;
import com.dawasakhi.backend.exception.AuthenticationException;
import com.dawasakhi.backend.exception.ResourceNotFoundException;
import com.dawasakhi.backend.exception.ValidationException;
import com.dawasakhi.backend.repository.UserRepository;
import com.dawasakhi.backend.util.JwtUtil;
import com.dawasakhi.backend.util.OtpUtil;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private OtpUtil otpUtil;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${jwt.access-expiry}")
    private long accessTokenExpiry;

    public void sendOtp(SendOtpRequest request) {
        String phoneNumber = request.getPhoneNumber();
        String otpType = request.getOtpType();
        
        logger.info("Sending OTP to phone: {} for type: {}", phoneNumber, otpType);
        
        // Generate and store OTP
        OtpUtil.OtpResult result = otpUtil.storeOtp(phoneNumber, otpType);
        if (!result.isSuccess()) {
            throw new ValidationException(result.getError(), "OTP_GENERATION_FAILED");
        }
        
        // TODO: Send SMS via Twilio or other SMS service
        // For now, just log the OTP for development
        logger.info("OTP generated successfully for phone: {} - OTP: {}", phoneNumber, result.getOtp());
    }

    public AuthResponse login(LoginRequest request) {
        String phoneNumber = request.getPhoneNumber();
        String otp = request.getOtp();
        
        logger.info("Login attempt for phone: {}", phoneNumber);
        
        // Verify OTP
        OtpUtil.OtpResult verifyResult = otpUtil.verifyOtp(phoneNumber, otp, AppConstants.OTP_LOGIN);
        if (!verifyResult.isSuccess()) {
            throw new AuthenticationException(verifyResult.getError(), "INVALID_OTP");
        }
        
        // Find or create user
        User user = findOrCreateUser(phoneNumber);
        
        // Update last login
        user.setLastLoginAt(LocalDateTime.now());
        user.setFailedLoginAttempts(0);
        user.setPhoneVerified(true);
        user = userRepository.save(user);
        
        // Generate tokens
        Map<String, String> tokens = jwtUtil.generateTokens(user);
        user.setRefreshToken(tokens.get("refreshToken"));
        userRepository.save(user);
        
        // Create response
        UserResponse userResponse = modelMapper.map(user, UserResponse.class);
        AuthResponse authResponse = new AuthResponse(
            tokens.get("accessToken"),
            tokens.get("refreshToken"),
            accessTokenExpiry / 1000, // Convert to seconds
            userResponse
        );
        
        logger.info("User logged in successfully: {}", phoneNumber);
        return authResponse;
    }

    public AuthResponse loginWithPassword(PasswordLoginRequest request) {
        String identifier = request.getIdentifier();
        String password = request.getPassword();
        
        logger.info("Password login request received for identifier: {}", identifier);
        
        // Find user by phone number or email
        User user = findUserByIdentifier(identifier);
        
        // Verify password
        if (user.getPasswordHash() == null || !passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new AuthenticationException("Invalid credentials", "INVALID_CREDENTIALS");
        }
        
        // Check if account is active
        if (!user.isActive()) {
            throw new AuthenticationException("Account is not active", "ACCOUNT_INACTIVE");
        }
        
        // Update last login time
        user.setLastLoginAt(LocalDateTime.now());
        user.setFailedLoginAttempts(0); // Reset failed attempts on successful login
        userRepository.save(user);
        
        // Generate tokens
        Map<String, String> tokens = jwtUtil.generateTokens(user);
        user.setRefreshToken(tokens.get("refreshToken"));
        userRepository.save(user);
        
        // Create response
        UserResponse userResponse = modelMapper.map(user, UserResponse.class);
        AuthResponse authResponse = new AuthResponse(
            tokens.get("accessToken"),
            tokens.get("refreshToken"),
            accessTokenExpiry / 1000, // Convert to seconds
            userResponse
        );
        
        logger.info("User logged in successfully with identifier: {}", identifier);
        return authResponse;
    }

    public AuthResponse refreshToken(String refreshToken) {
        logger.info("Refreshing token");
        
        if (refreshToken == null || !jwtUtil.validateRefreshToken(refreshToken)) {
            throw new AuthenticationException("Invalid refresh token", "INVALID_REFRESH_TOKEN");
        }
        
        String phoneNumber = jwtUtil.extractUsername(refreshToken);
        Long userId = jwtUtil.extractUserId(refreshToken);
        
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found", "USER_NOT_FOUND"));
        
        if (!user.getPhoneNumber().equals(phoneNumber) || !refreshToken.equals(user.getRefreshToken())) {
            throw new AuthenticationException("Invalid refresh token", "INVALID_REFRESH_TOKEN");
        }
        
        // Generate new tokens
        Map<String, String> tokens = jwtUtil.generateTokens(user);
        user.setRefreshToken(tokens.get("refreshToken"));
        userRepository.save(user);
        
        // Create response
        UserResponse userResponse = modelMapper.map(user, UserResponse.class);
        AuthResponse authResponse = new AuthResponse(
            tokens.get("accessToken"),
            tokens.get("refreshToken"),
            accessTokenExpiry / 1000, // Convert to seconds
            userResponse
        );
        
        logger.info("Token refreshed successfully for user: {}", phoneNumber);
        return authResponse;
    }

    public void logout(String accessToken) {
        logger.info("User logging out");
        
        // Blacklist the token
        jwtUtil.blacklistToken(accessToken);
        
        // Optionally clear refresh token from database
        try {
            String phoneNumber = jwtUtil.extractUsername(accessToken);
            Optional<User> userOpt = userRepository.findByPhoneNumber(phoneNumber);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                user.setRefreshToken(null);
                userRepository.save(user);
                logger.info("User logged out successfully: {}", phoneNumber);
            }
        } catch (Exception e) {
            logger.warn("Error clearing refresh token during logout: {}", e.getMessage());
        }
    }

    private User findOrCreateUser(String phoneNumber) {
        Optional<User> userOpt = userRepository.findByPhoneNumber(phoneNumber);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            
            // Check if account is active
            if (!user.isActive()) {
                throw new AuthenticationException("Account is not active", "ACCOUNT_INACTIVE");
            }
            
            return user;
        } else {
            // Create new user - in real app, this might require registration flow
            logger.info("Creating new user for phone: {}", phoneNumber);
            User newUser = new User();
            newUser.setPhoneNumber(phoneNumber);
            newUser.setFullName("New User"); // Default name, should be updated by user
            newUser.setRole(User.UserRole.CUSTOMER);
            newUser.setAccountStatus(User.AccountStatus.ACTIVE);
            newUser.setPhoneVerified(true);
            
            return userRepository.save(newUser);
        }
    }

    private User findUserByIdentifier(String identifier) {
        Optional<User> userOpt;
        
        // Check if identifier is an email
        if (identifier.contains("@")) {
            userOpt = userRepository.findByEmail(identifier);
        } else {
            // Assume it's a phone number
            userOpt = userRepository.findByPhoneNumber(identifier);
        }
        
        return userOpt.orElseThrow(() -> 
            new AuthenticationException("User not found with provided identifier", "USER_NOT_FOUND"));
    }

    public UserResponse getCurrentUser(String phoneNumber) {
        User user = userRepository.findByPhoneNumber(phoneNumber)
            .orElseThrow(() -> new ResourceNotFoundException("User not found", "USER_NOT_FOUND"));
        
        return modelMapper.map(user, UserResponse.class);
    }
}