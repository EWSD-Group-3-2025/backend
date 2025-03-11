package org.teamSmurfs.backend.api.allocation.service;

import org.teamSmurfs.backend.api.allocation.dto.CreateAllocationRequest;
import org.teamSmurfs.backend.api.allocation.dto.TransferStudentRequest;

public interface AllocationService {
    void allocate(final CreateAllocationRequest request);
    void deallocateAllStudents(final Long tutorId);

    void transferStudents(final TransferStudentRequest transferStudentRequest);
}
