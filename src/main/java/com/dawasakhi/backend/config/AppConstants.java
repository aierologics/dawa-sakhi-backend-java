package com.dawasakhi.backend.config;

public class AppConstants {
    
    // User Roles
    public static final String ROLE_CUSTOMER = "CUSTOMER";
    public static final String ROLE_PHARMACY = "PHARMACY";
    public static final String ROLE_DELIVERY = "DELIVERY";
    public static final String ROLE_ADMIN = "ADMIN";
    
    // Order Status
    public static final String ORDER_PLACED = "PLACED";
    public static final String ORDER_CONFIRMED = "CONFIRMED";
    public static final String ORDER_PREPARING = "PREPARING";
    public static final String ORDER_READY_FOR_PICKUP = "READY_FOR_PICKUP";
    public static final String ORDER_OUT_FOR_DELIVERY = "OUT_FOR_DELIVERY";
    public static final String ORDER_DELIVERED = "DELIVERED";
    public static final String ORDER_CANCELLED = "CANCELLED";
    public static final String ORDER_RETURNED = "RETURNED";
    
    // Payment Status
    public static final String PAYMENT_PENDING = "PENDING";
    public static final String PAYMENT_PROCESSING = "PROCESSING";
    public static final String PAYMENT_COMPLETED = "COMPLETED";
    public static final String PAYMENT_FAILED = "FAILED";
    public static final String PAYMENT_REFUNDED = "REFUNDED";
    public static final String PAYMENT_CANCELLED = "CANCELLED";
    
    // Delivery Types
    public static final String DELIVERY_STANDARD = "STANDARD";
    public static final String DELIVERY_EXPRESS = "EXPRESS";
    public static final String DELIVERY_SCHEDULED = "SCHEDULED";
    
    // Account Status
    public static final String ACCOUNT_ACTIVE = "ACTIVE";
    public static final String ACCOUNT_INACTIVE = "INACTIVE";
    public static final String ACCOUNT_SUSPENDED = "SUSPENDED";
    public static final String ACCOUNT_DELETED = "DELETED";
    
    // OTP Types
    public static final String OTP_REGISTRATION = "REGISTRATION";
    public static final String OTP_LOGIN = "LOGIN";
    public static final String OTP_FORGOT_PASSWORD = "FORGOT_PASSWORD";
    public static final String OTP_PHONE_VERIFICATION = "PHONE_VERIFICATION";
    public static final String OTP_DELIVERY_CONFIRMATION = "DELIVERY_CONFIRMATION";
    
    // Medicine Categories
    public static final String CATEGORY_PRESCRIPTION = "PRESCRIPTION";
    public static final String CATEGORY_OTC = "OVER_THE_COUNTER";
    public static final String CATEGORY_AYURVEDIC = "AYURVEDIC";
    public static final String CATEGORY_HOMEOPATHIC = "HOMEOPATHIC";
    public static final String CATEGORY_SUPPLEMENTS = "SUPPLEMENTS";
    public static final String CATEGORY_BABY_CARE = "BABY_CARE";
    public static final String CATEGORY_PERSONAL_CARE = "PERSONAL_CARE";
    public static final String CATEGORY_HEALTH_DEVICES = "HEALTH_DEVICES";
    
    // Medicine Forms
    public static final String FORM_TABLET = "TABLET";
    public static final String FORM_CAPSULE = "CAPSULE";
    public static final String FORM_SYRUP = "SYRUP";
    public static final String FORM_INJECTION = "INJECTION";
    public static final String FORM_CREAM = "CREAM";
    public static final String FORM_OINTMENT = "OINTMENT";
    public static final String FORM_DROPS = "DROPS";
    public static final String FORM_INHALER = "INHALER";
    public static final String FORM_PATCH = "PATCH";
    public static final String FORM_POWDER = "POWDER";
    
    // Error Codes
    public static final String ERROR_VALIDATION = "VALIDATION_ERROR";
    public static final String ERROR_AUTHENTICATION = "AUTHENTICATION_ERROR";
    public static final String ERROR_AUTHORIZATION = "AUTHORIZATION_ERROR";
    public static final String ERROR_RESOURCE_NOT_FOUND = "RESOURCE_NOT_FOUND";
    public static final String ERROR_DUPLICATE_RESOURCE = "DUPLICATE_RESOURCE";
    public static final String ERROR_EXTERNAL_SERVICE = "EXTERNAL_SERVICE_ERROR";
    public static final String ERROR_RATE_LIMIT_EXCEEDED = "RATE_LIMIT_EXCEEDED";
    public static final String ERROR_INTERNAL_SERVER = "INTERNAL_SERVER_ERROR";
    
    // Cache Keys
    public static final String CACHE_USER_PROFILE = "user:profile:";
    public static final String CACHE_MEDICINE_DETAILS = "medicine:details:";
    public static final String CACHE_SEARCH_RESULTS = "search:results:";
    public static final String CACHE_OTP = "otp:";
    public static final String CACHE_TOKEN_BLACKLIST = "blacklist:";
    
    // Cache TTL (in seconds)
    public static final long CACHE_TTL_SHORT = 300L;     // 5 minutes
    public static final long CACHE_TTL_MEDIUM = 1800L;   // 30 minutes
    public static final long CACHE_TTL_LONG = 3600L;     // 1 hour
    public static final long CACHE_TTL_OTP = 300L;       // 5 minutes
    
    // Pagination
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;
    
    // API Versions
    public static final String API_V1 = "/api/v1";
    
    private AppConstants() {
        // Private constructor to prevent instantiation
    }
}