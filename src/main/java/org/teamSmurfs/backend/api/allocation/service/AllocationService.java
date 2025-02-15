package org.teamSmurfs.backend.api.allocation.service;

import org.teamSmurfs.backend.api.allocation.dto.AllocationDto;

public interface AllocationService {
    AllocationDto allocate();
    AllocationDto reallocate();
}
