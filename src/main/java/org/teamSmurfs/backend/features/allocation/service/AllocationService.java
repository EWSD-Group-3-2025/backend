package org.teamSmurfs.backend.features.allocation.service;

import org.teamSmurfs.backend.features.allocation.dto.AllocatedStudentResponse;
import org.teamSmurfs.backend.features.allocation.dto.CreateAllocationRequest;
import org.teamSmurfs.backend.features.allocation.dto.TransferStudentRequest;

import java.util.List;

public interface AllocationService {
    void allocate(final CreateAllocationRequest request);
    void deallocateAllStudents(final Long tutorId);
    void deallocateStudent(final Long studentId);

    void transferStudents(final TransferStudentRequest transferStudentRequest);

    List<AllocatedStudentResponse> retrieveStudentsByTutorId(final Long userId);
}
