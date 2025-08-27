package com.dawasakhi.backend.service;

import com.dawasakhi.backend.entity.Medicine;
import com.dawasakhi.backend.exception.ResourceNotFoundException;
import com.dawasakhi.backend.repository.MedicineRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class MedicineService {

    private static final Logger logger = LoggerFactory.getLogger(MedicineService.class);

    @Autowired
    private MedicineRepository medicineRepository;

    public Medicine getMedicineById(Long medicineId) {
        return medicineRepository.findById(medicineId)
            .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with id: " + medicineId, "MEDICINE_NOT_FOUND"));
    }

    public Page<Medicine> searchMedicines(String searchTerm, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            return medicineRepository.searchByNameOrComposition(searchTerm.trim(), pageable);
        }
        
        return medicineRepository.findByStatus(Medicine.MedicineStatus.ACTIVE, pageable);
    }

    public Page<Medicine> getMedicinesByCategory(String category, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        
        return medicineRepository.findByTherapeuticCategoryAndStatus(category, Medicine.MedicineStatus.ACTIVE, pageable);
    }

    public Page<Medicine> getFeaturedMedicines(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        
        return medicineRepository.findByIsFeaturedAndStatus(true, Medicine.MedicineStatus.ACTIVE, pageable);
    }

    public List<Medicine> getMedicinesByIds(List<Long> medicineIds) {
        return medicineRepository.findAllById(medicineIds);
    }

    public Medicine createMedicine(Medicine medicine) {
        medicine.setStatus(Medicine.MedicineStatus.ACTIVE);
        medicine.setCreatedAt(LocalDateTime.now());
        medicine.setUpdatedAt(LocalDateTime.now());
        
        Medicine savedMedicine = medicineRepository.save(medicine);
        logger.info("Medicine created: {}", savedMedicine.getGenericName());
        
        return savedMedicine;
    }

    public Medicine updateMedicine(Long medicineId, Medicine medicineUpdate) {
        Medicine medicine = getMedicineById(medicineId);
        
        // Update fields
        if (medicineUpdate.getGenericName() != null) {
            medicine.setGenericName(medicineUpdate.getGenericName());
        }
        if (medicineUpdate.getBrandName() != null) {
            medicine.setBrandName(medicineUpdate.getBrandName());
        }
        if (medicineUpdate.getManufacturer() != null) {
            medicine.setManufacturer(medicineUpdate.getManufacturer());
        }
        if (medicineUpdate.getComposition() != null) {
            medicine.setComposition(medicineUpdate.getComposition());
        }
        if (medicineUpdate.getMedicineForm() != null) {
            medicine.setMedicineForm(medicineUpdate.getMedicineForm());
        }
        if (medicineUpdate.getStrength() != null) {
            medicine.setStrength(medicineUpdate.getStrength());
        }
        if (medicineUpdate.getMrp() != null) {
            medicine.setMrp(medicineUpdate.getMrp());
        }
        if (medicineUpdate.getDiscountPercentage() != null) {
            medicine.setDiscountPercentage(medicineUpdate.getDiscountPercentage());
        }
        if (medicineUpdate.getPrescriptionRequired() != null) {
            medicine.setPrescriptionRequired(medicineUpdate.getPrescriptionRequired());
        }
        if (medicineUpdate.getTherapeuticCategory() != null) {
            medicine.setTherapeuticCategory(medicineUpdate.getTherapeuticCategory());
        }
        if (medicineUpdate.getIsFeatured() != null) {
            medicine.setIsFeatured(medicineUpdate.getIsFeatured());
        }
        
        medicine.setUpdatedAt(LocalDateTime.now());
        Medicine savedMedicine = medicineRepository.save(medicine);
        
        logger.info("Medicine updated: {}", savedMedicine.getGenericName());
        return savedMedicine;
    }

    public void deleteMedicine(Long medicineId) {
        Medicine medicine = getMedicineById(medicineId);
        medicine.setStatus(Medicine.MedicineStatus.DISCONTINUED);
        medicine.setUpdatedAt(LocalDateTime.now());
        
        medicineRepository.save(medicine);
        logger.info("Medicine deleted: {}", medicine.getGenericName());
    }

    public Page<Medicine> getAllMedicines(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        
        return medicineRepository.findAll(pageable);
    }

    public List<String> getMedicineCategories() {
        return medicineRepository.findDistinctTherapeuticCategories();
    }

    public List<String> getMedicineManufacturers() {
        return medicineRepository.findDistinctManufacturers();
    }
}