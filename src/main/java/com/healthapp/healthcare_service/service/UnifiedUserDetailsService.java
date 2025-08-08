package com.healthapp.healthcare_service.service;

import com.healthapp.healthcare_service.entity.HealthcareProxy;
import com.healthapp.healthcare_service.entity.Staff;
import com.healthapp.healthcare_service.repository.HealthcareProxyRepository;
import com.healthapp.healthcare_service.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UnifiedUserDetailsService implements UserDetailsService {

    private final HealthcareProxyRepository healthcareProxyRepository;
    private final StaffRepository staffRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // First try to find a staff member
        Staff staff = staffRepository.findByEmail(username)
                .orElse(null);

        if (staff != null) {
            return staff;
        }

        // If not found as staff, try as healthcareProxy
        HealthcareProxy healthcareProxy = healthcareProxyRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        return healthcareProxy;
    }
}
