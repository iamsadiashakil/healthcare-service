package com.healthapp.healthcare_service.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ConversationDto {
    private Long staffId;

    private String staffName;

    private String staffSpecialization;

    private String lastMessage;

    private LocalDateTime lastMessageTime;

    private List<MessageDto> messages;
}
