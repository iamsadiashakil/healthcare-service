package com.healthapp.healthcare_service.mapper;

import com.healthapp.healthcare_service.dto.PatientDto;
import com.healthapp.healthcare_service.entity.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    PatientMapper INSTANCE = Mappers.getMapper(PatientMapper.class);

    PatientDto patientToPatientDto(Patient patient);

    Patient patientDtoToPatient(PatientDto patientDto);
}


