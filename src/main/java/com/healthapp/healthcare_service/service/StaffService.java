package com.healthapp.healthcare_service.service;

import com.healthapp.healthcare_service.dto.*;
import com.healthapp.healthcare_service.entity.*;
import com.healthapp.healthcare_service.exception.ResourceNotFoundException;
import com.healthapp.healthcare_service.mapper.*;
import com.healthapp.healthcare_service.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StaffService {
    private final PatientRepository patientRepository;
    private final AllergyRepository allergyRepository;
    private final VitalRepository vitalRepository;
    private final AppointmentRepository appointmentRepository;
    private final MessageRepository messageRepository;
    private final StaffRepository staffRepository;

    private final PatientMapper patientMapper;
    private final AllergyMapper allergyMapper;
    private final VitalMapper vitalMapper;
    private final AppointmentMapper appointmentMapper;
    private final MessageMapper messageMapper;
    private final StaffMapper staffMapper;
    private final HealthcareProxyMapper healthcareProxyMapper;

    public List<PatientDto> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(patientMapper::patientToPatientDto)
                .collect(Collectors.toList());
    }

    public List<PatientDto> searchPatients(String query) {
        return patientRepository.findByNameContainingIgnoreCase(query).stream()
                .map(patientMapper::patientToPatientDto)
                .collect(Collectors.toList());
    }

    public PatientDto getPatientById(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));
        return patientMapper.patientToPatientDto(patient);
    }

    public List<VitalDto> getPatientVitals(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));

        return vitalRepository.findByPatient(patient).stream()
                .map(vitalMapper::vitalToVitalDto)
                .collect(Collectors.toList());
    }

    public List<VitalDto> getPatientVitalsByType(Long patientId, String type) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));

        return vitalRepository.findByPatientAndType(patient, type).stream()
                .map(vitalMapper::vitalToVitalDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public VitalDto addPatientVital(Long patientId, VitalDto vitalDto, String staffEmail) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));

        Staff staff = staffRepository.findByEmail(staffEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with email: " + staffEmail));

        Vital vital = vitalMapper.vitalDtoToVital(vitalDto);
        vital.setPatient(patient);
        vital.setMeasuredAt(LocalDateTime.now());

        Vital savedVital = vitalRepository.save(vital);
        return vitalMapper.vitalToVitalDto(savedVital);
    }

    public List<AllergyDto> getPatientAllergies(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));

        return allergyRepository.findByPatient(patient).stream()
                .map(allergyMapper::allergyToAllergyDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public AllergyDto addPatientAllergy(Long patientId, AllergyDto allergyDto, String staffEmail) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));

        Staff staff = staffRepository.findByEmail(staffEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with email: " + staffEmail));

        Allergy allergy = allergyMapper.allergyDtoToAllergy(allergyDto);
        allergy.setPatient(patient);
        allergy.setNotedOn(LocalDate.now());

        Allergy savedAllergy = allergyRepository.save(allergy);
        return allergyMapper.allergyToAllergyDto(savedAllergy);
    }

    public List<AppointmentDto> getStaffAppointments(String staffEmail) {
        Staff staff = staffRepository.findByEmail(staffEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with email: " + staffEmail));

        return appointmentRepository.findByStaff(staff).stream()
                .map(appointmentMapper::appointmentToAppointmentDto)
                .collect(Collectors.toList());
    }

    public AppointmentDto getAppointmentById(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + appointmentId));
        return appointmentMapper.appointmentToAppointmentDto(appointment);
    }

    @Transactional
    public AppointmentDto updateAppointment(Long appointmentId, AppointmentDto appointmentDto, String staffEmail) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + appointmentId));

        Staff staff = staffRepository.findByEmail(staffEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with email: " + staffEmail));

        if (!appointment.getStaff().getId().equals(staff.getId())) {
            throw new ResourceNotFoundException("Appointment does not belong to staff member");
        }

        appointment.setTime(appointmentDto.getTime());
        appointment.setStatus(appointmentDto.getStatus());
        appointment.setNotes(appointmentDto.getNotes());
        appointment.setPrescription(appointmentDto.getPrescription());

        Appointment updatedAppointment = appointmentRepository.save(appointment);
        return appointmentMapper.appointmentToAppointmentDto(updatedAppointment);
    }

    public List<MessageDto> getMessagesWithPatient(Long patientId, String staffEmail) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));

        Staff staff = staffRepository.findByEmail(staffEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with email: " + staffEmail));

        return messageRepository.findByPatientAndStaff(patient, staff).stream()
                .map(messageMapper::messageToMessageDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public MessageDto sendMessageToPatient(Long patientId, MessageDto messageDto, String staffEmail) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));

        Staff staff = staffRepository.findByEmail(staffEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with email: " + staffEmail));

        Message message = messageMapper.messageDtoToMessage(messageDto);
        message.setPatient(patient);
        message.setStaff(staff);
        message.setSenderType("STAFF");
        message.setTimestamp(LocalDateTime.now());

        Message savedMessage = messageRepository.save(message);
        return messageMapper.messageToMessageDto(savedMessage);
    }

    public StaffDto getStaffProfile(String staffEmail) {
        Staff staff = staffRepository.findByEmail(staffEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with email: " + staffEmail));
        return staffMapper.staffToStaffDto(staff);
    }

    @Transactional
    public StaffDto updateStaffProfile(StaffDto staffDto, String staffEmail) {
        Staff staff = staffRepository.findByEmail(staffEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with email: " + staffEmail));

        staff.setName(staffDto.getName());
        staff.setPhone(staffDto.getPhone());

        Staff updatedStaff = staffRepository.save(staff);
        return staffMapper.staffToStaffDto(updatedStaff);
    }

    public List<HealthcareProxyDto> getAssignedPatientsProxies(Long staffId) {
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + staffId));

        return staff.getPatients().stream()
                .map(patient -> healthcareProxyMapper.healthcareProxyToHealthcareProxyDto(patient.getHealthcareProxy()))
                .collect(Collectors.toList());
    }
}