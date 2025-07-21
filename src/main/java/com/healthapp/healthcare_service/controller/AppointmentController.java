package com.healthapp.healthcare_service.controller;

import com.healthapp.healthcare_service.dto.AppointmentDTO;
import com.healthapp.healthcare_service.entity.Appointment;
import com.healthapp.healthcare_service.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;

    @GetMapping
    public List<Appointment> getAllAppointments() {
        return appointmentService.findAll();
    }

    @GetMapping("/{id}")
    public Appointment getAppointmentById(@PathVariable Long id) {
        return appointmentService.findById(id);
    }

    @PostMapping
    public Appointment createAppointment(@Valid @RequestBody AppointmentDTO appointment) {
        return appointmentService.save(appointment);
    }

    @PutMapping("/{id}")
    public Appointment updateAppointment(@PathVariable Long id, @Valid @RequestBody AppointmentDTO appointment) {
        return appointmentService.update(id, appointment);
    }

    @DeleteMapping("/{id}")
    public void deleteAppointment(@PathVariable Long id) {
        appointmentService.delete(id);
    }
}

