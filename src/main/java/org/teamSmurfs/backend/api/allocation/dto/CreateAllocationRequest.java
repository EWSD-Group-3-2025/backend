package org.teamSmurfs.backend.api.allocation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateAllocationRequest {

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotNull(message = "Tutor ID is required")
    private Long tutorId;

    @NotNull(message = "Subject is required")
    private String subject;
}
