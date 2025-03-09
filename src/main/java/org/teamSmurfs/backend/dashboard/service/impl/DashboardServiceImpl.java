package org.teamSmurfs.backend.dashboard.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.teamSmurfs.backend.api.allocation.model.Allocation;
import org.teamSmurfs.backend.api.allocation.repository.AllocationRepository;
import org.teamSmurfs.backend.api.chat.model.ChatMessage;
import org.teamSmurfs.backend.api.chat.repository.ChatMessageRepository;
import org.teamSmurfs.backend.api.user.dto.StudentDto;
import org.teamSmurfs.backend.api.user.dto.StudentMapper;
import org.teamSmurfs.backend.api.user.dto.TutorDto;
import org.teamSmurfs.backend.api.user.dto.TutorMapper;
import org.teamSmurfs.backend.api.user.model.Student;
import org.teamSmurfs.backend.api.user.model.Tutor;
import org.teamSmurfs.backend.api.user.repository.StudentRepository;
import org.teamSmurfs.backend.api.user.repository.TutorRepository;
import org.teamSmurfs.backend.api.user.repository.UserRepository;
import org.teamSmurfs.backend.dashboard.dto.AdminDashboardDto;
import org.teamSmurfs.backend.dashboard.service.DashboardService;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;
    private final AllocationRepository allocationRepository;
    private final TutorRepository tutorRepository;
    private final StudentRepository studentRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final TutorMapper tutorMapper;
    private final StudentMapper studentMapper;

    @Override
    public AdminDashboardDto getAdminDashboardData() {
        try {
            long totalUsers = userRepository.count();
            long assignedStudents = allocationRepository.countAssignedStudents();
            long activeTutors = tutorRepository.countActiveTutors();
            long totalMessages = chatMessageRepository.count();

            log.debug("Fetched Admin Dashboard Data: totalUsers={}, assignedStudents={}, activeTutors={}, totalMessages={}",
                    totalUsers, assignedStudents, activeTutors, totalMessages);

            return new AdminDashboardDto(totalUsers, assignedStudents, activeTutors, totalMessages);
        } catch (Exception e) {
            log.error("Error fetching Admin Dashboard data", e);
            return new AdminDashboardDto(0, 0, 0, 0);  // Returning default values to prevent failure
        }
    }

    @Override
    public TutorDto getTutorByStudentId(Long userId) {
        Optional<Student> studentOpt = studentRepository.findByUserId(userId);

        if (studentOpt.isEmpty()) {
            log.warn("No student found for user ID: {}", userId);
            return null;
        }

        Long studentId = studentOpt.get().getId();
        Optional<Allocation> allocationOpt = allocationRepository.findByStudentId(studentId);

        if (allocationOpt.isEmpty()) {
            log.warn("No active tutor found for student ID: {}", studentId);
            return null;
        }

        return tutorMapper.mapToDto(allocationOpt.get().getTutor().getUser());
    }


    @Override
    public List<StudentDto> getStudentsByTutorId(Long userId) {
        Optional<Tutor> tutorOpt = tutorRepository.findByUserId(userId);

        if (tutorOpt.isEmpty()) {
            log.warn("No tutor found for user ID: {}", userId);
            return List.of();
        }

        Long tutorId = tutorOpt.get().getId();
        List<Allocation> allocations = allocationRepository.findByTutorId(tutorId);

        if (allocations.isEmpty()) {
            log.warn("No students assigned to tutor with user ID: {}", tutorId);
            return List.of();
        }

        return allocations.stream()
                .filter(allocation -> allocation.getStudent().getUser().isStatus()) // Ensure the student is active
                .map(allocation -> studentMapper.mapToDto(allocation.getStudent().getUser())) // Map to StudentDto
                .collect(Collectors.toList());
    }


    @Override
    public List<StudentDto> getUnassignedStudentsByTutorUserId() {
        // Fetch all students
        List<Student> allStudents = studentRepository.findAll();

        // Fetch all allocations and get the set of student IDs that are already assigned
        List<Allocation> allAllocations = allocationRepository.findAll();
        Set<Long> assignedStudentIds = allAllocations.stream()
                .map(allocation -> allocation.getStudent().getId())  // Get student IDs from allocations
                .collect(Collectors.toSet());

        // Filter out the students who are assigned to any tutor
        List<Student> unassignedStudents = allStudents.stream()
                .filter(student -> !assignedStudentIds.contains(student.getId()))  // Ensure the student is unassigned
                .collect(Collectors.toList());

        // Map unassigned students to StudentDto
        return unassignedStudents.stream()
                .map(student -> studentMapper.mapToDto(student.getUser())) // Map User to StudentDto
                .collect(Collectors.toList());
    }



}
