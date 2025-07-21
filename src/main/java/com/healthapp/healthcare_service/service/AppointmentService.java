package com.healthapp.healthcare_service.service;

import com.healthapp.healthcare_service.dto.AppointmentDTO;
import com.healthapp.healthcare_service.entity.Appointment;
import com.healthapp.healthcare_service.entity.Doctor;
import com.healthapp.healthcare_service.entity.Patient;
import com.healthapp.healthcare_service.exception.ResourceNotFoundException;
import com.healthapp.healthcare_service.mapper.AppointmentMapper;
import com.healthapp.healthcare_service.repository.AppointmentRepository;
import com.healthapp.healthcare_service.repository.DoctorRepository;
import com.healthapp.healthcare_service.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentMapper appointmentMapper;

    public List<AppointmentDTO> findAll() {
        List<Appointment> appointments = appointmentRepository.findAll();
        return appointments.stream()
                .map(appointmentMapper::toDTO)
                .toList();
    }

    public AppointmentDTO findById(Long id) {
        Appointment appointment = getById(id);
        return appointmentMapper.toDTO(appointment);
    }

    public AppointmentDTO save(AppointmentDTO appointmentDTO) {
        Patient patient = patientRepository.findById(appointmentDTO.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        Doctor doctor = doctorRepository.findById(appointmentDTO.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        Appointment appointment = appointmentMapper.toEntity(appointmentDTO);
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);

        Appointment savedAppointment = appointmentRepository.save(appointment);
        return appointmentMapper.toDTO(savedAppointment);
    }

    public AppointmentDTO update(Long id, AppointmentDTO updatedAppointment) {
        getById(id);
        return save(updatedAppointment);
    }

    private Appointment getById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id " + id));
    }

    public void delete(Long id) {
        appointmentRepository.deleteById(id);
    }
}

