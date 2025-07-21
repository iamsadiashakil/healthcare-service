package com.healthapp.healthcare_service.service;

import com.healthapp.healthcare_service.dto.PatientDTO;
import com.healthapp.healthcare_service.entity.Patient;
import com.healthapp.healthcare_service.exception.ResourceNotFoundException;
import com.healthapp.healthcare_service.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {
    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Patient createPatient(PatientDTO dto) {
        Patient patient = new Patient();
        patient.setName(dto.getName());
        patient.setDob(dto.getDob());
        patient.setGender(dto.getGender());
        patient.setPhone(dto.getPhone());
        return patientRepository.save(patient);
    }


    public Patient getPatient(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id " + id));
    }


    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }


    public Patient updatePatient(Long id, PatientDTO updatedPatient) {
        Patient existing = getPatient(id);
        existing.setName(updatedPatient.getName());
        existing.setDob(updatedPatient.getDob());
        existing.setGender(updatedPatient.getGender());
        existing.setPhone(updatedPatient.getPhone());
        return patientRepository.save(existing);
    }


    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }
}

