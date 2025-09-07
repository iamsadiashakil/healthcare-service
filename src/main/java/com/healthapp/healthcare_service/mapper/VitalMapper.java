package com.healthapp.healthcare_service.mapper;

import com.healthapp.healthcare_service.dto.VitalDto;
import com.healthapp.healthcare_service.entity.Vital;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VitalMapper {
    VitalMapper INSTANCE = Mappers.getMapper(VitalMapper.class);

    @Mapping(target = "patientId", source = "patient.id")
    VitalDto vitalToVitalDto(Vital vital);

    @Mapping(target = "patient", ignore = true)
    Vital vitalDtoToVital(VitalDto vitalDto);

    List<VitalDto> vitalListToVitalDtoList(List<Vital> vitals);

    List<Vital> vitalDtoListToVitalList(List<VitalDto> vitalDtos);
}
