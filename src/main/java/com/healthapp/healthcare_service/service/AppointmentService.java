package com.healthapp.healthcare_service.service;

import com.healthapp.healthcare_service.dto.AppointmentDTO;
import com.healthapp.healthcare_service.entity.Appointment;
import com.healthapp.healthcare_service.entity.Doctor;
import com.healthapp.healthcare_service.entity.Patient;
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

    public Appointment findById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id " + id));
    }

    public Appointment save(AppointmentDTO appointmentDTO) {
        Patient patient = patientRepository.findById(appointmentDTO.getPatient().getId())
                .orElseThrow(() -> new RuntimeException("Patient not found with id " + appointmentDTO.getPatient().getId()));
        Doctor doctor = doctorRepository.findById(appointmentDTO.getDoctor().getId())
                .orElseThrow(() -> new RuntimeException("Doctor not found with id " + appointmentDTO.getDoctor().getId()));

        Appointment appointment = Appointment.builder()
                .status(appointmentDTO.getStatus())
                .appointmentDate(appointmentDTO.getAppointmentDate())
                .doctor(doctor)
                .patient(patient)
                .build();
        return appointmentRepository.save(appointment);
    }

    public Appointment update(Long id, AppointmentDTO updatedAppointment) {
        Appointment existing = findById(id);
        existing.setAppointmentDate(updatedAppointment.getAppointmentDate());
        existing.setStatus(updatedAppointment.getStatus());
        return appointmentRepository.save(existing);
    }

    public void delete(Long id) {
        appointmentRepository.deleteById(id);
    }
}

