package com.healthapp.healthcare_service.service;

import com.healthapp.healthcare_service.entity.Patient;
import com.healthapp.healthcare_service.entity.Staff;
import com.healthapp.healthcare_service.repository.PatientRepository;
import com.healthapp.healthcare_service.repository.StaffRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UnifiedUserDetailsService implements UserDetailsService {

    private final PatientRepository patientRepository;
    private final StaffRepository staffRepository;

    public UnifiedUserDetailsService(PatientRepository patientRepository, StaffRepository staffRepository) {
        this.patientRepository = patientRepository;
        this.staffRepository = staffRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // First try to find a staff member
        Staff staff = staffRepository.findByEmail(username)
                .orElse(null);

        if (staff != null) {
            return staff;
        }

        // If not found as staff, try as patient
        Patient patient = patientRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        return patient;
    }
}
