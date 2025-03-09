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
import org.teamSmurfs.backend.api.user.repository.TutorRepository;
import org.teamSmurfs.backend.api.user.repository.UserRepository;
import org.teamSmurfs.backend.dashboard.dto.AdminDashboardDto;
import org.teamSmurfs.backend.dashboard.service.DashboardService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;
    private final AllocationRepository allocationRepository;
    private final TutorRepository tutorRepository;
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
    public TutorDto getTutorByStudentId(Long studentId) {
        Optional<Allocation> allocationOpt = allocationRepository.findByStudentId(studentId);

        if (allocationOpt.isEmpty()) {
            log.warn("No active tutor found for student ID: {}", studentId);
            return null;
        }

        return tutorMapper.mapToDto(allocationOpt.get().getTutor().getUser());
    }

    @Override
    public List<StudentDto> getStudentsByTutorId(Long tutorId) {
        List<Allocation> allocations = allocationRepository.findByTutorId(tutorId);

        if (allocations.isEmpty()) {
            log.warn("No students assigned to tutor ID: {}", tutorId);
            return List.of();
        }

        return allocations.stream()
                .filter(allocation -> allocation.getStudent().getUser().isStatus())
                .map(allocation -> studentMapper.mapToDto(allocation.getStudent().getUser())) // Use StudentMapper
                .collect(Collectors.toList());
    }
}
