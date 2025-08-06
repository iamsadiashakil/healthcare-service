package com.healthapp.healthcare_service.mapper;

import com.healthapp.healthcare_service.dto.MessageDto;
import com.healthapp.healthcare_service.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    @Mapping(source = "staff.id", target = "staffId")
    @Mapping(source = "patient.id", target = "patientId")
    MessageDto messageToMessageDto(Message message);

    @Mapping(source = "staffId", target = "staff.id")
    @Mapping(source = "patientId", target = "patient.id")
    Message messageDtoToMessage(MessageDto messageDto);
}
