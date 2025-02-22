package org.teamSmurfs.backend.api.allocation.service;

import org.teamSmurfs.backend.api.allocation.dto.AllocationDto;
import org.teamSmurfs.backend.api.allocation.dto.BulkCreateAllocationRequest;
import org.teamSmurfs.backend.api.allocation.dto.CreateAllocationRequest;

import java.util.List;

public interface AllocationService {
    AllocationDto allocate(final CreateAllocationRequest request);
    AllocationDto reallocate(final CreateAllocationRequest request);
    List<AllocationDto> bulkAllocate(final BulkCreateAllocationRequest request);
}
