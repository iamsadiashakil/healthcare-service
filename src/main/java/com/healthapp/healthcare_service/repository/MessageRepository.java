package com.healthapp.healthcare_service.repository;

import com.healthapp.healthcare_service.entity.Message;
import com.healthapp.healthcare_service.entity.Patient;
import com.healthapp.healthcare_service.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByPatient(Patient patient);
    List<Message> findByPatientAndStaff(Patient patient, Staff staff);
    @Query("SELECT DISTINCT m.staff FROM Message m WHERE m.patient.id = :patientId")
    List<Staff> findDistinctStaffByPatientId(@Param("patientId") Long patientId);

    @Query("SELECT m FROM Message m WHERE m.patient.id = :patientId AND m.staff.id = :staffId ORDER BY m.timestamp DESC")
    List<Message> findMessagesByPatientAndStaff(@Param("patientId") Long patientId,
                                                @Param("staffId") Long staffId);
}
