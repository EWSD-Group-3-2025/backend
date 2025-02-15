package org.teamSmurfs.backend.api.allocation.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.teamSmurfs.backend.api.allocation.dto.AllocationDto;
import org.teamSmurfs.backend.api.allocation.repository.AllocationRepository;
import org.teamSmurfs.backend.api.allocation.service.AllocationService;

@Service
@RequiredArgsConstructor
@Slf4j
public class AllocationServiceImpl implements AllocationService {

    private final AllocationRepository allocationRepository;

    @Override
    public AllocationDto allocate() {
        return AllocationDto.builder().studentName("John Doe").tutorName("Dr. Smith").subject("Math").active(true).build();
    }

    @Override
    public AllocationDto reallocate() {
        return AllocationDto.builder().studentName("Jane Doe").tutorName("Dr. Adams").subject("Science").active(true).build();
    }
}
