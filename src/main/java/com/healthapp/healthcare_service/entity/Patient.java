package com.healthapp.healthcare_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "patient")
@Data
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String phone;
}
