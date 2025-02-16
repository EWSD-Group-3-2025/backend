package org.teamSmurfs.backend.api.allocation.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.teamSmurfs.backend.api.allocation.dto.AllocationDto;
import org.teamSmurfs.backend.api.allocation.dto.BulkCreateAllocationRequest;
import org.teamSmurfs.backend.api.allocation.dto.CreateAllocationRequest;
import org.teamSmurfs.backend.api.allocation.model.Allocation;
import org.teamSmurfs.backend.api.allocation.repository.AllocationRepository;
import org.teamSmurfs.backend.api.allocation.service.AllocationService;
import org.teamSmurfs.backend.api.user.model.Student;
import org.teamSmurfs.backend.api.user.model.Tutor;
import org.teamSmurfs.backend.api.user.repository.StudentRepository;
import org.teamSmurfs.backend.api.user.repository.TutorRepository;
import org.teamSmurfs.backend.config.utils.DtoUtil;
import org.teamSmurfs.backend.config.utils.EntityUtil;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AllocationServiceImpl implements AllocationService {

    private final AllocationRepository allocationRepository;
    private final StudentRepository studentRepository;
    private final TutorRepository tutorRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public AllocationDto allocate(CreateAllocationRequest request) {
        log.info("Allocating student {} to tutor {}", request.getStudentId(), request.getTutorId());
        return convertToDto(createAllocation(request));
    }

    @Override
    @Transactional
    public AllocationDto reallocate(CreateAllocationRequest request) {
        log.info("Reallocating student {} to tutor {}", request.getStudentId(), request.getTutorId());
        return allocate(request);
    }

    @Override
    @Transactional
    public List<AllocationDto> bulkAllocate(BulkCreateAllocationRequest request) {
        log.info("Processing bulk allocation for {} requests", request.getAllocations().size());

        List<Allocation> allocations = request.getAllocations().stream()
                .map(this::createAllocation)
                .collect(Collectors.toList());

        List<Allocation> savedAllocations = allocationRepository.saveAll(allocations);

        return DtoUtil.mapList(savedAllocations, AllocationDto.class, modelMapper);
    }

    /**
     * Extracted method to create an allocation entity.
     */
    private Allocation createAllocation(CreateAllocationRequest request) {
        Student student = EntityUtil.getEntityById(studentRepository, request.getStudentId());
        Tutor tutor = EntityUtil.getEntityById(tutorRepository, request.getTutorId());

        Allocation allocation = new Allocation();
        allocation.setStudent(student);
        allocation.setTutor(tutor);
        allocation.setActive(true);

        return allocation;
    }

    private AllocationDto convertToDto(Allocation allocation) {
        return AllocationDto.builder()
                .id(allocation.getId())
                .studentName(allocation.getStudent().getUser().getName())
                .tutorName(allocation.getTutor().getUser().getName())
                .active(allocation.isActive())
                .build();
    }
}
