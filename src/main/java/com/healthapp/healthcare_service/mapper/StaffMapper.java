package com.healthapp.healthcare_service.mapper;

import com.healthapp.healthcare_service.dto.StaffDto;
import com.healthapp.healthcare_service.entity.Staff;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface StaffMapper {
    StaffMapper INSTANCE = Mappers.getMapper(StaffMapper.class);

    StaffDto staffToStaffDto(Staff staff);

    Staff staffDtoToStaff(StaffDto staffDto);
}
