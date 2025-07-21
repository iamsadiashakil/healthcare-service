package com.healthapp.healthcare_service.controller;

import com.healthapp.healthcare_service.dto.DoctorDTO;
import com.healthapp.healthcare_service.entity.Doctor;
import com.healthapp.healthcare_service.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;

    @GetMapping
    public List<Doctor> getAllDoctors() {
        return doctorService.findAll();
    }

    @GetMapping("/{id}")
    public Doctor getDoctorById(@PathVariable Long id) {
        return doctorService.findById(id);
    }

    @PostMapping
    public Doctor createDoctor(@Valid @RequestBody DoctorDTO doctor) {
        return doctorService.save(doctor);
    }

    @PutMapping("/{id}")
    public Doctor updateDoctor(@PathVariable Long id, @Valid @RequestBody DoctorDTO doctor) {
        return doctorService.update(id, doctor);
    }

    @DeleteMapping("/{id}")
    public void deleteDoctor(@PathVariable Long id) {
        doctorService.delete(id);
    }
}

