package org.teamSmurfs.backend.features.specialization.dto;

import lombok.Data;

@Data
public class UpdateSpecializationRequest {
    private String name;
    private Long staffId;
}
