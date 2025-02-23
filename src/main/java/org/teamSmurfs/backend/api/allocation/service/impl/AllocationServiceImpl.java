package org.teamSmurfs.backend.api.allocation.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.teamSmurfs.backend.api.allocation.dto.CreateAllocationRequest;
import org.teamSmurfs.backend.api.allocation.model.Allocation;
import org.teamSmurfs.backend.api.allocation.repository.AllocationRepository;
import org.teamSmurfs.backend.api.allocation.service.AllocationService;
import org.teamSmurfs.backend.api.user.model.Student;
import org.teamSmurfs.backend.api.user.model.Tutor;
import org.teamSmurfs.backend.api.user.model.User;
import org.teamSmurfs.backend.api.user.repository.StudentRepository;
import org.teamSmurfs.backend.api.user.repository.TutorRepository;
import org.teamSmurfs.backend.api.user.repository.UserRepository;
import org.teamSmurfs.backend.config.exception.EntityNotFoundException;
import org.teamSmurfs.backend.config.utils.EntityUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AllocationServiceImpl implements AllocationService {

    private final AllocationRepository allocationRepository;
    private final StudentRepository studentRepository;
    private final TutorRepository tutorRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void allocate(final CreateAllocationRequest request) {
        log.info("Processing allocation for {} students with tutor {}",
                request.getStudentIds().size(), request.getTutorId());

        User user = EntityUtil.getEntityById(this.userRepository, request.getTutorId());

        Tutor tutor = tutorRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Tutor not found for user ID: " + user.getId()));

        List<Allocation> allocations = request.getStudentIds().stream()
                .map(studentId -> prepareAllocation(studentId, tutor))
                .collect(Collectors.toList());

        List<Allocation> savedAllocations = allocationRepository.saveAll(allocations);
        log.info("Successfully allocated {} students to tutor {}", savedAllocations.size(), tutor.getId());
    }

    /**
     * Prepares an allocation entity from a CreateAllocationRequest.
     */
    private Allocation prepareAllocation(Long studentId, Tutor tutor) {
        User user = EntityUtil.getEntityById(this.userRepository, studentId);
        Student student = studentRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Student not found for user ID: " + user.getId()));

        Optional<Allocation> existingAllocationOpt = allocationRepository.findByStudentAndActiveTrue(student);
        if (existingAllocationOpt.isPresent()) {
            Allocation existingAllocation = existingAllocationOpt.get();
            existingAllocation.setTutor(tutor);
            existingAllocation.setUpdatedAt(LocalDateTime.now());
            return existingAllocation;
        }

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
}
