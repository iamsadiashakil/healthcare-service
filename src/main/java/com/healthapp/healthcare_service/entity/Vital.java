package com.healthapp.healthcare_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "vitals")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vital {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reading;
    private String type;

    @Column(name = "measured_at")
    private LocalDateTime measuredAt;

    private String status;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;
}