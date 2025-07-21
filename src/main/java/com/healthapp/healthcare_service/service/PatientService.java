package com.healthapp.healthcare_service.service;

import com.healthapp.healthcare_service.dto.PatientDTO;
import com.healthapp.healthcare_service.entity.Patient;
import com.healthapp.healthcare_service.exception.ResourceNotFoundException;
import com.healthapp.healthcare_service.mapper.PatientMapper;
import com.healthapp.healthcare_service.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    public PatientDTO createPatient(PatientDTO dto) {
        Patient patient = patientMapper.toEntity(dto);
        Patient savedPatient = patientRepository.save(patient);
        return patientMapper.toDTO(savedPatient);
    }


    public PatientDTO getPatient(Long id) {
        Patient patient = findById(id);
        return patientMapper.toDTO(patient);
    }

    private Patient findById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id " + id));
    }


    public List<PatientDTO> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();
        return patients.stream()
                .map(patientMapper::toDTO)
                .toList();
    }


    public PatientDTO updatePatient(Long id, PatientDTO updatedPatient) {
        findById(id);
        return createPatient(updatedPatient);
    }


    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }
}

