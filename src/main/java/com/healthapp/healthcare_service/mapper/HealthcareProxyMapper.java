package com.healthapp.healthcare_service.mapper;

import com.healthapp.healthcare_service.dto.HealthcareProxyDto;
import com.healthapp.healthcare_service.entity.HealthcareProxy;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface HealthcareProxyMapper {

    PatientMapper INSTANCE = Mappers.getMapper(PatientMapper.class);

    HealthcareProxyDto healthcareProxyToHealthcareProxyDto(HealthcareProxy healthcareProxy);

    HealthcareProxy healthcareProxyDtoToHealthcareProxy(HealthcareProxyDto healthcareProxyDto);
}