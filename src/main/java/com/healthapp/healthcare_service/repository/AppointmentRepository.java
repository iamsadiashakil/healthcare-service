package com.healthapp.healthcare_service.repository;

import com.healthapp.healthcare_service.entity.Appointment;
import com.healthapp.healthcare_service.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByStaff(Staff staff);
}
