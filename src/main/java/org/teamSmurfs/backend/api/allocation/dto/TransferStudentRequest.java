package org.teamSmurfs.backend.api.allocation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TransferStudentRequest {

    @NotNull(message = "First tutor ID is required")
    private Long firstTutorId;

    @NotNull(message = "Second tutor ID is required")
    private Long secondTutorId;

    private List<Long> studentsFromFirstToSecond;
    private List<Long> studentsFromSecondToFirst;
}

