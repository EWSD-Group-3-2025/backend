package org.teamSmurfs.backend.features.specialization.dto;

import lombok.Data;

@Data
public class CreateSpecializationRequest {
    private String[] names;
    private Long staffId;
}
