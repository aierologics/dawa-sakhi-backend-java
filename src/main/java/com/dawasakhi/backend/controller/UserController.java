package com.dawasakhi.backend.controller;

import com.dawasakhi.backend.dto.response.ApiResponse;
import com.dawasakhi.backend.dto.response.UserResponse;
import com.dawasakhi.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Tag(name = "User Management", description = "User management APIs")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    @Operation(summary = "Get Current User Profile", description = "Get current authenticated user's profile")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phoneNumber = authentication.getName();
        
        UserResponse userResponse = userService.getUserByPhoneNumber(phoneNumber);
        
        return ResponseEntity.ok(
            ApiResponse.success("User profile retrieved successfully", userResponse)
        );
    }

    @PutMapping("/profile")
    @Operation(summary = "Update User Profile", description = "Update current user's profile")
    public ResponseEntity<ApiResponse<UserResponse>> updateCurrentUserProfile(
            @Valid @RequestBody UserService.UpdateUserRequest request) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phoneNumber = authentication.getName();
        
        UserResponse userResponse = userService.updateUserProfile(phoneNumber, request);
        
        return ResponseEntity.ok(
            ApiResponse.success("Profile updated successfully", userResponse)
        );
    }

    @DeleteMapping("/profile")
    @Operation(summary = "Delete User Account", description = "Delete current user's account")
    public ResponseEntity<ApiResponse<String>> deleteCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phoneNumber = authentication.getName();
        
        userService.deleteUser(phoneNumber);
        
        return ResponseEntity.ok(
            ApiResponse.success("Account deleted successfully")
        );
    }

    // Admin endpoints
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get User by ID", description = "Get user details by ID (Admin only)")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long userId) {
        UserResponse userResponse = userService.getUserById(userId);
        
        return ResponseEntity.ok(
            ApiResponse.success("User details retrieved successfully", userResponse)
        );
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get All Users", description = "Get paginated list of users (Admin only)")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Page<UserResponse> users = userService.getAllUsers(page, size, sortBy, sortDir);
        
        return ResponseEntity.ok(
            ApiResponse.success("Users retrieved successfully", users)
        );
    }

    @GetMapping("/phone/{phoneNumber}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get User by Phone", description = "Get user by phone number (Admin only)")
    public ResponseEntity<ApiResponse<UserResponse>> getUserByPhoneNumber(@PathVariable String phoneNumber) {
        UserResponse userResponse = userService.getUserByPhoneNumber(phoneNumber);
        
        return ResponseEntity.ok(
            ApiResponse.success("User details retrieved successfully", userResponse)
        );
    }

    @GetMapping("/exists/{phoneNumber}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Check User Exists", description = "Check if user exists by phone number (Admin only)")
    public ResponseEntity<ApiResponse<Boolean>> checkUserExists(@PathVariable String phoneNumber) {
        boolean exists = userService.existsByPhoneNumber(phoneNumber);
        
        return ResponseEntity.ok(
            ApiResponse.success("User existence check completed", exists)
        );
    }
}