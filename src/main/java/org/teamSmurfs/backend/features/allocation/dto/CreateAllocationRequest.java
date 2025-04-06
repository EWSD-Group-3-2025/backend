package org.teamSmurfs.backend.features.allocation.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CreateAllocationRequest {

    @NotEmpty(message = "Student IDs list cannot be empty")
    private List<Long> studentIds;

    @NotNull(message = "Tutor ID is required")
    private Long tutorId;
}
