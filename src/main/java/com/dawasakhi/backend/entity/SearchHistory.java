package com.dawasakhi.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "search_history", indexes = {
    @Index(name = "idx_search_history_user", columnList = "user_id"),
    @Index(name = "idx_search_history_term", columnList = "search_term"),
    @Index(name = "idx_search_history_created", columnList = "created_at")
})
public class SearchHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "search_id")
    private Long searchId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotBlank(message = "Search term is required")
    @Size(max = 255, message = "Search term must be less than 255 characters")
    @Column(name = "search_term", nullable = false)
    private String searchTerm;

    @Enumerated(EnumType.STRING)
    @Column(name = "search_type", nullable = false)
    private SearchType searchType = SearchType.MEDICINE;

    @Column(name = "result_count")
    private Integer resultCount = 0;

    @Column(name = "search_count", nullable = false)
    private Integer searchCount = 1;

    @Column(name = "clicked_medicine_id")
    private Long clickedMedicineId;

    @Column(name = "search_filters", columnDefinition = "TEXT")
    private String searchFilters; // JSON string for applied filters

    public enum SearchType {
        MEDICINE, CATEGORY, PHARMACY, BRAND
    }

    // Constructors
    public SearchHistory() {
        super();
    }

    public SearchHistory(User user, String searchTerm, SearchType searchType) {
        this();
        this.user = user;
        this.searchTerm = searchTerm;
        this.searchType = searchType;
    }

    // Getters and Setters
    public Long getSearchId() {
        return searchId;
    }

    public void setSearchId(Long searchId) {
        this.searchId = searchId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public SearchType getSearchType() {
        return searchType;
    }

    public void setSearchType(SearchType searchType) {
        this.searchType = searchType;
    }

    public Integer getResultCount() {
        return resultCount;
    }

    public void setResultCount(Integer resultCount) {
        this.resultCount = resultCount;
    }

    public Integer getSearchCount() {
        return searchCount;
    }

    public void setSearchCount(Integer searchCount) {
        this.searchCount = searchCount;
    }

    public Long getClickedMedicineId() {
        return clickedMedicineId;
    }

    public void setClickedMedicineId(Long clickedMedicineId) {
        this.clickedMedicineId = clickedMedicineId;
    }

    public String getSearchFilters() {
        return searchFilters;
    }

    public void setSearchFilters(String searchFilters) {
        this.searchFilters = searchFilters;
    }

    @Override
    public String toString() {
        return "SearchHistory{" +
                "searchId=" + searchId +
                ", searchTerm='" + searchTerm + '\'' +
                ", searchType=" + searchType +
                ", resultCount=" + resultCount +
                ", searchCount=" + searchCount +
                '}';
    }
}