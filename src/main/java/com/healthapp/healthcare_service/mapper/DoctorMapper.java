package com.healthapp.healthcare_service.mapper;

import com.healthapp.healthcare_service.dto.DoctorDTO;
import com.healthapp.healthcare_service.entity.Doctor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
    DoctorDTO toDTO(Doctor doctor);
    Doctor toEntity(DoctorDTO dto);
}
