package com.dawasakhi.backend.controller;

import com.dawasakhi.backend.dto.response.ApiResponse;
import com.dawasakhi.backend.entity.Medicine;
import com.dawasakhi.backend.service.MedicineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicines")
@Tag(name = "Medicine Management", description = "Medicine management APIs")
public class MedicineController {

    private static final Logger logger = LoggerFactory.getLogger(MedicineController.class);

    @Autowired
    private MedicineService medicineService;

    @GetMapping("/search")
    @Operation(summary = "Search Medicines", description = "Search medicines by name or composition")
    public ResponseEntity<ApiResponse<Page<Medicine>>> searchMedicines(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "genericName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        Page<Medicine> medicines = medicineService.searchMedicines(q, page, size, sortBy, sortDir);
        
        return ResponseEntity.ok(
            ApiResponse.success("Medicines retrieved successfully", medicines)
        );
    }

    @GetMapping("/{medicineId}/details")
    @Operation(summary = "Get Medicine Details", description = "Get detailed information about a specific medicine")
    public ResponseEntity<ApiResponse<Medicine>> getMedicineDetails(@PathVariable Long medicineId) {
        Medicine medicine = medicineService.getMedicineById(medicineId);
        
        return ResponseEntity.ok(
            ApiResponse.success("Medicine details retrieved successfully", medicine)
        );
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Get Medicines by Category", description = "Get medicines filtered by therapeutic category")
    public ResponseEntity<ApiResponse<Page<Medicine>>> getMedicinesByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "genericName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        Page<Medicine> medicines = medicineService.getMedicinesByCategory(category, page, size, sortBy, sortDir);
        
        return ResponseEntity.ok(
            ApiResponse.success("Medicines retrieved successfully", medicines)
        );
    }

    @GetMapping("/featured")
    @Operation(summary = "Get Featured Medicines", description = "Get featured medicines")
    public ResponseEntity<ApiResponse<Page<Medicine>>> getFeaturedMedicines(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Page<Medicine> medicines = medicineService.getFeaturedMedicines(page, size);
        
        return ResponseEntity.ok(
            ApiResponse.success("Featured medicines retrieved successfully", medicines)
        );
    }

    @GetMapping("/categories")
    @Operation(summary = "Get Medicine Categories", description = "Get list of available medicine categories")
    public ResponseEntity<ApiResponse<List<String>>> getMedicineCategories() {
        List<String> categories = medicineService.getMedicineCategories();
        
        return ResponseEntity.ok(
            ApiResponse.success("Medicine categories retrieved successfully", categories)
        );
    }

    @GetMapping("/manufacturers")
    @Operation(summary = "Get Medicine Manufacturers", description = "Get list of medicine manufacturers")
    public ResponseEntity<ApiResponse<List<String>>> getMedicineManufacturers() {
        List<String> manufacturers = medicineService.getMedicineManufacturers();
        
        return ResponseEntity.ok(
            ApiResponse.success("Medicine manufacturers retrieved successfully", manufacturers)
        );
    }

    // Admin endpoints
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create Medicine", description = "Create a new medicine (Admin only)")
    public ResponseEntity<ApiResponse<Medicine>> createMedicine(@Valid @RequestBody Medicine medicine) {
        Medicine createdMedicine = medicineService.createMedicine(medicine);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponse.success("Medicine created successfully", createdMedicine)
        );
    }

    @PutMapping("/{medicineId}/update")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update Medicine", description = "Update medicine details (Admin only)")
    public ResponseEntity<ApiResponse<Medicine>> updateMedicine(
            @PathVariable Long medicineId,
            @Valid @RequestBody Medicine medicineUpdate) {
        
        Medicine updatedMedicine = medicineService.updateMedicine(medicineId, medicineUpdate);
        
        return ResponseEntity.ok(
            ApiResponse.success("Medicine updated successfully", updatedMedicine)
        );
    }

    @DeleteMapping("/{medicineId}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete Medicine", description = "Delete/discontinue a medicine (Admin only)")
    public ResponseEntity<ApiResponse<String>> deleteMedicine(@PathVariable Long medicineId) {
        medicineService.deleteMedicine(medicineId);
        
        return ResponseEntity.ok(
            ApiResponse.success("Medicine deleted successfully")
        );
    }

    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get All Medicines", description = "Get all medicines including inactive ones (Admin only)")
    public ResponseEntity<ApiResponse<Page<Medicine>>> getAllMedicines(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Page<Medicine> medicines = medicineService.getAllMedicines(page, size, sortBy, sortDir);
        
        return ResponseEntity.ok(
            ApiResponse.success("All medicines retrieved successfully", medicines)
        );
    }
}