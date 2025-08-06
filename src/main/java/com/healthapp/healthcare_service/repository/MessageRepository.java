package com.healthapp.healthcare_service.repository;

import com.healthapp.healthcare_service.entity.Message;
import com.healthapp.healthcare_service.entity.Patient;
import com.healthapp.healthcare_service.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByPatient(Patient patient);
    List<Message> findByPatientAndStaff(Patient patient, Staff staff);
}
