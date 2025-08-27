package com.dawasakhi.backend.controller;

import com.dawasakhi.backend.dto.request.LoginRequest;
import com.dawasakhi.backend.dto.request.PasswordLoginRequest;
import com.dawasakhi.backend.dto.request.SendOtpRequest;
import com.dawasakhi.backend.dto.response.ApiResponse;
import com.dawasakhi.backend.dto.response.AuthResponse;
import com.dawasakhi.backend.dto.response.UserResponse;
import com.dawasakhi.backend.service.AuthService;
import com.dawasakhi.backend.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @PostMapping("/send-otp")
    @Operation(summary = "Send OTP", description = "Send OTP to the provided phone number")
    public ResponseEntity<ApiResponse<String>> sendOtp(@Valid @RequestBody SendOtpRequest request) {
        logger.info("OTP request received for phone: {}", request.getPhoneNumber());
        
        authService.sendOtp(request);
        
        return ResponseEntity.ok(
            ApiResponse.success("OTP sent successfully to " + request.getPhoneNumber())
        );
    }

    @PostMapping("/login")
    @Operation(summary = "Login with OTP", description = "Login using phone number and OTP")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        logger.info("Login request received for phone: {}", request.getPhoneNumber());
        
        AuthResponse authResponse = authService.login(request);
        
        return ResponseEntity.ok(
            ApiResponse.success("Login successful", authResponse)
        );
    }

    @PostMapping("/login-password")
    @Operation(summary = "Login with Password", description = "Login using phone number/email and password")
    public ResponseEntity<ApiResponse<AuthResponse>> loginWithPassword(@Valid @RequestBody PasswordLoginRequest request) {
        logger.info("Password login request received for identifier: {}", request.getIdentifier());
        
        AuthResponse authResponse = authService.loginWithPassword(request);
        
        return ResponseEntity.ok(
            ApiResponse.success("Login successful", authResponse)
        );
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh Token", description = "Refresh access token using refresh token")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@RequestBody RefreshTokenRequest request) {
        logger.info("Token refresh request received");
        
        AuthResponse authResponse = authService.refreshToken(request.getRefreshToken());
        
        return ResponseEntity.ok(
            ApiResponse.success("Token refreshed successfully", authResponse)
        );
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout", description = "Logout user and invalidate tokens")
    public ResponseEntity<ApiResponse<String>> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = jwtUtil.extractTokenFromHeader(authHeader);
        
        if (token != null) {
            authService.logout(token);
        }
        
        return ResponseEntity.ok(
            ApiResponse.success("Logout successful")
        );
    }

    @GetMapping("/me")
    @Operation(summary = "Get Current User", description = "Get current authenticated user details")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phoneNumber = authentication.getName();
        
        UserResponse userResponse = authService.getCurrentUser(phoneNumber);
        
        return ResponseEntity.ok(
            ApiResponse.success("User details retrieved successfully", userResponse)
        );
    }

    // Temporary endpoint to generate password hash - REMOVE IN PRODUCTION!
    @GetMapping("/generate-hash")
    @Operation(summary = "Generate Password Hash", description = "Temporary endpoint to generate BCrypt hash")
    public ResponseEntity<ApiResponse<String>> generatePasswordHash(@RequestParam String password) {
        String hash = passwordEncoder.encode(password);
        return ResponseEntity.ok(
            ApiResponse.success("Password hash generated", hash)
        );
    }

    // Helper DTO class for refresh token request
    public static class RefreshTokenRequest {
        private String refreshToken;

        public RefreshTokenRequest() {}

        public String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }
    }
}