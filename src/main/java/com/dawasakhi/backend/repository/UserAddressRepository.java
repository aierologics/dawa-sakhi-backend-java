package com.dawasakhi.backend.repository;

import com.dawasakhi.backend.entity.User;
import com.dawasakhi.backend.entity.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {

    List<UserAddress> findByUserUserIdAndIsActiveTrue(Long userId);
    
    List<UserAddress> findByUserUserIdAndIsActiveTrueOrderByIsDefaultDescCreatedAtDesc(Long userId);
    
    Optional<UserAddress> findByAddressIdAndUserUserIdAndIsActiveTrue(Long addressId, Long userId);
    
    Optional<UserAddress> findByUserUserIdAndIsDefaultTrueAndIsActiveTrue(Long userId);
    
    List<UserAddress> findByUserUserIdAndAddressTypeAndIsActiveTrue(Long userId, UserAddress.AddressType addressType);
    
    List<UserAddress> findByPostalCodeAndIsActiveTrue(String postalCode);
    
    List<UserAddress> findByCityAndStateAndIsActiveTrue(String city, String state);
    
    @Query("SELECT a FROM UserAddress a WHERE a.user.userId = :userId AND " +
           "(a.addressLine1 ILIKE %:searchTerm% OR a.addressLine2 ILIKE %:searchTerm% OR " +
           "a.city ILIKE %:searchTerm% OR a.state ILIKE %:searchTerm%) AND a.isActive = true")
    List<UserAddress> searchUserAddresses(@Param("userId") Long userId, @Param("searchTerm") String searchTerm);
    
    @Query("SELECT COUNT(a) FROM UserAddress a WHERE a.user.userId = :userId AND a.isActive = true")
    long countActiveAddressesByUser(@Param("userId") Long userId);
    
    @Modifying
    @Query("UPDATE UserAddress a SET a.isDefault = false WHERE a.user.userId = :userId")
    void unsetAllDefaultAddresses(@Param("userId") Long userId);
    
    @Modifying
    @Query("UPDATE UserAddress a SET a.isDefault = true WHERE a.addressId = :addressId AND a.user.userId = :userId")
    void setDefaultAddress(@Param("addressId") Long addressId, @Param("userId") Long userId);
    
    @Modifying
    @Query("UPDATE UserAddress a SET a.isActive = false WHERE a.addressId = :addressId AND a.user.userId = :userId")
    void softDeleteAddress(@Param("addressId") Long addressId, @Param("userId") Long userId);
    
    boolean existsByUserUserIdAndIsDefaultTrueAndIsActiveTrue(Long userId);
    
    // Additional methods needed by services
    List<UserAddress> findByUserOrderByIsDefaultDescCreatedAtAsc(User user);
    
    List<UserAddress> findByUser(User user);
    
    Optional<UserAddress> findByUserAndIsDefaultTrue(User user);
    
    List<UserAddress> findByPincode(String pincode);
}