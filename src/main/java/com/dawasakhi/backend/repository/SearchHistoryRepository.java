package com.dawasakhi.backend.repository;

import com.dawasakhi.backend.entity.SearchHistory;
import com.dawasakhi.backend.entity.User;
import com.dawasakhi.backend.service.SearchHistoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {

    Page<SearchHistory> findByUser(User user, Pageable pageable);
    
    Optional<SearchHistory> findByUserAndSearchTermAndCreatedAtAfter(User user, String searchTerm, LocalDateTime afterTime);
    
    @Query("SELECT sh.searchTerm FROM SearchHistory sh WHERE sh.user = :user GROUP BY sh.searchTerm ORDER BY SUM(sh.searchCount) DESC")
    List<String> findTopSearchTermsByUser(@Param("user") User user, Pageable pageable);
    
    @Query("SELECT sh.searchTerm FROM SearchHistory sh WHERE sh.user = :user ORDER BY sh.createdAt DESC")
    List<String> findRecentSearchTermsByUser(@Param("user") User user, Pageable pageable);
    
    @Query("SELECT sh.searchTerm FROM SearchHistory sh WHERE sh.createdAt >= :fromDate GROUP BY sh.searchTerm ORDER BY SUM(sh.searchCount) DESC")
    List<String> findTrendingSearchTerms(@Param("fromDate") LocalDateTime fromDate, Pageable pageable);
    
    @Query("SELECT sh.searchTerm FROM SearchHistory sh GROUP BY sh.searchTerm ORDER BY SUM(sh.searchCount) DESC")
    List<String> findPopularSearchTerms(Pageable pageable);
    
    @Query("SELECT sh.searchTerm FROM SearchHistory sh WHERE sh.user = :user AND sh.searchTerm ILIKE %:query% ORDER BY sh.searchCount DESC, sh.createdAt DESC")
    List<String> findUserSearchSuggestions(@Param("user") User user, @Param("query") String query, Pageable pageable);
    
    @Query("SELECT sh.searchTerm FROM SearchHistory sh WHERE sh.searchTerm ILIKE %:query% GROUP BY sh.searchTerm ORDER BY SUM(sh.searchCount) DESC")
    List<String> findGlobalSearchSuggestions(@Param("query") String query, Pageable pageable);
    
    void deleteByUser(User user);
    
    long countByUserAndCreatedAtBetween(User user, LocalDateTime fromDate, LocalDateTime toDate);
    
    @Query("SELECT COUNT(DISTINCT sh.searchTerm) FROM SearchHistory sh WHERE sh.user = :user AND sh.createdAt BETWEEN :fromDate AND :toDate")
    long countDistinctSearchTermsByUserAndDateRange(@Param("user") User user, @Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate);
    
    long countByUserAndSearchTypeAndCreatedAtBetween(User user, SearchHistory.SearchType searchType, LocalDateTime fromDate, LocalDateTime toDate);
    
    long countByCreatedAtBetween(LocalDateTime fromDate, LocalDateTime toDate);
    
    @Query("SELECT COUNT(DISTINCT sh.user) FROM SearchHistory sh WHERE sh.createdAt BETWEEN :fromDate AND :toDate")
    long countDistinctUsersByDateRange(@Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate);
    
    @Query("SELECT COUNT(DISTINCT sh.searchTerm) FROM SearchHistory sh WHERE sh.createdAt BETWEEN :fromDate AND :toDate")
    long countDistinctSearchTermsByDateRange(@Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate);
    
    long countBySearchTypeAndCreatedAtBetween(SearchHistory.SearchType searchType, LocalDateTime fromDate, LocalDateTime toDate);
    
    @Query("SELECT new com.dawasakhi.backend.service.SearchHistoryService$SearchTrendData(sh.searchTerm, SUM(sh.searchCount), MAX(sh.createdAt)) " +
           "FROM SearchHistory sh WHERE sh.createdAt >= :fromDate " +
           "GROUP BY sh.searchTerm ORDER BY SUM(sh.searchCount) DESC")
    List<SearchHistoryService.SearchTrendData> findSearchTrends(@Param("fromDate") LocalDateTime fromDate, Pageable pageable);
    
    boolean existsByUserAndSearchTerm(User user, String searchTerm);
    
    long countByUser(User user);
}