package com.dawasakhi.backend.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Document(collection = "search_history")
public class SearchHistory {

    @Id
    private String id;

    @Indexed
    private Long userId;

    @Indexed
    private String searchQuery;

    private String searchType = "medicine";

    private Integer resultsCount = 0;

    private Long clickedMedicineId;

    private String sessionId;

    private String ipAddress;

    private String userAgent;

    private Location location;

    private FiltersApplied filtersApplied;

    private Long searchTimeMs = 0L;

    @CreatedDate
    @Indexed(expireAfterSeconds = 7776000) // 90 days
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    // Nested classes
    public static class Location {
        private String city;
        private String state;
        private String country;
        private Coordinates coordinates;

        public static class Coordinates {
            private Double latitude;
            private Double longitude;

            // Getters and Setters
            public Double getLatitude() {
                return latitude;
            }

            public void setLatitude(Double latitude) {
                this.latitude = latitude;
            }

            public Double getLongitude() {
                return longitude;
            }

            public void setLongitude(Double longitude) {
                this.longitude = longitude;
            }
        }

        // Getters and Setters
        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public Coordinates getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(Coordinates coordinates) {
            this.coordinates = coordinates;
        }
    }

    public static class FiltersApplied {
        private List<String> category;
        private PriceRange priceRange;
        private Boolean prescriptionRequired;
        private List<String> brand;
        private List<String> manufacturer;

        public static class PriceRange {
            private Double min;
            private Double max;

            // Getters and Setters
            public Double getMin() {
                return min;
            }

            public void setMin(Double min) {
                this.min = min;
            }

            public Double getMax() {
                return max;
            }

            public void setMax(Double max) {
                this.max = max;
            }
        }

        // Getters and Setters
        public List<String> getCategory() {
            return category;
        }

        public void setCategory(List<String> category) {
            this.category = category;
        }

        public PriceRange getPriceRange() {
            return priceRange;
        }

        public void setPriceRange(PriceRange priceRange) {
            this.priceRange = priceRange;
        }

        public Boolean getPrescriptionRequired() {
            return prescriptionRequired;
        }

        public void setPrescriptionRequired(Boolean prescriptionRequired) {
            this.prescriptionRequired = prescriptionRequired;
        }

        public List<String> getBrand() {
            return brand;
        }

        public void setBrand(List<String> brand) {
            this.brand = brand;
        }

        public List<String> getManufacturer() {
            return manufacturer;
        }

        public void setManufacturer(List<String> manufacturer) {
            this.manufacturer = manufacturer;
        }
    }

    // Constructors
    public SearchHistory() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public SearchHistory(Long userId, String searchQuery, Integer resultsCount) {
        this();
        this.userId = userId;
        this.searchQuery = searchQuery;
        this.resultsCount = resultsCount;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public Integer getResultsCount() {
        return resultsCount;
    }

    public void setResultsCount(Integer resultsCount) {
        this.resultsCount = resultsCount;
    }

    public Long getClickedMedicineId() {
        return clickedMedicineId;
    }

    public void setClickedMedicineId(Long clickedMedicineId) {
        this.clickedMedicineId = clickedMedicineId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public FiltersApplied getFiltersApplied() {
        return filtersApplied;
    }

    public void setFiltersApplied(FiltersApplied filtersApplied) {
        this.filtersApplied = filtersApplied;
    }

    public Long getSearchTimeMs() {
        return searchTimeMs;
    }

    public void setSearchTimeMs(Long searchTimeMs) {
        this.searchTimeMs = searchTimeMs;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "SearchHistory{" +
                "id='" + id + '\'' +
                ", userId=" + userId +
                ", searchQuery='" + searchQuery + '\'' +
                ", resultsCount=" + resultsCount +
                ", createdAt=" + createdAt +
                '}';
    }
}