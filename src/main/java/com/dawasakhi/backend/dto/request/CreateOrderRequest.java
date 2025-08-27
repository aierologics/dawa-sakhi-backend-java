package com.dawasakhi.backend.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

public class CreateOrderRequest {

    @NotNull(message = "Delivery address ID is required")
    private Long deliveryAddressId;

    @NotEmpty(message = "Order items cannot be empty")
    @Size(min = 1, max = 50, message = "Order must contain 1-50 items")
    @Valid
    private List<OrderItemRequest> items;

    private String deliveryType = "STANDARD"; // STANDARD, EXPRESS, SCHEDULED

    private LocalDateTime scheduledDeliveryTime;

    @Size(max = 500, message = "Special instructions must be less than 500 characters")
    private String specialInstructions;

    @Size(max = 50, message = "Coupon code must be less than 50 characters")
    private String couponCode;

    private String paymentMethod = "COD"; // COD, UPI, CREDIT_CARD, DEBIT_CARD, NETBANKING, WALLET

    public CreateOrderRequest() {}

    // Getters and Setters
    public Long getDeliveryAddressId() {
        return deliveryAddressId;
    }

    public void setDeliveryAddressId(Long deliveryAddressId) {
        this.deliveryAddressId = deliveryAddressId;
    }

    public List<OrderItemRequest> getItems() {
        return items;
    }

    public void setItems(List<OrderItemRequest> items) {
        this.items = items;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
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

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public static class OrderItemRequest {
        @NotNull(message = "Medicine ID is required")
        private Long medicineId;

        @NotNull(message = "Quantity is required")
        private Integer quantity = 1;

        public OrderItemRequest() {}

        public Long getMedicineId() {
            return medicineId;
        }

        public void setMedicineId(Long medicineId) {
            this.medicineId = medicineId;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
    }
}