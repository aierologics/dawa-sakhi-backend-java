package com.dawasakhi.backend.repository;

import com.dawasakhi.backend.entity.Medicine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {

    Optional<Medicine> findByMedicineIdAndStatus(Long medicineId, Medicine.MedicineStatus status);
    
    Page<Medicine> findByStatusOrderByIsFeaturedDescCreatedAtDesc(Medicine.MedicineStatus status, Pageable pageable);
    
    Page<Medicine> findByIsFeaturedTrueAndStatusOrderByCreatedAtDesc(Medicine.MedicineStatus status, Pageable pageable);
    
    List<Medicine> findByIsFeaturedTrueAndStatusOrderByCreatedAtDesc(Medicine.MedicineStatus status);
    
    Page<Medicine> findByTherapeuticCategoryAndStatusOrderByCreatedAtDesc(String therapeuticCategory, Medicine.MedicineStatus status, Pageable pageable);
    
    Page<Medicine> findByManufacturerAndStatusOrderByCreatedAtDesc(String manufacturer, Medicine.MedicineStatus status, Pageable pageable);
    
    Page<Medicine> findByMedicineFormAndStatusOrderByCreatedAtDesc(Medicine.MedicineForm medicineForm, Medicine.MedicineStatus status, Pageable pageable);
    
    Page<Medicine> findByPrescriptionRequiredAndStatusOrderByCreatedAtDesc(Boolean prescriptionRequired, Medicine.MedicineStatus status, Pageable pageable);
    
    @Query("SELECT m FROM Medicine m WHERE m.status = :status AND " +
           "(LOWER(m.genericName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(m.brandName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(m.manufacturer) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(m.composition) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(m.therapeuticCategory) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(m.searchKeywords) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
           "ORDER BY m.isFeatured DESC, m.createdAt DESC")
    Page<Medicine> searchMedicines(@Param("searchTerm") String searchTerm, 
                                  @Param("status") Medicine.MedicineStatus status, 
                                  Pageable pageable);
    
    @Query("SELECT m FROM Medicine m WHERE m.status = :status AND " +
           "m.sellingPrice BETWEEN :minPrice AND :maxPrice " +
           "ORDER BY m.isFeatured DESC, m.createdAt DESC")
    Page<Medicine> findByPriceRange(@Param("minPrice") BigDecimal minPrice, 
                                   @Param("maxPrice") BigDecimal maxPrice, 
                                   @Param("status") Medicine.MedicineStatus status, 
                                   Pageable pageable);
    
    @Query("SELECT m FROM Medicine m WHERE m.status = :status AND " +
           "m.therapeuticCategory = :category AND " +
           "m.prescriptionRequired = :prescriptionRequired " +
           "ORDER BY m.isFeatured DESC, m.createdAt DESC")
    Page<Medicine> findByCategoryAndPrescriptionRequired(@Param("category") String category,
                                                        @Param("prescriptionRequired") Boolean prescriptionRequired,
                                                        @Param("status") Medicine.MedicineStatus status,
                                                        Pageable pageable);
    
    @Query("SELECT m FROM Medicine m WHERE m.status = :status AND m.medicineId != :excludeId AND " +
           "(m.therapeuticCategory = :category OR " +
           "LOWER(m.composition) LIKE LOWER(CONCAT('%', :compositionKeyword, '%')) OR " +
           "m.manufacturer = :manufacturer) " +
           "ORDER BY " +
           "CASE WHEN m.therapeuticCategory = :category THEN 1 ELSE 2 END, " +
           "m.createdAt DESC")
    List<Medicine> findSimilarMedicines(@Param("excludeId") Long excludeId,
                                       @Param("category") String category,
                                       @Param("compositionKeyword") String compositionKeyword,
                                       @Param("manufacturer") String manufacturer,
                                       @Param("status") Medicine.MedicineStatus status,
                                       Pageable pageable);
    
    @Query("SELECT DISTINCT m.therapeuticCategory, COUNT(m) as count FROM Medicine m " +
           "WHERE m.status = :status AND m.therapeuticCategory IS NOT NULL " +
           "GROUP BY m.therapeuticCategory " +
           "HAVING COUNT(m) > 0 " +
           "ORDER BY count DESC")
    List<Object[]> findCategoriesWithCount(@Param("status") Medicine.MedicineStatus status);
    
    @Query("SELECT DISTINCT m.manufacturer, COUNT(m) as count FROM Medicine m " +
           "WHERE m.status = :status AND m.manufacturer IS NOT NULL " +
           "GROUP BY m.manufacturer " +
           "HAVING COUNT(m) > 0 " +
           "ORDER BY count DESC")
    List<Object[]> findManufacturersWithCount(@Param("status") Medicine.MedicineStatus status);
    
    @Query("SELECT COUNT(m) FROM Medicine m WHERE m.status = 'ACTIVE'")
    long countActiveMedicines();
    
    @Query("SELECT COUNT(m) FROM Medicine m WHERE m.isFeatured = true AND m.status = 'ACTIVE'")
    long countFeaturedMedicines();
    
    @Query("SELECT COUNT(m) FROM Medicine m WHERE m.prescriptionRequired = true AND m.status = 'ACTIVE'")
    long countPrescriptionMedicines();
    
    List<Medicine> findByGenericNameIgnoreCaseAndStatus(String genericName, Medicine.MedicineStatus status);
    
    List<Medicine> findByBrandNameIgnoreCaseAndStatus(String brandName, Medicine.MedicineStatus status);
    
    boolean existsByGenericNameIgnoreCaseAndManufacturerIgnoreCase(String genericName, String manufacturer);
    
    @Query("SELECT m FROM Medicine m WHERE m.status = 'ACTIVE' AND " +
           "(LOWER(m.genericName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(m.brandName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(m.composition) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<Medicine> searchByNameOrComposition(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    Page<Medicine> findByStatus(Medicine.MedicineStatus status, Pageable pageable);
    
    Page<Medicine> findByTherapeuticCategoryAndStatus(String category, Medicine.MedicineStatus status, Pageable pageable);
    
    Page<Medicine> findByIsFeaturedAndStatus(Boolean featured, Medicine.MedicineStatus status, Pageable pageable);
    
    @Query("SELECT DISTINCT m.therapeuticCategory FROM Medicine m WHERE m.status = 'ACTIVE' AND m.therapeuticCategory IS NOT NULL ORDER BY m.therapeuticCategory")
    List<String> findDistinctTherapeuticCategories();
    
    @Query("SELECT DISTINCT m.manufacturer FROM Medicine m WHERE m.status = 'ACTIVE' AND m.manufacturer IS NOT NULL ORDER BY m.manufacturer")
    List<String> findDistinctManufacturers();
}