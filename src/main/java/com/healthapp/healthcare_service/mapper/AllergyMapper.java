package com.healthapp.healthcare_service.mapper;

import com.healthapp.healthcare_service.dto.AllergyDto;
import com.healthapp.healthcare_service.entity.Allergy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AllergyMapper {
    AllergyMapper INSTANCE = Mappers.getMapper(AllergyMapper.class);

    @Mapping(target = "patientId", source = "patient.id")
    AllergyDto allergyToAllergyDto(Allergy allergy);

    @Mapping(target = "patient", ignore = true)
    Allergy allergyDtoToAllergy(AllergyDto allergyDto);
}

