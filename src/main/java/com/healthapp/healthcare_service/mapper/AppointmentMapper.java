package com.healthapp.healthcare_service.mapper;

import com.healthapp.healthcare_service.dto.AppointmentDto;
import com.healthapp.healthcare_service.entity.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    AppointmentMapper INSTANCE = Mappers.getMapper(AppointmentMapper.class);

    @Mapping(source = "patient.id", target = "patientId")
    @Mapping(source = "staff.id", target = "staffId")
    AppointmentDto appointmentToAppointmentDto(Appointment appointment);

    @Mapping(source = "patientId", target = "patient.id")
    @Mapping(source = "staffId", target = "staff.id")
    Appointment appointmentDtoToAppointment(AppointmentDto appointmentDto);
}

