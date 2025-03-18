package org.teamSmurfs.backend.dashboard.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.teamSmurfs.backend.api.allocation.model.Allocation;
import org.teamSmurfs.backend.api.allocation.repository.AllocationRepository;
import org.teamSmurfs.backend.api.chat.model.ChatMessage;
import org.teamSmurfs.backend.api.chat.repository.ChatMessageRepository;
import org.teamSmurfs.backend.api.course.model.Course;
import org.teamSmurfs.backend.api.course.repository.CourseRepository;
import org.teamSmurfs.backend.api.student_course.repository.StudentCourseRepository;
import org.teamSmurfs.backend.api.user.dto.StudentDashBoardDto;
import org.teamSmurfs.backend.api.user.dto.StudentDto;
import org.teamSmurfs.backend.api.user.dto.StudentMapper;
import org.teamSmurfs.backend.api.user.dto.TutorDto;
import org.teamSmurfs.backend.api.user.dto.TutorMapper;
import org.teamSmurfs.backend.api.user.model.Student;
import org.teamSmurfs.backend.api.user.model.Tutor;
import org.teamSmurfs.backend.api.user.model.User;
import org.teamSmurfs.backend.api.user.repository.StudentRepository;
import org.teamSmurfs.backend.api.user.repository.TutorRepository;
import org.teamSmurfs.backend.api.user.repository.UserRepository;
import org.teamSmurfs.backend.api.visit_log.model.VisitLog;
import org.teamSmurfs.backend.api.visit_log.repository.VisitLogRepository;
import org.teamSmurfs.backend.dashboard.dto.AdminDashboardDto;
import org.teamSmurfs.backend.dashboard.dto.TutorDashboardCount;
import org.teamSmurfs.backend.dashboard.service.DashboardService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final StudentCourseRepository studentCourseRepository;
    private final AllocationRepository allocationRepository;
    private final TutorRepository tutorRepository;
    private final StudentRepository studentRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final VisitLogRepository visitLogRepository;
    private final TutorMapper tutorMapper;
    private final StudentMapper studentMapper;

    @Override
    public AdminDashboardDto getAdminDashboardData() {
        try {
            long totalUsers = userRepository.count();
            long assignedStudents = allocationRepository.countAssignedStudents();
            long activeTutors = tutorRepository.countActiveTutors();
            long totalMessages = chatMessageRepository.count();           
            long thisMothIncreaseCnt = userRepository.thisMothIncreaseCnt();
            
            log.debug("Fetched Admin Dashboard Data: totalUsers={}, assignedStudents={}, activeTutors={}, totalMessages={} , thisMonthIncreaseCount={}",
                    totalUsers, assignedStudents, activeTutors, totalMessages, thisMothIncreaseCnt);

            return new AdminDashboardDto(totalUsers, assignedStudents, activeTutors, totalMessages , thisMothIncreaseCnt);
        } catch (Exception e) {
            log.error("Error fetching Admin Dashboard data", e);
            return new AdminDashboardDto(0, 0, 0, 0, 0);  // Returning default values to prevent failure
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

        // Getting the tutor object from the allocation
        Tutor tutor = allocationOpt.get().getTutor();

        // Build and return the TutorDto using Builder
        return TutorDto.builder()
                .tutorId(tutor.getId())  // Tutor ID
                .id(tutor.getUser().getId())  // User ID associated with the tutor
                .name(tutor.getUser().getName())  // Tutor's name
                .email(tutor.getUser().getEmail())  // Tutor's email
                .username(tutor.getUser().getUsername())  // Tutor's username
                .roleName(tutor.getUser().getRoles().stream().findFirst().map(role -> role.getName().name().replaceFirst("^ROLE_", "")).orElse("No Role"))  // Assuming the first role is the tutor's role
                .roleId(tutor.getUser().getRoles().stream().findFirst().map(role -> role.getId()).orElse(null))  // Role ID
                .specializationName(tutor.getSpecialization() != null ? tutor.getSpecialization().getName() : "No Specialization")  // Tutor's specialization
                .specializationId(tutor.getSpecialization() != null ? tutor.getSpecialization().getId() : null)  // Specialization ID
                .status(tutor.getUser().isStatus())  // Tutor's status (active or not)
                .createdAt(tutor.getUser().getCreatedAt())  // Created at date
                .gender(tutor.getUser().getGender())  // Gender of the tutor
                .build();
    }


    @Override
    public List<StudentDashBoardDto> getStudentsByTutorId(Long userId) {
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
                .map(allocation -> {
                    // Get the student from the allocation and build the StudentDto manually
                    Student student = allocation.getStudent();
                    User user = student.getUser();
                    
                    List<VisitLog> visitedLogs = visitLogRepository.findByUserId(user.getId());                  
                    boolean isInactive = user.updateInactiveStatus(visitedLogs);
                    
                 // Calculate inactive days dynamically
                    int inactiveDays = 0;
                    if (isInactive) {
                        VisitLog latestVisit = visitedLogs.stream()
                                .max(Comparator.comparing(VisitLog::getCreatedAt))
                                .orElse(null);
                        if (latestVisit != null) {
                            inactiveDays = (int) ChronoUnit.DAYS.between(latestVisit.getCreatedAt(), LocalDateTime.now());
                        }
                    }
                    return StudentDashBoardDto.builder()
                            .studentId(student.getId()) // Student's user ID
                            .id(student.getUser().getId()) // User ID
                            .name(user.getName()) // Student's name
                            .email(user.getEmail()) // Student's email
                            .username(user.getUsername()) // Student's username
                            .status(user.isStatus()) // Student's status
                            .createdAt(user.getCreatedAt()) // Student's createdAt
                            .gender(user.getGender()) // Student's gender
                            .roleName(user.getRoles().stream().findFirst().map(role -> role.getName().name().replaceFirst("^ROLE_", "")).orElse("No Role")) // Role name (first role found)
                            .roleId(user.getRoles().stream().findFirst().map(role -> role.getId()).orElse(null)) // Role ID (first role found)
                            .courseId(studentCourseRepository.findByStudentId(student.getId()).map(studentCourse -> studentCourse.getCourseId()).orElse(null)) // Course ID from StudentCourse
                            .courseName(studentCourseRepository.findByStudentId(student.getId()).map(studentCourse -> {
                                Course course = courseRepository.findById(studentCourse.getCourseId()).orElse(null);
                                return course != null ? course.getName() : "No Course";
                            }).orElse("No Course")) // Course name from StudentCourse
                            .allocateTutorId(allocation.getTutor().getUser().getId()) // Allocated tutor's user ID
                            .inactive(isInactive)
                            .inactiveDays(inactiveDays)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentDto> getUnassignedStudentsByTutorUserId() {
        // Fetch all students that are active
        List<Student> allStudents = studentRepository.findAll();  // Assuming 'status' is a field indicating if a student is active

        // Fetch all allocations and get the set of student IDs that are already assigned
        List<Allocation> allAllocations = allocationRepository.findAll();
        Set<Long> assignedStudentIds = allAllocations.stream()
                .map(allocation -> allocation.getStudent().getId())  // Get student IDs from allocations
                .collect(Collectors.toSet());

        // Filter out the students who are unassigned (not in any allocation)
        List<Student> unassignedStudents = allStudents.stream()
                .filter(student -> !assignedStudentIds.contains(student.getId()))  // Ensure the student is unassigned
                .filter(student -> student.getUser().isStatus()) // Ensure the student is active
                .collect(Collectors.toList());

        // Map unassigned students to StudentDto using builder pattern
        return unassignedStudents.stream()
                .map(student -> {
                    User user = student.getUser(); // Get the user associated with the student

                    return StudentDto.builder()
                            .studentId(student.getId()) // Student's user ID
                            .id(user.getId()) // User ID
                            .name(user.getName()) // Student's name
                            .email(user.getEmail()) // Student's email
                            .username(user.getUsername()) // Student's username
                            .status(user.isStatus()) // Student's status
                            .createdAt(user.getCreatedAt()) // Student's createdAt
                            .gender(user.getGender()) // Student's gender
                            .roleName(user.getRoles().stream().findFirst().map(role -> role.getName().name().replaceFirst("^ROLE_", "")).orElse("No Role")) // Role name (first role found)
                            .roleId(user.getRoles().stream().findFirst().map(role -> role.getId()).orElse(null)) // Role ID (first role found)
                            .courseId(studentCourseRepository.findByStudentId(student.getId()).map(studentCourse -> studentCourse.getCourseId()).orElse(null)) // Course ID from StudentCourse
                            .courseName(studentCourseRepository.findByStudentId(student.getId()).map(studentCourse -> {
                                Course course = courseRepository.findById(studentCourse.getCourseId()).orElse(null);
                                return course != null ? course.getName() : "No Course";
                            }).orElse("No Course")) // Course name from StudentCourse
                            .allocateTutorId(null) // No tutor allocated, so set to null
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public TutorDashboardCount retrieveDashboardCountByTutorUserId(final Long userId) {
        return null;
    }

}
