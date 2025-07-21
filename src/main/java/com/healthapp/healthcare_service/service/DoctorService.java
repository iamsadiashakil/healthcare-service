package com.healthapp.healthcare_service.service;

import com.healthapp.healthcare_service.dto.DoctorDTO;
import com.healthapp.healthcare_service.entity.Doctor;
import com.healthapp.healthcare_service.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;

    public List<Doctor> findAll() {
        return doctorRepository.findAll();
    }

    public Doctor findById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id " + id));
    }

    public Doctor save(DoctorDTO doctorDTO) {
        Doctor doctor = Doctor.builder()
                .name(doctorDTO.getName())
                .phone(doctorDTO.getPhone())
                .specialization(doctorDTO.getSpecialization())
                .build();
        return doctorRepository.save(doctor);
    }

    public Doctor update(Long id, DoctorDTO updatedDoctor) {
        Doctor existing = findById(id);
        existing.setName(updatedDoctor.getName());
        existing.setSpecialization(updatedDoctor.getSpecialization());
        existing.setPhone(updatedDoctor.getPhone());
        return doctorRepository.save(existing);
    }

    public void delete(Long id) {
        doctorRepository.deleteById(id);
    }
}

