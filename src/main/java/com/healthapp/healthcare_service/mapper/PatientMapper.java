package com.healthapp.healthcare_service.mapper;

import com.healthapp.healthcare_service.dto.PatientDTO;
import com.healthapp.healthcare_service.entity.Patient;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    PatientDTO toDTO(Patient patient);
    Patient toEntity(PatientDTO dto);
}
