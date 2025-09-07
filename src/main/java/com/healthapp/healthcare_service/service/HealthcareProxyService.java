package com.healthapp.healthcare_service.service;

import com.healthapp.healthcare_service.dto.*;
import com.healthapp.healthcare_service.entity.*;
import com.healthapp.healthcare_service.exception.ResourceNotFoundException;
import com.healthapp.healthcare_service.mapper.*;
import com.healthapp.healthcare_service.repository.HealthcareProxyRepository;
import com.healthapp.healthcare_service.repository.MessageRepository;
import com.healthapp.healthcare_service.repository.StaffRepository;
import com.healthapp.healthcare_service.repository.VitalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HealthcareProxyService {
    private final HealthcareProxyRepository proxyRepository;
    private final MessageRepository messageRepository;
    private final StaffRepository staffRepository;
    private final VitalRepository vitalRepository;

    private final AllergyMapper allergyMapper;
    private final HealthcareProxyMapper proxyMapper;
    private final PatientMapper patientMapper;
    private final MessageMapper messageMapper;
    private final VitalMapper vitalMapper;

    public HealthcareProxyDto getProxyProfile(String email) {
        HealthcareProxy proxy = proxyRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Healthcare proxy not found"));
        return proxyMapper.healthcareProxyToHealthcareProxyDto(proxy);
    }

    @Transactional
    public HealthcareProxyDto updateProxyProfile(HealthcareProxyDto proxyDto, String email) {
        HealthcareProxy proxy = proxyRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Healthcare proxy not found"));

        proxy.setName(proxyDto.getName());
        proxy.setPhone(proxyDto.getPhone());
        proxy.setRelationship(proxyDto.getRelationship());

        HealthcareProxy updatedProxy = proxyRepository.save(proxy);
        return proxyMapper.healthcareProxyToHealthcareProxyDto(updatedProxy);
    }

    public PatientDto getAssignedPatient(String email) {
        HealthcareProxy proxy = proxyRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Healthcare proxy not found"));

        if (proxy.getPatient() == null) {
            throw new ResourceNotFoundException("No patient assigned to this healthcare proxy");
        }

        return patientMapper.patientToPatientDto(proxy.getPatient());
    }

    @Transactional
    public MessageDto sendMessageToStaff(Long staffId, MessageDto messageDto, String email) {
        HealthcareProxy proxy = proxyRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Healthcare proxy not found"));

        if (proxy.getPatient() == null) {
            throw new ResourceNotFoundException("No patient assigned to this healthcare proxy");
        }

        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));

        Message message = messageMapper.messageDtoToMessage(messageDto);
        message.setPatient(proxy.getPatient());
        message.setStaff(staff);
        message.setHealthcareProxy(proxy);
        message.setSenderType("PATIENT_PROXY");
        message.setTimestamp(LocalDateTime.now());

        Message savedMessage = messageRepository.save(message);
        return messageMapper.messageToMessageDto(savedMessage);
    }

    public VitalsSummaryDto getVitalsSummary(String username) {
        // 1. Get the healthcare proxy
        HealthcareProxy proxy = proxyRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("Healthcare proxy not found"));

        // 2. Get the associated patient
        Patient patient = proxy.getPatient();
        if (patient == null) {
            throw new ResourceNotFoundException("No patient assigned to this proxy");
        }

        // 3. Get latest vitals
        List<Vital> vitals = vitalRepository.findLatestReadingsByPatientId(patient.getId());
        List<VitalDto> vitalDtos = vitalMapper.vitalListToVitalDtoList(vitals);
        Map<String, VitalDto> latestVitals = vitalDtos.stream()
                .collect(Collectors.toMap(VitalDto::getType, v -> v));


        // 4. Create and return DTO
        return VitalsSummaryDto.builder()
                .latestReadings(latestVitals)
                .lastUpdated(LocalDateTime.now().toString())
                .build();
    }


    public List<ConversationDto> getConversations(String username) {
        // 1. Get healthcare proxy and their patient
        HealthcareProxy proxy = proxyRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("Healthcare proxy not found"));

        Patient patient = Optional.ofNullable(proxy.getPatient())
                .orElseThrow(() -> new ResourceNotFoundException("No patient assigned to this proxy"));

        // 2. Get all distinct staff members who have messaged with this patient
        List<Staff> staffList = messageRepository.findDistinctStaffByPatientId(patient.getId());

        // 3. Convert to ConversationDto list
        return staffList.stream()
                .map(staff -> mapToConversationDto(patient, staff))
                .sorted(Comparator.comparing(ConversationDto::getLastMessageTime).reversed())
                .collect(Collectors.toList());
    }

    private ConversationDto mapToConversationDto(Patient patient, Staff staff) {
        // Get all messages between this patient and staff
        List<Message> messages = messageRepository.findMessagesByPatientAndStaff(
                patient.getId(), staff.getId());

        // Get last message
        Message lastMessage = messages.stream()
                .max(Comparator.comparing(Message::getTimestamp))
                .orElse(null);

        // Map to DTO
        ConversationDto dto = new ConversationDto();
        dto.setStaffId(staff.getId());
        dto.setStaffName(staff.getName());
        dto.setStaffSpecialization(staff.getRole());

        if (lastMessage != null) {
            dto.setLastMessage(lastMessage.getText());
            dto.setLastMessageTime(lastMessage.getTimestamp());
        }

        // Map all messages
        dto.setMessages(messages.stream()
                .map(this::mapToMessageDto)
                .collect(Collectors.toList()));

        return dto;
    }

    private MessageDto mapToMessageDto(Message message) {
        MessageDto dto = new MessageDto();
        dto.setText(message.getText());
        dto.setSenderType(message.getSenderType());
        dto.setTimestamp(message.getTimestamp());
        return dto;
    }

    public List<AllergyDto> getAllergies(String username) {
        // 1. Get the healthcare proxy
        HealthcareProxy proxy = proxyRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("Healthcare proxy not found"));

        // 2. Get the associated patient
        Patient assignedPatient = proxy.getPatient();
        if (assignedPatient == null) {
            throw new ResourceNotFoundException("No patient assigned to this proxy");
        }

        return assignedPatient.getAllergies().stream().map(allergyMapper::allergyToAllergyDto).toList();
    }

}