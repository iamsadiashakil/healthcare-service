package com.healthapp.healthcare_service.service;

import com.healthapp.healthcare_service.dto.HealthcareProxyDto;
import com.healthapp.healthcare_service.dto.MessageDto;
import com.healthapp.healthcare_service.dto.PatientDto;
import com.healthapp.healthcare_service.entity.HealthcareProxy;
import com.healthapp.healthcare_service.entity.Message;
import com.healthapp.healthcare_service.entity.Staff;
import com.healthapp.healthcare_service.exception.ResourceNotFoundException;
import com.healthapp.healthcare_service.mapper.HealthcareProxyMapper;
import com.healthapp.healthcare_service.mapper.MessageMapper;
import com.healthapp.healthcare_service.mapper.PatientMapper;
import com.healthapp.healthcare_service.repository.HealthcareProxyRepository;
import com.healthapp.healthcare_service.repository.MessageRepository;
import com.healthapp.healthcare_service.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class HealthcareProxyService {
    private final HealthcareProxyRepository proxyRepository;
    private final MessageRepository messageRepository;
    private final StaffRepository staffRepository;

    private final HealthcareProxyMapper proxyMapper;
    private final PatientMapper patientMapper;
    private final MessageMapper messageMapper;

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
}