package com.healthapp.healthcare_service.repository;

import com.healthapp.healthcare_service.entity.Allergy;
import com.healthapp.healthcare_service.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AllergyRepository extends JpaRepository<Allergy, Long> {
    List<Allergy> findByPatient(Patient patient);
}
