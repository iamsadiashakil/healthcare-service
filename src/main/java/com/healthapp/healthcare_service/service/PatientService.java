package com.healthapp.healthcare_service.service;

import com.healthapp.healthcare_service.dto.AllergyDto;
import com.healthapp.healthcare_service.dto.MessageDto;
import com.healthapp.healthcare_service.dto.StaffDto;
import com.healthapp.healthcare_service.dto.VitalDto;
import com.healthapp.healthcare_service.entity.Patient;
import com.healthapp.healthcare_service.entity.Staff;
import com.healthapp.healthcare_service.exception.ResourceNotFoundException;
import com.healthapp.healthcare_service.mapper.AllergyMapper;
import com.healthapp.healthcare_service.mapper.MessageMapper;
import com.healthapp.healthcare_service.mapper.StaffMapper;
import com.healthapp.healthcare_service.mapper.VitalMapper;
import com.healthapp.healthcare_service.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final AllergyRepository allergyRepository;
    private final VitalRepository vitalRepository;
    private final MessageRepository messageRepository;
    private final PatientRepository patientRepository;
    private final StaffRepository staffRepository;

    private final AllergyMapper allergyMapper;
    private final VitalMapper vitalMapper;
    private final MessageMapper messageMapper;
    private final StaffMapper staffMapper;

    public List<AllergyDto> getAllergiesByPatientId(Long patientId) {
        Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));

        return allergyRepository.findByPatient(patient).stream().map(allergyMapper::allergyToAllergyDto).collect(Collectors.toList());
    }

    public List<VitalDto> getVitalsByPatientId(Long patientId) {
        Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));

        return vitalRepository.findByPatient(patient).stream().map(vitalMapper::vitalToVitalDto).collect(Collectors.toList());
    }

    public List<VitalDto> getVitalsByPatientIdAndType(Long patientId, String type) {
        Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));

        return vitalRepository.findByPatientAndType(patient, type).stream().map(vitalMapper::vitalToVitalDto).collect(Collectors.toList());
    }

    public List<MessageDto> getMessagesByPatientId(Long patientId) {
        Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));

        return messageRepository.findByPatient(patient).stream().map(messageMapper::messageToMessageDto).collect(Collectors.toList());
    }

    public List<MessageDto> getMessagesByPatientIdAndStaffId(Long patientId, Long staffId) {
        Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));

        Staff staff = staffRepository.findById(staffId).orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + staffId));

        return messageRepository.findByPatientAndStaff(patient, staff).stream().map(messageMapper::messageToMessageDto).collect(Collectors.toList());
    }

    public List<StaffDto> getStaffForPatient(Long patientId) {
        patientRepository.findById(patientId).orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));

        // In a real app, this would fetch staff associated with the patient (e.g., through appointments)
        return staffRepository.findAll().stream().map(staffMapper::staffToStaffDto).collect(Collectors.toList());
    }
}