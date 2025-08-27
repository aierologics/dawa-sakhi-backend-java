package com.dawasakhi.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "medicines", indexes = {
    @Index(name = "idx_medicine_generic_name", columnList = "genericName"),
    @Index(name = "idx_medicine_brand_name", columnList = "brandName"),
    @Index(name = "idx_medicine_manufacturer", columnList = "manufacturer"),
    @Index(name = "idx_medicine_category", columnList = "therapeuticCategory"),
    @Index(name = "idx_medicine_prescription", columnList = "prescriptionRequired"),
    @Index(name = "idx_medicine_status", columnList = "status"),
    @Index(name = "idx_medicine_featured", columnList = "isFeatured")
})
public class Medicine extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medicine_id")
    private Long medicineId;

    @NotBlank(message = "Generic name is required")
    @Size(max = 200, message = "Generic name must be less than 200 characters")
    @Column(name = "generic_name", nullable = false, length = 200)
    private String genericName;

    @Size(max = 200, message = "Brand name must be less than 200 characters")
    @Column(name = "brand_name", length = 200)
    private String brandName;

    @NotBlank(message = "Manufacturer is required")
    @Size(max = 100, message = "Manufacturer must be less than 100 characters")
    @Column(name = "manufacturer", nullable = false, length = 100)
    private String manufacturer;

    @NotBlank(message = "Composition is required")
    @Column(name = "composition", nullable = false, columnDefinition = "TEXT")
    private String composition;

    @Enumerated(EnumType.STRING)
    @Column(name = "medicine_form", nullable = false)
    private MedicineForm medicineForm;

    @Size(max = 50, message = "Strength must be less than 50 characters")
    @Column(name = "strength", length = 50)
    private String strength;

    @Size(max = 50, message = "Pack size must be less than 50 characters")
    @Column(name = "pack_size", length = 50)
    private String packSize;

    @Size(max = 50, message = "Pack type must be less than 50 characters")
    @Column(name = "pack_type", length = 50)
    private String packType;

    @NotNull(message = "MRP is required")
    @PositiveOrZero(message = "MRP must be positive")
    @Column(name = "mrp", nullable = false, precision = 10, scale = 2)
    private BigDecimal mrp;

    @PositiveOrZero(message = "Discount percentage must be positive")
    @Column(name = "discount_percentage", precision = 5, scale = 2)
    private BigDecimal discountPercentage = BigDecimal.ZERO;

    @Column(name = "selling_price", precision = 10, scale = 2)
    private BigDecimal sellingPrice;

    @Column(name = "prescription_required", nullable = false)
    private Boolean prescriptionRequired = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "schedule_type")
    private ScheduleType scheduleType;

    @Size(max = 20, message = "HSN code must be less than 20 characters")
    @Column(name = "hsn_code", length = 20)
    private String hsnCode;

    @Size(max = 100, message = "Therapeutic category must be less than 100 characters")
    @Column(name = "therapeutic_category", length = 100)
    private String therapeuticCategory;

    @Size(max = 100, message = "Drug category must be less than 100 characters")
    @Column(name = "drug_category", length = 100)
    private String drugCategory;

    @Size(max = 50, message = "Drug license number must be less than 50 characters")
    @Column(name = "drug_license_number", length = 50)
    private String drugLicenseNumber;

    @Column(name = "storage_instructions", columnDefinition = "TEXT")
    private String storageInstructions;

    @Column(name = "contraindications", columnDefinition = "TEXT")
    private String contraindications;

    @Column(name = "side_effects", columnDefinition = "TEXT")
    private String sideEffects;

    @Column(name = "usage_instructions", columnDefinition = "TEXT")
    private String usageInstructions;

    @Column(name = "dosage_instructions", columnDefinition = "TEXT")
    private String dosageInstructions;

    @Size(max = 100, message = "Age restrictions must be less than 100 characters")
    @Column(name = "age_restrictions", length = 100)
    private String ageRestrictions;

    @Size(max = 10, message = "Pregnancy category must be less than 10 characters")
    @Column(name = "pregnancy_category", length = 10)
    private String pregnancyCategory;

    @Column(name = "image_urls", columnDefinition = "TEXT")
    private String imageUrls; // JSON array as string

    @Column(name = "tags", columnDefinition = "TEXT")
    private String tags; // JSON array as string

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private MedicineStatus status = MedicineStatus.ACTIVE;

    @Column(name = "is_featured", nullable = false)
    private Boolean isFeatured = false;

    @Column(name = "search_keywords", columnDefinition = "TEXT")
    private String searchKeywords;

    @Column(name = "min_order_quantity", nullable = false)
    private Integer minOrderQuantity = 1;

    @Column(name = "max_order_quantity", nullable = false)
    private Integer maxOrderQuantity = 10;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Size(max = 50, message = "Batch number must be less than 50 characters")
    @Column(name = "batch_number", length = 50)
    private String batchNumber;

    // Relationships
    @OneToMany(mappedBy = "medicine", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<OrderItem> orderItems = new ArrayList<>();

    // Enums
    public enum MedicineForm {
        TABLET, CAPSULE, SYRUP, INJECTION, CREAM, OINTMENT, 
        DROPS, INHALER, PATCH, POWDER
    }

    public enum ScheduleType {
        SCHEDULE_H, SCHEDULE_H1, SCHEDULE_X, NARCOTIC
    }

    public enum MedicineStatus {
        ACTIVE, INACTIVE, DISCONTINUED
    }

    // Constructors
    public Medicine() {
        super();
    }

    public Medicine(String genericName, String manufacturer, String composition, 
                   MedicineForm medicineForm, BigDecimal mrp) {
        this();
        this.genericName = genericName;
        this.manufacturer = manufacturer;
        this.composition = composition;
        this.medicineForm = medicineForm;
        this.mrp = mrp;
        calculateSellingPrice();
    }

    // Business Logic
    @PrePersist
    @PreUpdate
    private void calculateSellingPrice() {
        if (mrp != null && discountPercentage != null) {
            BigDecimal discountAmount = mrp.multiply(discountPercentage).divide(BigDecimal.valueOf(100));
            this.sellingPrice = mrp.subtract(discountAmount);
        }
    }

    // Getters and Setters
    public Long getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(Long medicineId) {
        this.medicineId = medicineId;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getComposition() {
        return composition;
    }

    public void setComposition(String composition) {
        this.composition = composition;
    }

    public MedicineForm getMedicineForm() {
        return medicineForm;
    }

    public void setMedicineForm(MedicineForm medicineForm) {
        this.medicineForm = medicineForm;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public String getPackSize() {
        return packSize;
    }

    public void setPackSize(String packSize) {
        this.packSize = packSize;
    }

    public String getPackType() {
        return packType;
    }

    public void setPackType(String packType) {
        this.packType = packType;
    }

    public BigDecimal getMrp() {
        return mrp;
    }

    public void setMrp(BigDecimal mrp) {
        this.mrp = mrp;
        calculateSellingPrice();
    }

    public BigDecimal getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(BigDecimal discountPercentage) {
        this.discountPercentage = discountPercentage;
        calculateSellingPrice();
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public Boolean getPrescriptionRequired() {
        return prescriptionRequired;
    }

    public void setPrescriptionRequired(Boolean prescriptionRequired) {
        this.prescriptionRequired = prescriptionRequired;
    }

    public ScheduleType getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(ScheduleType scheduleType) {
        this.scheduleType = scheduleType;
    }

    public String getHsnCode() {
        return hsnCode;
    }

    public void setHsnCode(String hsnCode) {
        this.hsnCode = hsnCode;
    }

    public String getTherapeuticCategory() {
        return therapeuticCategory;
    }

    public void setTherapeuticCategory(String therapeuticCategory) {
        this.therapeuticCategory = therapeuticCategory;
    }

    public String getDrugCategory() {
        return drugCategory;
    }

    public void setDrugCategory(String drugCategory) {
        this.drugCategory = drugCategory;
    }

    public String getDrugLicenseNumber() {
        return drugLicenseNumber;
    }

    public void setDrugLicenseNumber(String drugLicenseNumber) {
        this.drugLicenseNumber = drugLicenseNumber;
    }

    public String getStorageInstructions() {
        return storageInstructions;
    }

    public void setStorageInstructions(String storageInstructions) {
        this.storageInstructions = storageInstructions;
    }

    public String getContraindications() {
        return contraindications;
    }

    public void setContraindications(String contraindications) {
        this.contraindications = contraindications;
    }

    public String getSideEffects() {
        return sideEffects;
    }

    public void setSideEffects(String sideEffects) {
        this.sideEffects = sideEffects;
    }

    public String getUsageInstructions() {
        return usageInstructions;
    }

    public void setUsageInstructions(String usageInstructions) {
        this.usageInstructions = usageInstructions;
    }

    public String getDosageInstructions() {
        return dosageInstructions;
    }

    public void setDosageInstructions(String dosageInstructions) {
        this.dosageInstructions = dosageInstructions;
    }

    public String getAgeRestrictions() {
        return ageRestrictions;
    }

    public void setAgeRestrictions(String ageRestrictions) {
        this.ageRestrictions = ageRestrictions;
    }

    public String getPregnancyCategory() {
        return pregnancyCategory;
    }

    public void setPregnancyCategory(String pregnancyCategory) {
        this.pregnancyCategory = pregnancyCategory;
    }

    public String getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(String imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public MedicineStatus getStatus() {
        return status;
    }

    public void setStatus(MedicineStatus status) {
        this.status = status;
    }

    public Boolean getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(Boolean isFeatured) {
        this.isFeatured = isFeatured;
    }

    public String getSearchKeywords() {
        return searchKeywords;
    }

    public void setSearchKeywords(String searchKeywords) {
        this.searchKeywords = searchKeywords;
    }

    public Integer getMinOrderQuantity() {
        return minOrderQuantity;
    }

    public void setMinOrderQuantity(Integer minOrderQuantity) {
        this.minOrderQuantity = minOrderQuantity;
    }

    public Integer getMaxOrderQuantity() {
        return maxOrderQuantity;
    }

    public void setMaxOrderQuantity(Integer maxOrderQuantity) {
        this.maxOrderQuantity = maxOrderQuantity;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    // Helper methods
    public boolean isActive() {
        return status == MedicineStatus.ACTIVE;
    }

    public boolean isExpired() {
        return expiryDate != null && expiryDate.isBefore(LocalDate.now());
    }

    public String getDisplayName() {
        if (brandName != null && !brandName.isEmpty()) {
            return brandName + " (" + genericName + ")";
        }
        return genericName;
    }

    @Override
    public String toString() {
        return "Medicine{" +
                "medicineId=" + medicineId +
                ", genericName='" + genericName + '\'' +
                ", brandName='" + brandName + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", medicineForm=" + medicineForm +
                ", mrp=" + mrp +
                ", sellingPrice=" + sellingPrice +
                ", status=" + status +
                '}';
    }
}