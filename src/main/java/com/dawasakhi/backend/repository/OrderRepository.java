package com.dawasakhi.backend.repository;

import com.dawasakhi.backend.entity.Order;
import com.dawasakhi.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderNumber(String orderNumber);
    
    Optional<Order> findByOrderIdAndCustomerUserId(Long orderId, Long customerId);
    
    Optional<Order> findByOrderNumberAndCustomerUserId(String orderNumber, Long customerId);
    
    Page<Order> findByCustomerUserIdOrderByCreatedAtDesc(Long customerId, Pageable pageable);
    
    Page<Order> findByCustomerUserIdAndOrderStatusOrderByCreatedAtDesc(Long customerId, Order.OrderStatus orderStatus, Pageable pageable);
    
    Page<Order> findByOrderStatusOrderByCreatedAtDesc(Order.OrderStatus orderStatus, Pageable pageable);
    
    Page<Order> findByPaymentStatusOrderByCreatedAtDesc(Order.PaymentStatus paymentStatus, Pageable pageable);
    
    Page<Order> findByOrderStatusAndPaymentStatusOrderByCreatedAtDesc(Order.OrderStatus orderStatus, Order.PaymentStatus paymentStatus, Pageable pageable);
    
    @Query("SELECT o FROM Order o WHERE o.customer.userId = :customerId AND " +
           "(o.orderNumber LIKE %:searchTerm% OR " +
           "LOWER(o.deliveryAddress.city) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(o.deliveryAddress.state) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
           "ORDER BY o.createdAt DESC")
    Page<Order> searchCustomerOrders(@Param("customerId") Long customerId, 
                                    @Param("searchTerm") String searchTerm, 
                                    Pageable pageable);
    
    @Query("SELECT o FROM Order o WHERE o.createdAt >= :fromDate AND o.createdAt <= :toDate ORDER BY o.createdAt DESC")
    Page<Order> findOrdersByDateRange(@Param("fromDate") LocalDateTime fromDate, 
                                     @Param("toDate") LocalDateTime toDate, 
                                     Pageable pageable);
    
    @Query("SELECT o FROM Order o WHERE o.customer.userId = :customerId AND " +
           "o.createdAt >= :fromDate AND o.createdAt <= :toDate ORDER BY o.createdAt DESC")
    Page<Order> findCustomerOrdersByDateRange(@Param("customerId") Long customerId,
                                             @Param("fromDate") LocalDateTime fromDate, 
                                             @Param("toDate") LocalDateTime toDate, 
                                             Pageable pageable);
    
    List<Order> findByDeliveryTypeAndOrderStatusOrderByCreatedAtDesc(Order.DeliveryType deliveryType, Order.OrderStatus orderStatus);
    
    List<Order> findByPrescriptionRequiredTrueAndPrescriptionUploadedFalse();
    
    @Query("SELECT COUNT(o) FROM Order o WHERE o.customer.userId = :customerId")
    long countOrdersByCustomer(@Param("customerId") Long customerId);
    
    @Query("SELECT COUNT(o) FROM Order o WHERE o.orderStatus = :status")
    long countOrdersByStatus(@Param("status") Order.OrderStatus status);
    
    @Query("SELECT COUNT(o) FROM Order o WHERE o.paymentStatus = :status")
    long countOrdersByPaymentStatus(@Param("status") Order.PaymentStatus status);
    
    @Query("SELECT COUNT(o) FROM Order o WHERE o.createdAt >= :fromDate")
    long countOrdersSince(@Param("fromDate") LocalDateTime fromDate);
    
    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.orderStatus = 'DELIVERED' AND o.paymentStatus = 'COMPLETED'")
    BigDecimal getTotalRevenue();
    
    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.customer.userId = :customerId AND " +
           "o.orderStatus = 'DELIVERED' AND o.paymentStatus = 'COMPLETED'")
    BigDecimal getCustomerTotalSpent(@Param("customerId") Long customerId);
    
    @Query("SELECT AVG(o.totalAmount) FROM Order o WHERE o.orderStatus = 'DELIVERED'")
    BigDecimal getAverageOrderValue();
    
    @Query("SELECT o FROM Order o WHERE o.estimatedDeliveryTime <= :currentTime AND " +
           "o.orderStatus IN ('PLACED', 'CONFIRMED', 'PREPARING', 'READY_FOR_PICKUP', 'OUT_FOR_DELIVERY')")
    List<Order> findOverdueOrders(@Param("currentTime") LocalDateTime currentTime);
    
    @Query("SELECT o FROM Order o WHERE o.orderStatus IN ('PLACED', 'CONFIRMED') " +
           "ORDER BY CASE WHEN o.deliveryType = 'EXPRESS' THEN 1 ELSE 2 END, o.createdAt ASC")
    List<Order> findPendingOrdersByPriority();
    
    @Query("SELECT o FROM Order o WHERE o.paymentStatus = 'COMPLETED' AND o.refundStatus = 'PENDING'")
    List<Order> findOrdersWithPendingRefunds();
    
    @Query("SELECT DATE(o.createdAt), COUNT(o), SUM(o.totalAmount) FROM Order o " +
           "WHERE o.createdAt >= :fromDate AND o.orderStatus = 'DELIVERED' " +
           "GROUP BY DATE(o.createdAt) ORDER BY DATE(o.createdAt)")
    List<Object[]> getDailyOrderStats(@Param("fromDate") LocalDateTime fromDate);
    
    @Query("SELECT o.paymentMethod, COUNT(o) FROM Order o " +
           "WHERE o.paymentStatus = 'COMPLETED' " +
           "GROUP BY o.paymentMethod")
    List<Object[]> getPaymentMethodStats();
    
    @Query("SELECT o.deliveryType, COUNT(o), AVG(o.totalAmount) FROM Order o " +
           "WHERE o.orderStatus = 'DELIVERED' " +
           "GROUP BY o.deliveryType")
    List<Object[]> getDeliveryTypeStats();
    
    // Additional methods needed by services
    Optional<Order> findByOrderNumberAndCustomer(String orderNumber, User customer);
    
    Page<Order> findByCustomer(User customer, Pageable pageable);
    
    Page<Order> findByOrderStatus(Order.OrderStatus orderStatus, Pageable pageable);
    
    List<Order> findByDeliveryAddressPincodeAndOrderStatus(String pincode, Order.OrderStatus orderStatus);
    
    List<Order> findByOrderStatusOrderByCreatedAtAsc(Order.OrderStatus orderStatus);
    
    List<Order> findByOrderStatusAndActualDeliveryTimeBetween(Order.OrderStatus orderStatus, LocalDateTime fromDate, LocalDateTime toDate);
    
    List<Order> findByDeliveryAddressPincodeAndCreatedAtBetween(String pincode, LocalDateTime fromDate, LocalDateTime toDate);
    
    List<Order> findByCreatedAtBetween(LocalDateTime fromDate, LocalDateTime toDate);
    
    List<Order> findByOrderStatusAndEstimatedDeliveryTimeBefore(Order.OrderStatus orderStatus, LocalDateTime dateTime);
}