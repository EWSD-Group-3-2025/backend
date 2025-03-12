package org.teamSmurfs.backend.api.allocation.service;

import org.teamSmurfs.backend.api.allocation.dto.AllocatedStudentResponse;
import org.teamSmurfs.backend.api.allocation.dto.CreateAllocationRequest;
import org.teamSmurfs.backend.api.allocation.dto.TransferStudentRequest;

import java.util.List;

public interface AllocationService {
    void allocate(final CreateAllocationRequest request);
    void deallocateAllStudents(final Long tutorId);

    void transferStudents(final TransferStudentRequest transferStudentRequest);

    List<AllocatedStudentResponse> retrieveStudentsByTutorId(final Long userId);
}
