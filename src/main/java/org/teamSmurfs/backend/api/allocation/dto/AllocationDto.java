package org.teamSmurfs.backend.api.allocation.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AllocationDto {
    private Long id;

    private String studentName;

    private String tutorName;

    private boolean active;
}
