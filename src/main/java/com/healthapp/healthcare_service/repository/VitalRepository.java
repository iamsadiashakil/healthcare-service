package com.healthapp.healthcare_service.repository;

import com.healthapp.healthcare_service.dto.VitalDto;
import com.healthapp.healthcare_service.entity.Patient;
import com.healthapp.healthcare_service.entity.Vital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface VitalRepository extends JpaRepository<Vital, Long> {
    List<Vital> findByPatient(Patient patient);
    List<Vital> findByPatientAndType(Patient patient, String type);
//    @Query("SELECT new map(v.type as type, (v.reading as reading, v.status as status)) " +
//            "FROM Vital v " +
//            "WHERE v.patient.id = :patientId " +
//            "AND v.measuredAt = (SELECT MAX(v2.measuredAt) FROM Vital v2 WHERE v2.patient.id = :patientId AND v2.type = v.type) " +
//            "GROUP BY v.type")
//    Map<String, VitalDto> findLatestReadingsByPatientId(@Param("patientId") Long patientId);

    @Query("SELECT v FROM Vital v " +
            "WHERE v.patient.id = :patientId " +
            "AND v.measuredAt = (" +
            "   SELECT MAX(v2.measuredAt) " +
            "   FROM Vital v2 " +
            "   WHERE v2.patient.id = :patientId AND v2.type = v.type" +
            ") " +
            "GROUP BY v.type, v.reading, v.status, v.id, v.patient, v.measuredAt")
    List<Vital> findLatestReadingsByPatientId(@Param("patientId") Long patientId);
}
