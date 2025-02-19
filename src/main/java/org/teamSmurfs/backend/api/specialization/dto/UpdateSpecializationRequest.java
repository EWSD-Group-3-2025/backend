package org.teamSmurfs.backend.api.specialization.dto;

import lombok.Data;

@Data
public class UpdateSpecializationRequest {
    private String name;
    private Long staffId;
}
