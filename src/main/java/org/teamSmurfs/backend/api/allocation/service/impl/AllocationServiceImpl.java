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

    @Override
    @Transactional
    public AllocationDto allocate(final CreateAllocationRequest request) {
        log.info("Allocating student {} to tutor {}", request.getStudentId(), request.getTutorId());
        Allocation allocation = prepareAllocation(request);
        return saveAndConvertToDto(allocation);
    }

    @Override
    @Transactional
    public AllocationDto reallocate(final CreateAllocationRequest request) {
        log.info("Reallocating student {} to tutor {}", request.getStudentId(), request.getTutorId());
        return allocate(request);
    }

    @Override
    @Transactional
    public List<AllocationDto> bulkAllocate(final BulkCreateAllocationRequest request) {
        log.info("Processing bulk allocation for {} students with tutor {}",
                request.getStudentIds().size(), request.getTutorId());

        Tutor tutor = EntityUtil.getEntityById(tutorRepository, request.getTutorId());

        List<Allocation> allocations = request.getStudentIds().stream()
                .map(studentId -> prepareAllocation(studentId, tutor))
                .collect(Collectors.toList());

        List<Allocation> savedAllocations = allocationRepository.saveAll(allocations);
        log.info("Successfully allocated {} students to tutor {}", savedAllocations.size(), tutor.getId());

        return savedAllocations.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Prepares an allocation entity from a CreateAllocationRequest.
     */
    private Allocation prepareAllocation(final CreateAllocationRequest request) {
        Student student = EntityUtil.getEntityById(studentRepository, request.getStudentId());
        Tutor tutor = EntityUtil.getEntityById(tutorRepository, request.getTutorId());
        return buildAllocation(student, tutor);
    }

    /**
     * Overloaded method for bulk allocation to avoid repetitive fetching of Tutor entity.
     */
    private Allocation prepareAllocation(Long studentId, Tutor tutor) {
        Student student = EntityUtil.getEntityById(studentRepository, studentId);
        return buildAllocation(student, tutor);
    }

    /**
     * Constructs an Allocation entity with all required parameters.
     */
    private Allocation buildAllocation(Student student, Tutor tutor) {
        Allocation allocation = new Allocation();
        allocation.setStudent(student);
        allocation.setTutor(tutor);
        allocation.setActive(true);
        return allocation;
    }

    /**
     * Persists the allocation and returns its DTO representation.
     */
    private AllocationDto saveAndConvertToDto(Allocation allocation) {
        Allocation savedAllocation = allocationRepository.save(allocation);
        return convertToDto(savedAllocation);
    }

    /**
     * Converts an Allocation entity into its DTO representation.
     */
    private AllocationDto convertToDto(Allocation allocation) {
        return AllocationDto.builder()
                .id(allocation.getId())
                .studentName(allocation.getStudent().getUser().getName())
                .tutorName(allocation.getTutor().getUser().getName())
                .active(allocation.isActive())
                .build();
    }
}
