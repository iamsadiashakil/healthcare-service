package com.healthapp.healthcare_service.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class VitalsSummaryDto {
    private Map<String, VitalDto> latestReadings;

    private String lastUpdated;
}