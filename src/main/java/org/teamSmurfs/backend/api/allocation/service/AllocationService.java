package org.teamSmurfs.backend.api.allocation.service;

import org.teamSmurfs.backend.api.allocation.dto.CreateAllocationRequest;

public interface AllocationService {
    void allocate(final CreateAllocationRequest request);
}
