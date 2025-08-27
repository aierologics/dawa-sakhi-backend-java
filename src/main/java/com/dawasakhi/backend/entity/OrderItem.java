package com.dawasakhi.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "order_items", indexes = {
    @Index(name = "idx_order_item_order", columnList = "order_id"),
    @Index(name = "idx_order_item_medicine", columnList = "medicine_id")
})
public class OrderItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long orderItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @JsonIgnore
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "medicine_id", nullable = false)
    private Medicine medicine;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @NotNull(message = "Unit price is required")
    @PositiveOrZero(message = "Unit price must be positive")
    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @NotNull(message = "Total price is required")
    @PositiveOrZero(message = "Total price must be positive")
    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @PositiveOrZero(message = "Discount percentage must be positive")
    @Column(name = "discount_percentage", precision = 5, scale = 2)
    private BigDecimal discountPercentage = BigDecimal.ZERO;

    @PositiveOrZero(message = "Discount amount must be positive")
    @Column(name = "discount_amount", precision = 8, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @PositiveOrZero(message = "Tax percentage must be positive")
    @Column(name = "tax_percentage", precision = 5, scale = 2)
    private BigDecimal taxPercentage = BigDecimal.ZERO;

    @PositiveOrZero(message = "Tax amount must be positive")
    @Column(name = "tax_amount", precision = 8, scale = 2)
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @NotNull(message = "Final price is required")
    @PositiveOrZero(message = "Final price must be positive")
    @Column(name = "final_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal finalPrice;

    @Size(max = 50, message = "Batch number must be less than 50 characters")
    @Column(name = "batch_number", length = 50)
    private String batchNumber;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "substitute_medicine_id")
    private Medicine substituteMedicine;

    @Size(max = 200, message = "Substitute reason must be less than 200 characters")
    @Column(name = "substitute_reason", length = 200)
    private String substituteReason;

    @Column(name = "is_prescription_required", nullable = false)
    private Boolean isPrescriptionRequired = false;

    // Constructors
    public OrderItem() {
        super();
    }

    public OrderItem(Order order, Medicine medicine, Integer quantity, BigDecimal unitPrice) {
        this();
        this.order = order;
        this.medicine = medicine;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.isPrescriptionRequired = medicine.getPrescriptionRequired();
        calculatePrices();
    }

    // Business Logic
    @PrePersist
    @PreUpdate
    private void calculatePrices() {
        if (unitPrice != null && quantity != null) {
            this.totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
            
            // Calculate discount
            if (discountPercentage != null && discountPercentage.compareTo(BigDecimal.ZERO) > 0) {
                this.discountAmount = totalPrice.multiply(discountPercentage).divide(BigDecimal.valueOf(100));
            }
            
            // Calculate tax (GST = 5% by default)
            if (taxPercentage == null || taxPercentage.compareTo(BigDecimal.ZERO) == 0) {
                this.taxPercentage = BigDecimal.valueOf(5.0); // 5% GST
            }
            
            BigDecimal taxableAmount = totalPrice.subtract(discountAmount != null ? discountAmount : BigDecimal.ZERO);
            this.taxAmount = taxableAmount.multiply(taxPercentage).divide(BigDecimal.valueOf(100));
            
            // Calculate final price
            this.finalPrice = totalPrice
                    .subtract(discountAmount != null ? discountAmount : BigDecimal.ZERO)
                    .add(taxAmount != null ? taxAmount : BigDecimal.ZERO);
        }
    }

    // Getters and Setters
    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
        if (medicine != null) {
            this.isPrescriptionRequired = medicine.getPrescriptionRequired();
        }
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        calculatePrices();
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        calculatePrices();
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(BigDecimal discountPercentage) {
        this.discountPercentage = discountPercentage;
        calculatePrices();
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getTaxPercentage() {
        return taxPercentage;
    }

    public void setTaxPercentage(BigDecimal taxPercentage) {
        this.taxPercentage = taxPercentage;
        calculatePrices();
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(BigDecimal finalPrice) {
        this.finalPrice = finalPrice;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Medicine getSubstituteMedicine() {
        return substituteMedicine;
    }

    public void setSubstituteMedicine(Medicine substituteMedicine) {
        this.substituteMedicine = substituteMedicine;
    }

    public String getSubstituteReason() {
        return substituteReason;
    }

    public void setSubstituteReason(String substituteReason) {
        this.substituteReason = substituteReason;
    }

    public Boolean getIsPrescriptionRequired() {
        return isPrescriptionRequired;
    }

    public void setIsPrescriptionRequired(Boolean isPrescriptionRequired) {
        this.isPrescriptionRequired = isPrescriptionRequired;
    }

    // Helper methods
    public boolean hasSubstitute() {
        return substituteMedicine != null;
    }

    public Medicine getActualMedicine() {
        return hasSubstitute() ? substituteMedicine : medicine;
    }

    public String getActualMedicineName() {
        Medicine actualMedicine = getActualMedicine();
        return actualMedicine.getDisplayName();
    }

    public BigDecimal getSavingsAmount() {
        if (discountAmount != null && discountAmount.compareTo(BigDecimal.ZERO) > 0) {
            return discountAmount;
        }
        return BigDecimal.ZERO;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "orderItemId=" + orderItemId +
                ", medicine=" + (medicine != null ? medicine.getGenericName() : "null") +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", finalPrice=" + finalPrice +
                '}';
    }
}