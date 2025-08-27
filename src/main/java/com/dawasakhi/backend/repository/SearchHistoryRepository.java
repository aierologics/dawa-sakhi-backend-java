package com.dawasakhi.backend.repository;

import com.dawasakhi.backend.entity.SearchHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SearchHistoryRepository extends MongoRepository<SearchHistory, String> {

    Page<SearchHistory> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    
    List<SearchHistory> findByUserIdAndSearchQueryOrderByCreatedAtDesc(Long userId, String searchQuery);
    
    @Query("{'userId': ?0, 'createdAt': {'$gte': ?1}}")
    Page<SearchHistory> findByUserIdAndCreatedAtAfter(Long userId, LocalDateTime fromDate, Pageable pageable);
    
    @Query("{'searchQuery': {'$regex': ?0, '$options': 'i'}}")
    Page<SearchHistory> findBySearchQueryContainingIgnoreCase(String searchTerm, Pageable pageable);
    
    @Query("{'createdAt': {'$gte': ?0, '$lte': ?1}}")
    List<SearchHistory> findByCreatedAtBetween(LocalDateTime fromDate, LocalDateTime toDate);
    
    @Query("{'userId': ?0, 'clickedMedicineId': {'$ne': null}}")
    List<SearchHistory> findByUserIdWithClicks(Long userId);
    
    long countByUserId(Long userId);
    
    long countBySearchQuery(String searchQuery);
    
    @Query(value = "{'createdAt': {'$gte': ?0}}", count = true)
    long countSearchesSince(LocalDateTime fromDate);
    
    // Aggregation queries for analytics
    @Aggregation(pipeline = {
        "{ '$match': { 'createdAt': { '$gte': ?0 } } }",
        "{ '$group': { '_id': '$searchQuery', 'count': { '$sum': 1 }, 'avgResults': { '$avg': '$resultsCount' } } }",
        "{ '$sort': { 'count': -1 } }",
        "{ '$limit': ?1 }"
    })
    List<PopularSearch> getPopularSearches(LocalDateTime fromDate, int limit);
    
    @Aggregation(pipeline = {
        "{ '$match': { 'userId': ?0 } }",
        "{ '$group': { '_id': '$searchQuery', 'count': { '$sum': 1 }, 'lastSearched': { '$max': '$createdAt' } } }",
        "{ '$sort': { 'lastSearched': -1 } }",
        "{ '$limit': ?1 }"
    })
    List<UserSearchHistory> getUserRecentSearches(Long userId, int limit);
    
    @Aggregation(pipeline = {
        "{ '$match': { 'createdAt': { '$gte': ?0 } } }",
        "{ '$group': { '_id': { '$dateToString': { 'format': '%Y-%m-%d', 'date': '$createdAt' } }, 'totalSearches': { '$sum': 1 }, 'uniqueUsers': { '$addToSet': '$userId' } } }",
        "{ '$project': { 'date': '$_id', 'totalSearches': 1, 'uniqueUsers': { '$size': '$uniqueUsers' } } }",
        "{ '$sort': { 'date': 1 } }"
    })
    List<DailySearchStats> getDailySearchStats(LocalDateTime fromDate);
    
    @Aggregation(pipeline = {
        "{ '$match': { 'clickedMedicineId': { '$ne': null } } }",
        "{ '$group': { '_id': '$clickedMedicineId', 'clickCount': { '$sum': 1 }, 'searchQueries': { '$addToSet': '$searchQuery' } } }",
        "{ '$sort': { 'clickCount': -1 } }",
        "{ '$limit': ?0 }"
    })
    List<PopularMedicine> getMostClickedMedicines(int limit);
    
    // Interface for aggregation results
    interface PopularSearch {
        String getId(); // search query
        long getCount();
        double getAvgResults();
    }
    
    interface UserSearchHistory {
        String getId(); // search query
        long getCount();
        LocalDateTime getLastSearched();
    }
    
    interface DailySearchStats {
        String getDate();
        long getTotalSearches();
        long getUniqueUsers();
    }
    
    interface PopularMedicine {
        Long getId(); // medicine id
        long getClickCount();
        List<String> getSearchQueries();
    }
}