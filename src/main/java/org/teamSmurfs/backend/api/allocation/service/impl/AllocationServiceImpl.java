package org.teamSmurfs.backend.api.allocation.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.teamSmurfs.backend.api.allocation.dto.AllocatedStudentResponse;
import org.teamSmurfs.backend.api.allocation.dto.CreateAllocationRequest;
import org.teamSmurfs.backend.api.allocation.dto.EmailRequest;
import org.teamSmurfs.backend.api.allocation.dto.TransferStudentRequest;
import org.teamSmurfs.backend.api.allocation.model.Allocation;
import org.teamSmurfs.backend.api.allocation.repository.AllocationRepository;
import org.teamSmurfs.backend.api.allocation.service.AllocationService;
import org.teamSmurfs.backend.api.chat.service.ChatService;
import org.teamSmurfs.backend.api.user.dto.StudentDto;
import org.teamSmurfs.backend.api.user.model.Student;
import org.teamSmurfs.backend.api.user.model.Tutor;
import org.teamSmurfs.backend.api.user.model.User;
import org.teamSmurfs.backend.api.user.repository.StudentRepository;
import org.teamSmurfs.backend.api.user.repository.TutorRepository;
import org.teamSmurfs.backend.api.user.repository.UserRepository;
import org.teamSmurfs.backend.config.exception.EntityNotFoundException;
import org.teamSmurfs.backend.config.service.MailService;
import org.teamSmurfs.backend.config.utils.EntityUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class cAllocationServiceImpl implements AllocationService {

    private final AllocationRepository allocationRepository;
    private final StudentRepository studentRepository;
    private final TutorRepository tutorRepository;
    private final UserRepository userRepository;
    private final MailService mailService;
    private final ChatService chatService;
    private final RabbitTemplate rabbitTemplate;

    @Override
    @Transactional
    public void allocate(final CreateAllocationRequest request) {
        if (request.getStudentIds().size() > 10) {
            throw new IllegalArgumentException("Cannot allocate more than 10 students at once.");
        }

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

        savedAllocations.forEach(allocation -> {
            Long tutorId = allocation.getTutor().getUser().getId();
            Long studentId = allocation.getStudent().getUser().getId();
            chatService.createOrGetChatRoom(tutorId, studentId);
        });

        sendAllocationEmailsAsync(savedAllocations, tutor);
    }

    @Override
    public void deallocateAllStudents(final Long tutorId) {
        User user = EntityUtil.getEntityById(this.userRepository, tutorId);
        Tutor tutor = this.tutorRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Tutor not found for user ID: " + user.getId()));

        List<Allocation> allocationList = this.allocationRepository.findByTutorId(tutor.getId());
        if (allocationList == null || allocationList.isEmpty()) {
            log.info("There is no students allocated with tutor: {}", user.getName());
            throw new EntityNotFoundException("There is no students allocated with tutor " + user.getName());
        }

        this.allocationRepository.deleteAll(allocationList);
    }

    @Override
    public void deallocateStudent(final Long studentId) {
        final User user = EntityUtil.getEntityById(this.userRepository, studentId);
        final Student student = this.studentRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Student not found for user ID: " + user.getId()));

        final Allocation allocation = this.allocationRepository.findByStudentId(student.getId())
                .orElseThrow(() -> new EntityNotFoundException("Allocation not found for student ID: " + studentId));

        this.allocationRepository.delete(allocation);
    }

    @Override
    @Transactional
    public void transferStudents(final TransferStudentRequest transferRequest) {
        log.info("Initiating student transfer process...");

        final Tutor firstTutor = this.getTutorById(transferRequest.getFirstTutorId());
        final Tutor secondTutor = this.getTutorById(transferRequest.getSecondTutorId());

        final List<Long> studentsToMoveToSecond = transferRequest.getStudentsFromFirstToSecond();
        final List<Long> studentsToMoveToFirst = transferRequest.getStudentsFromSecondToFirst();

        if (this.isTransferListEmpty(studentsToMoveToFirst) && this.isTransferListEmpty(studentsToMoveToSecond)) {
            log.warn("No students specified for transfer.");
            throw new IllegalArgumentException("No students provided for transfer.");
        }

        this.transferStudentsBetweenTutors(studentsToMoveToSecond, firstTutor, secondTutor);
        this.transferStudentsBetweenTutors(studentsToMoveToFirst, secondTutor, firstTutor);

        log.info("Student transfer process completed successfully.");
    }

    @Override
    public List<AllocatedStudentResponse> retrieveStudentsByTutorId(final Long userId) {
        Optional<Tutor> tutorOpt = tutorRepository.findByUserId(userId);

        if (tutorOpt.isEmpty()) {
            log.warn("No tutor found for user ID: {}", userId);
            return List.of();
        }

        Long tutorId = tutorOpt.get().getId();
        List<Allocation> allocations = this.allocationRepository.findByTutorId(tutorId);

        if (allocations.isEmpty()) {
            log.warn("No students assigned to tutor with user ID: {}", tutorId);
            return List.of();
        }
        return allocations.stream()
                .filter(allocation -> allocation.getStudent().getUser().isStatus()) // Ensure the student is active
                .map(allocation -> {
                    Student student = allocation.getStudent();
                    User user = student.getUser();

                    return AllocatedStudentResponse.builder()
                            .id(student.getUser().getId())
                            .name(user.getName())
                            .build();
                })
                .collect(Collectors.toList());
    }

    private Tutor getTutorById(final Long tutorId) {
        return this.tutorRepository.findByUser(
                        EntityUtil.getEntityById(this.userRepository, tutorId))
                .orElseThrow(() -> new EntityNotFoundException("Tutor not found for user ID: " + tutorId));
    }

    private boolean isTransferListEmpty(final List<Long> studentIds) {
        return studentIds == null || studentIds.isEmpty();
    }

    private void transferStudentsBetweenTutors(final List<Long> studentIds, final Tutor fromTutor, final Tutor toTutor) {
        if (this.isTransferListEmpty(studentIds)) return;

        List<Student> students = this.studentRepository.findAllByUserIdIn(studentIds);
        if (students.size() != studentIds.size()) {
            throw new EntityNotFoundException("Some students were not found.");
        }

        final List<Allocation> updatedAllocations = students.stream()
                .map(student -> this.allocationRepository.findByStudentAndActiveTrue(student)
                        .map(allocation -> this.updateAllocation(allocation, toTutor))
                        .orElseGet(() -> this.logNoActiveAllocation(student)))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (!updatedAllocations.isEmpty()) {
            this.allocationRepository.saveAll(updatedAllocations);
            log.info("{} students successfully transferred to Tutor {}", updatedAllocations.size(), toTutor.getId());
        }
    }

    private Allocation updateAllocation(final Allocation allocation, final Tutor toTutor) {
        allocation.setTutor(toTutor);
        allocation.setUpdatedAt(LocalDateTime.now());
        log.info("Student {} moved to Tutor {}", allocation.getStudent().getId(), toTutor.getId());
        return allocation;
    }

    private Allocation logNoActiveAllocation(Student student) {
        log.warn("Student {} has no active allocation and cannot be transferred", student.getId());
        return null;
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

    private void sendAllocationEmailsAsync(List<Allocation> allocations, Tutor tutor) {
        String tutorName = tutor.getUser().getName();

        EmailRequest tutorEmailRequest = new EmailRequest(
                tutor.getUser().getEmail(),
                "TUTOR",
                tutorName,
                allocations.stream()
                        .map(a -> a.getStudent().getUser().getName())
                        .collect(Collectors.joining(", "))
        );
        rabbitTemplate.convertAndSend("allocationEmailQueue", tutorEmailRequest);

        for (Allocation allocation : allocations) {
            Student student = allocation.getStudent();
            EmailRequest studentEmailRequest = new EmailRequest(
                    student.getUser().getEmail(),
                    "STUDENT",
                    tutorName,
                    student.getUser().getName()
            );
            rabbitTemplate.convertAndSend("allocationEmailQueue", studentEmailRequest);
        }
    }
}
