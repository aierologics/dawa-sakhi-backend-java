package com.dawasakhi.backend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {

    @JsonProperty("orderId")
    private Long orderId;

    @JsonProperty("orderNumber")
    private String orderNumber;

    @JsonProperty("customer")
    private CustomerSummary customer;

    @JsonProperty("deliveryAddress")
    private AddressSummary deliveryAddress;

    @JsonProperty("orderStatus")
    private String orderStatus;

    @JsonProperty("deliveryType")
    private String deliveryType;

    @JsonProperty("items")
    private List<OrderItemResponse> items;

    @JsonProperty("pricing")
    private PricingDetails pricing;

    @JsonProperty("paymentMethod")
    private String paymentMethod;

    @JsonProperty("paymentStatus")
    private String paymentStatus;

    @JsonProperty("paymentId")
    private String paymentId;

    @JsonProperty("prescriptionRequired")
    private Boolean prescriptionRequired;

    @JsonProperty("prescriptionUploaded")
    private Boolean prescriptionUploaded;

    @JsonProperty("specialInstructions")
    private String specialInstructions;

    @JsonProperty("deliveryTracking")
    private DeliveryTracking deliveryTracking;

    @JsonProperty("timestamps")
    private OrderTimestamps timestamps;

    public OrderResponse() {}

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

    public CustomerSummary getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerSummary customer) {
        this.customer = customer;
    }

    public AddressSummary getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(AddressSummary deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public List<OrderItemResponse> getItems() {
        return items;
    }

    public void setItems(List<OrderItemResponse> items) {
        this.items = items;
    }

    public PricingDetails getPricing() {
        return pricing;
    }

    public void setPricing(PricingDetails pricing) {
        this.pricing = pricing;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
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

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

    public DeliveryTracking getDeliveryTracking() {
        return deliveryTracking;
    }

    public void setDeliveryTracking(DeliveryTracking deliveryTracking) {
        this.deliveryTracking = deliveryTracking;
    }

    public OrderTimestamps getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(OrderTimestamps timestamps) {
        this.timestamps = timestamps;
    }

    // Nested Classes
    public static class CustomerSummary {
        private Long userId;
        private String fullName;
        private String phoneNumber;

        public CustomerSummary() {}

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }
    }

    public static class AddressSummary {
        private Long addressId;
        private String fullName;
        private String phoneNumber;
        private String addressLine1;
        private String city;
        private String state;
        private String postalCode;

        public AddressSummary() {}

        // Getters and Setters
        public Long getAddressId() { return addressId; }
        public void setAddressId(Long addressId) { this.addressId = addressId; }
        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        public String getPhoneNumber() { return phoneNumber; }
        public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
        public String getAddressLine1() { return addressLine1; }
        public void setAddressLine1(String addressLine1) { this.addressLine1 = addressLine1; }
        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }
        public String getState() { return state; }
        public void setState(String state) { this.state = state; }
        public String getPostalCode() { return postalCode; }
        public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
    }

    public static class OrderItemResponse {
        private Long orderItemId;
        private MedicineSummary medicine;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal totalPrice;
        private BigDecimal finalPrice;
        private Boolean isPrescriptionRequired;

        public OrderItemResponse() {}

        // Getters and Setters
        public Long getOrderItemId() { return orderItemId; }
        public void setOrderItemId(Long orderItemId) { this.orderItemId = orderItemId; }
        public MedicineSummary getMedicine() { return medicine; }
        public void setMedicine(MedicineSummary medicine) { this.medicine = medicine; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        public BigDecimal getUnitPrice() { return unitPrice; }
        public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
        public BigDecimal getTotalPrice() { return totalPrice; }
        public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
        public BigDecimal getFinalPrice() { return finalPrice; }
        public void setFinalPrice(BigDecimal finalPrice) { this.finalPrice = finalPrice; }
        public Boolean getIsPrescriptionRequired() { return isPrescriptionRequired; }
        public void setIsPrescriptionRequired(Boolean isPrescriptionRequired) { this.isPrescriptionRequired = isPrescriptionRequired; }
    }

    public static class MedicineSummary {
        private Long medicineId;
        private String genericName;
        private String brandName;
        private String manufacturer;
        private String strength;
        private String medicineForm;

        public MedicineSummary() {}

        // Getters and Setters
        public Long getMedicineId() { return medicineId; }
        public void setMedicineId(Long medicineId) { this.medicineId = medicineId; }
        public String getGenericName() { return genericName; }
        public void setGenericName(String genericName) { this.genericName = genericName; }
        public String getBrandName() { return brandName; }
        public void setBrandName(String brandName) { this.brandName = brandName; }
        public String getManufacturer() { return manufacturer; }
        public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }
        public String getStrength() { return strength; }
        public void setStrength(String strength) { this.strength = strength; }
        public String getMedicineForm() { return medicineForm; }
        public void setMedicineForm(String medicineForm) { this.medicineForm = medicineForm; }
    }

    public static class PricingDetails {
        private BigDecimal subtotal;
        private BigDecimal deliveryCharges;
        private BigDecimal serviceCharges;
        private BigDecimal discountAmount;
        private BigDecimal taxAmount;
        private BigDecimal totalAmount;
        private String couponCode;

        public PricingDetails() {}

        // Getters and Setters
        public BigDecimal getSubtotal() { return subtotal; }
        public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
        public BigDecimal getDeliveryCharges() { return deliveryCharges; }
        public void setDeliveryCharges(BigDecimal deliveryCharges) { this.deliveryCharges = deliveryCharges; }
        public BigDecimal getServiceCharges() { return serviceCharges; }
        public void setServiceCharges(BigDecimal serviceCharges) { this.serviceCharges = serviceCharges; }
        public BigDecimal getDiscountAmount() { return discountAmount; }
        public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }
        public BigDecimal getTaxAmount() { return taxAmount; }
        public void setTaxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; }
        public BigDecimal getTotalAmount() { return totalAmount; }
        public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
        public String getCouponCode() { return couponCode; }
        public void setCouponCode(String couponCode) { this.couponCode = couponCode; }
    }

    public static class DeliveryTracking {
        private String deliveryOtp;
        
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime estimatedDeliveryTime;
        
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime actualDeliveryTime;
        
        private String deliveryProofUrl;

        public DeliveryTracking() {}

        // Getters and Setters
        public String getDeliveryOtp() { return deliveryOtp; }
        public void setDeliveryOtp(String deliveryOtp) { this.deliveryOtp = deliveryOtp; }
        public LocalDateTime getEstimatedDeliveryTime() { return estimatedDeliveryTime; }
        public void setEstimatedDeliveryTime(LocalDateTime estimatedDeliveryTime) { this.estimatedDeliveryTime = estimatedDeliveryTime; }
        public LocalDateTime getActualDeliveryTime() { return actualDeliveryTime; }
        public void setActualDeliveryTime(LocalDateTime actualDeliveryTime) { this.actualDeliveryTime = actualDeliveryTime; }
        public String getDeliveryProofUrl() { return deliveryProofUrl; }
        public void setDeliveryProofUrl(String deliveryProofUrl) { this.deliveryProofUrl = deliveryProofUrl; }
    }

    public static class OrderTimestamps {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime createdAt;
        
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime updatedAt;
        
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime scheduledDeliveryTime;

        public OrderTimestamps() {}

        // Getters and Setters
        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
        public LocalDateTime getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
        public LocalDateTime getScheduledDeliveryTime() { return scheduledDeliveryTime; }
        public void setScheduledDeliveryTime(LocalDateTime scheduledDeliveryTime) { this.scheduledDeliveryTime = scheduledDeliveryTime; }
    }
}