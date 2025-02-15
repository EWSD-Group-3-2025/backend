package org.teamSmurfs.backend.api.allocation.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BulkCreateAllocationRequest {

    @NotEmpty(message = "Allocations list cannot be empty")
    @Valid
    private List<CreateAllocationRequest> allocations;
}
