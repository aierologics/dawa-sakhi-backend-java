package com.dawasakhi.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders", indexes = {
    @Index(name = "idx_order_number", columnList = "orderNumber"),
    @Index(name = "idx_order_customer", columnList = "customer_id"),
    @Index(name = "idx_order_status", columnList = "orderStatus"),
    @Index(name = "idx_order_payment_status", columnList = "paymentStatus"),
    @Index(name = "idx_order_created", columnList = "createdAt")
})
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "order_number", unique = true, nullable = false, length = 20)
    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_address_id", nullable = false)
    private UserAddress deliveryAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus = OrderStatus.PLACED;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_type", nullable = false)
    private DeliveryType deliveryType = DeliveryType.STANDARD;

    @Column(name = "scheduled_delivery_time")
    private LocalDateTime scheduledDeliveryTime;

    @Column(name = "special_instructions", columnDefinition = "TEXT")
    private String specialInstructions;

    @NotNull(message = "Subtotal is required")
    @PositiveOrZero(message = "Subtotal must be positive")
    @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @PositiveOrZero(message = "Delivery charges must be positive")
    @Column(name = "delivery_charges", precision = 8, scale = 2)
    private BigDecimal deliveryCharges = BigDecimal.ZERO;

    @PositiveOrZero(message = "Service charges must be positive")
    @Column(name = "service_charges", precision = 8, scale = 2)
    private BigDecimal serviceCharges = BigDecimal.ZERO;

    @PositiveOrZero(message = "Discount amount must be positive")
    @Column(name = "discount_amount", precision = 8, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Size(max = 50, message = "Coupon code must be less than 50 characters")
    @Column(name = "coupon_code", length = 50)
    private String couponCode;

    @NotNull(message = "Tax amount is required")
    @PositiveOrZero(message = "Tax amount must be positive")
    @Column(name = "tax_amount", nullable = false, precision = 8, scale = 2)
    private BigDecimal taxAmount;

    @NotNull(message = "Total amount is required")
    @PositiveOrZero(message = "Total amount must be positive")
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Size(max = 100, message = "Payment ID must be less than 100 characters")
    @Column(name = "payment_id", length = 100)
    private String paymentId;

    @Column(name = "prescription_required", nullable = false)
    private Boolean prescriptionRequired = false;

    @Column(name = "prescription_uploaded", nullable = false)
    private Boolean prescriptionUploaded = false;

    @Column(name = "prescription_urls", columnDefinition = "TEXT")
    private String prescriptionUrls; // JSON array as string

    @Column(name = "estimated_delivery_time")
    private LocalDateTime estimatedDeliveryTime;

    @Column(name = "actual_delivery_time")
    private LocalDateTime actualDeliveryTime;

    @Column(name = "delivery_otp", length = 6)
    private String deliveryOtp;

    @Size(max = 500, message = "Delivery proof URL must be less than 500 characters")
    @Column(name = "delivery_proof_url", length = 500)
    private String deliveryProofUrl;

    @Column(name = "customer_rating")
    private Integer customerRating;

    @Column(name = "customer_review", columnDefinition = "TEXT")
    private String customerReview;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cancelled_by")
    private User cancelledBy;

    @Column(name = "cancellation_reason", columnDefinition = "TEXT")
    private String cancellationReason;

    @PositiveOrZero(message = "Refund amount must be positive")
    @Column(name = "refund_amount", precision = 10, scale = 2)
    private BigDecimal refundAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "refund_status")
    private RefundStatus refundStatus = RefundStatus.NOT_APPLICABLE;

    // Relationships
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> items = new ArrayList<>();

    // Enums
    public enum OrderStatus {
        PLACED, CONFIRMED, PREPARING, READY_FOR_PICKUP, 
        OUT_FOR_DELIVERY, DELIVERED, CANCELLED, RETURNED
    }

    public enum DeliveryType {
        STANDARD, EXPRESS, SCHEDULED
    }

    public enum PaymentMethod {
        CREDIT_CARD, DEBIT_CARD, UPI, NETBANKING, WALLET, COD
    }

    public enum PaymentStatus {
        PENDING, PROCESSING, COMPLETED, FAILED, REFUNDED, CANCELLED
    }

    public enum RefundStatus {
        NOT_APPLICABLE, PENDING, PROCESSED, FAILED
    }

    // Constructors
    public Order() {
        super();
        generateOrderNumber();
        generateDeliveryOtp();
    }

    public Order(User customer, UserAddress deliveryAddress, 
                BigDecimal subtotal, BigDecimal taxAmount, BigDecimal totalAmount) {
        this();
        this.customer = customer;
        this.deliveryAddress = deliveryAddress;
        this.subtotal = subtotal;
        this.taxAmount = taxAmount;
        this.totalAmount = totalAmount;
    }

    // Business Logic
    @PrePersist
    private void prePersist() {
        if (orderNumber == null || orderNumber.isEmpty()) {
            generateOrderNumber();
        }
        if (deliveryOtp == null || deliveryOtp.isEmpty()) {
            generateDeliveryOtp();
        }
    }

    private void generateOrderNumber() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String random = String.format("%03d", (int)(Math.random() * 1000));
        this.orderNumber = "DS" + timestamp.substring(timestamp.length() - 6) + random;
    }

    private void generateDeliveryOtp() {
        this.deliveryOtp = String.format("%06d", (int)(Math.random() * 1000000));
    }

    // Getters and Setters
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public UserAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(UserAddress deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public DeliveryType getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(DeliveryType deliveryType) {
        this.deliveryType = deliveryType;
    }

    public LocalDateTime getScheduledDeliveryTime() {
        return scheduledDeliveryTime;
    }

    public void setScheduledDeliveryTime(LocalDateTime scheduledDeliveryTime) {
        this.scheduledDeliveryTime = scheduledDeliveryTime;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getDeliveryCharges() {
        return deliveryCharges;
    }

    public void setDeliveryCharges(BigDecimal deliveryCharges) {
        this.deliveryCharges = deliveryCharges;
    }

    public BigDecimal getServiceCharges() {
        return serviceCharges;
    }

    public void setServiceCharges(BigDecimal serviceCharges) {
        this.serviceCharges = serviceCharges;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public Boolean getPrescriptionRequired() {
        return prescriptionRequired;
    }

    public void setPrescriptionRequired(Boolean prescriptionRequired) {
        this.prescriptionRequired = prescriptionRequired;
    }

    public Boolean getPrescriptionUploaded() {
        return prescriptionUploaded;
    }

    public void setPrescriptionUploaded(Boolean prescriptionUploaded) {
        this.prescriptionUploaded = prescriptionUploaded;
    }

    public String getPrescriptionUrls() {
        return prescriptionUrls;
    }

    public void setPrescriptionUrls(String prescriptionUrls) {
        this.prescriptionUrls = prescriptionUrls;
    }

    public LocalDateTime getEstimatedDeliveryTime() {
        return estimatedDeliveryTime;
    }

    public void setEstimatedDeliveryTime(LocalDateTime estimatedDeliveryTime) {
        this.estimatedDeliveryTime = estimatedDeliveryTime;
    }

    public LocalDateTime getActualDeliveryTime() {
        return actualDeliveryTime;
    }

    public void setActualDeliveryTime(LocalDateTime actualDeliveryTime) {
        this.actualDeliveryTime = actualDeliveryTime;
    }

    public String getDeliveryOtp() {
        return deliveryOtp;
    }

    public void setDeliveryOtp(String deliveryOtp) {
        this.deliveryOtp = deliveryOtp;
    }

    public String getDeliveryProofUrl() {
        return deliveryProofUrl;
    }

    public void setDeliveryProofUrl(String deliveryProofUrl) {
        this.deliveryProofUrl = deliveryProofUrl;
    }

    public Integer getCustomerRating() {
        return customerRating;
    }

    public void setCustomerRating(Integer customerRating) {
        this.customerRating = customerRating;
    }

    public String getCustomerReview() {
        return customerReview;
    }

    public void setCustomerReview(String customerReview) {
        this.customerReview = customerReview;
    }

    public LocalDateTime getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(LocalDateTime cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    public User getCancelledBy() {
        return cancelledBy;
    }

    public void setCancelledBy(User cancelledBy) {
        this.cancelledBy = cancelledBy;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public RefundStatus getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(RefundStatus refundStatus) {
        this.refundStatus = refundStatus;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    // Helper methods
    public boolean isCancellable() {
        return orderStatus == OrderStatus.PLACED || orderStatus == OrderStatus.CONFIRMED;
    }

    public boolean isDelivered() {
        return orderStatus == OrderStatus.DELIVERED;
    }

    public boolean isCancelled() {
        return orderStatus == OrderStatus.CANCELLED;
    }

    public boolean requiresPayment() {
        return paymentMethod != PaymentMethod.COD;
    }

    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

    public void removeItem(OrderItem item) {
        items.remove(item);
        item.setOrder(null);
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", orderNumber='" + orderNumber + '\'' +
                ", orderStatus=" + orderStatus +
                ", totalAmount=" + totalAmount +
                ", paymentStatus=" + paymentStatus +
                '}';
    }
}