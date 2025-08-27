package com.dawasakhi.backend.repository;

import com.dawasakhi.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByPhoneNumber(String phoneNumber);
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByPhoneNumberAndAccountStatus(String phoneNumber, User.AccountStatus accountStatus);
    
    boolean existsByPhoneNumber(String phoneNumber);
    
    boolean existsByEmail(String email);
    
    @Query("SELECT u FROM User u WHERE u.phoneNumber = :phoneNumber AND u.accountStatus = 'ACTIVE'")
    Optional<User> findActiveUserByPhoneNumber(@Param("phoneNumber") String phoneNumber);
    
    @Query("SELECT u FROM User u WHERE u.email = :email AND u.accountStatus = 'ACTIVE'")
    Optional<User> findActiveUserByEmail(@Param("email") String email);
    
    Page<User> findByAccountStatus(User.AccountStatus accountStatus, Pageable pageable);
    
    List<User> findByRole(User.UserRole role);
    
    Page<User> findByRoleAndAccountStatus(User.UserRole role, User.AccountStatus accountStatus, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.phoneVerified = :verified")
    Page<User> findByPhoneVerified(@Param("verified") Boolean verified, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.emailVerified = :verified")
    Page<User> findByEmailVerified(@Param("verified") Boolean verified, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.marketingConsent = true")
    List<User> findUsersWithMarketingConsent();
    
    @Query("SELECT u FROM User u WHERE u.lastLoginAt >= :fromDate")
    Page<User> findActiveUsersSince(@Param("fromDate") LocalDateTime fromDate, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.accountLockedUntil IS NOT NULL AND u.accountLockedUntil > CURRENT_TIMESTAMP")
    List<User> findLockedUsers();
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role AND u.accountStatus = 'ACTIVE'")
    long countActiveUsersByRole(@Param("role") User.UserRole role);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.createdAt >= :fromDate")
    long countNewUsersSince(@Param("fromDate") LocalDateTime fromDate);
    
    @Modifying
    @Query("UPDATE User u SET u.lastLoginAt = :loginTime, u.failedLoginAttempts = 0, u.accountLockedUntil = NULL WHERE u.userId = :userId")
    void updateLastLoginAndResetFailures(@Param("userId") Long userId, @Param("loginTime") LocalDateTime loginTime);
    
    @Modifying
    @Query("UPDATE User u SET u.failedLoginAttempts = u.failedLoginAttempts + 1 WHERE u.userId = :userId")
    void incrementFailedLoginAttempts(@Param("userId") Long userId);
    
    @Modifying
    @Query("UPDATE User u SET u.accountLockedUntil = :lockUntil WHERE u.userId = :userId")
    void lockUser(@Param("userId") Long userId, @Param("lockUntil") LocalDateTime lockUntil);
    
    @Modifying
    @Query("UPDATE User u SET u.phoneVerified = true WHERE u.userId = :userId")
    void markPhoneAsVerified(@Param("userId") Long userId);
    
    @Modifying
    @Query("UPDATE User u SET u.emailVerified = true WHERE u.userId = :userId")
    void markEmailAsVerified(@Param("userId") Long userId);
    
    @Modifying
    @Query("UPDATE User u SET u.refreshToken = :refreshToken WHERE u.userId = :userId")
    void updateRefreshToken(@Param("userId") Long userId, @Param("refreshToken") String refreshToken);
    
    @Query("SELECT u FROM User u WHERE u.fullName ILIKE %:searchTerm% OR u.phoneNumber ILIKE %:searchTerm% OR u.email ILIKE %:searchTerm%")
    Page<User> searchUsers(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    Page<User> findByAccountStatusNot(User.AccountStatus accountStatus, Pageable pageable);
    
    Optional<User> findByEmailAndUserIdNot(String email, Long userId);
}