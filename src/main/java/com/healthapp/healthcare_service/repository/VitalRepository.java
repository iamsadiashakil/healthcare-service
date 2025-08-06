package com.healthapp.healthcare_service.repository;

import com.healthapp.healthcare_service.entity.Patient;
import com.healthapp.healthcare_service.entity.Vital;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VitalRepository extends JpaRepository<Vital, Long> {
    List<Vital> findByPatient(Patient patient);
    List<Vital> findByPatientAndType(Patient patient, String type);
}
