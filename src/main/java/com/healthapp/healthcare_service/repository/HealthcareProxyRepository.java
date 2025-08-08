package com.healthapp.healthcare_service.repository;

import com.healthapp.healthcare_service.entity.HealthcareProxy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HealthcareProxyRepository extends JpaRepository<HealthcareProxy, Long> {
    Optional<HealthcareProxy> findByEmail(String username);
}
