package com.healthapp.healthcare_service.service;

import com.healthapp.healthcare_service.dto.DoctorDTO;
import com.healthapp.healthcare_service.entity.Doctor;
import com.healthapp.healthcare_service.exception.ResourceNotFoundException;
import com.healthapp.healthcare_service.mapper.DoctorMapper;
import com.healthapp.healthcare_service.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;

    public List<DoctorDTO> findAll() {
        List<Doctor> doctors = doctorRepository.findAll();
        return doctors.stream()
                .map(doctorMapper::toDTO)
                .toList();
    }

    public DoctorDTO findById(Long id) {
        Doctor doctor = getDoctor(id);
        return doctorMapper.toDTO(doctor);
    }

    private Doctor getDoctor(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id " + id));
    }

    public DoctorDTO save(DoctorDTO doctorDTO) {
        Doctor doctor = doctorMapper.toEntity(doctorDTO);
        Doctor savedDoctor = doctorRepository.save(doctor);
        return doctorMapper.toDTO(savedDoctor);
    }

    public DoctorDTO update(Long id, DoctorDTO updatedDoctor) {
        findById(id);
        return save(updatedDoctor);
    }

    public void delete(Long id) {
        doctorRepository.deleteById(id);
    }
}

