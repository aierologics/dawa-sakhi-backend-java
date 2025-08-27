package com.dawasakhi.backend.service;

import com.dawasakhi.backend.dto.response.UserResponse;
import com.dawasakhi.backend.entity.User;
import com.dawasakhi.backend.exception.ResourceNotFoundException;
import com.dawasakhi.backend.exception.ValidationException;
import com.dawasakhi.backend.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId, "USER_NOT_FOUND"));
        
        return modelMapper.map(user, UserResponse.class);
    }

    public UserResponse getUserByPhoneNumber(String phoneNumber) {
        User user = userRepository.findByPhoneNumber(phoneNumber)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with phone: " + phoneNumber, "USER_NOT_FOUND"));
        
        return modelMapper.map(user, UserResponse.class);
    }

    public UserResponse updateUserProfile(String phoneNumber, UpdateUserRequest request) {
        User user = userRepository.findByPhoneNumber(phoneNumber)
            .orElseThrow(() -> new ResourceNotFoundException("User not found", "USER_NOT_FOUND"));

        // Update allowed fields
        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }
        if (request.getEmail() != null) {
            // Check if email is already taken by another user
            Optional<User> existingUser = userRepository.findByEmailAndUserIdNot(request.getEmail(), user.getUserId());
            if (existingUser.isPresent()) {
                throw new ValidationException("Email is already in use", "EMAIL_ALREADY_EXISTS");
            }
            user.setEmail(request.getEmail());
        }
        if (request.getDateOfBirth() != null) {
            user.setDateOfBirth(request.getDateOfBirth());
        }
        if (request.getGender() != null) {
            user.setGender(request.getGender());
        }
        if (request.getMarketingConsent() != null) {
            user.setMarketingConsent(request.getMarketingConsent());
        }

        user.setUpdatedAt(LocalDateTime.now());
        user = userRepository.save(user);

        logger.info("User profile updated for phone: {}", phoneNumber);
        return modelMapper.map(user, UserResponse.class);
    }

    public void deleteUser(String phoneNumber) {
        User user = userRepository.findByPhoneNumber(phoneNumber)
            .orElseThrow(() -> new ResourceNotFoundException("User not found", "USER_NOT_FOUND"));

        // Soft delete - just mark as deleted
        user.setAccountStatus(User.AccountStatus.DELETED);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        logger.info("User account deleted for phone: {}", phoneNumber);
    }

    public Page<UserResponse> getAllUsers(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<User> users = userRepository.findByAccountStatusNot(User.AccountStatus.DELETED, pageable);
        
        return users.map(user -> modelMapper.map(user, UserResponse.class));
    }

    public boolean existsByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber).isPresent();
    }

    // Inner DTO class for update request
    public static class UpdateUserRequest {
        private String fullName;
        private String email;
        private java.time.LocalDate dateOfBirth;
        private User.Gender gender;
        private Boolean marketingConsent;

        public UpdateUserRequest() {}

        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public java.time.LocalDate getDateOfBirth() { return dateOfBirth; }
        public void setDateOfBirth(java.time.LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

        public User.Gender getGender() { return gender; }
        public void setGender(User.Gender gender) { this.gender = gender; }

        public Boolean getMarketingConsent() { return marketingConsent; }
        public void setMarketingConsent(Boolean marketingConsent) { this.marketingConsent = marketingConsent; }
    }
}