package org.teamSmurfs.backend.api.user.dto;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.teamSmurfs.backend.api.allocation.model.Allocation;
import org.teamSmurfs.backend.api.allocation.repository.AllocationRepository;
import org.teamSmurfs.backend.api.course.model.Course;
import org.teamSmurfs.backend.api.course.repository.CourseRepository;
import org.teamSmurfs.backend.api.student_course.model.StudentCourse;
import org.teamSmurfs.backend.api.student_course.repository.StudentCourseRepository;
import org.teamSmurfs.backend.api.user.model.Student;
import org.teamSmurfs.backend.api.user.model.User;

@Component
public class StudentMapper {
	
	private final StudentCourseRepository studentCourseRepository;
    private final CourseRepository courseRepository;
    private final AllocationRepository allocationRepository;

    public StudentMapper(StudentCourseRepository studentCourseRepository, CourseRepository courseRepository, AllocationRepository allocationRepository) {
        this.studentCourseRepository = studentCourseRepository;
        this.courseRepository = courseRepository;
        this.allocationRepository = allocationRepository;
    }

    public StudentDto mapToDto(User user) {
        if (user == null || user.getStudent() == null) {
            return null;
        }

        Student student = user.getStudent();
        StudentDto studentDto = StudentDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .username(user.getUsername())
                .status(user.isStatus())
                .createdAt(user.getCreatedAt())
                .build();

        user.getRoles().stream().findFirst().ifPresent(roleEntity -> {
            studentDto.setRoleName(roleEntity.getName().name().replaceFirst("^ROLE_", ""));
            studentDto.setRoleId(roleEntity.getId());
        });

        Optional<StudentCourse> studentCourseOptional = studentCourseRepository.findByStudentId(student.getId());

        if (studentCourseOptional.isPresent()) {
            StudentCourse studentCourse = studentCourseOptional.get();
            studentDto.setCourseId(studentCourse.getCourseId());

            Course course = courseRepository.findById(studentCourse.getCourseId()).orElse(null);
            studentDto.setCourse(course != null ? course.getName() : "No Course");
        } else {
            studentDto.setCourseId(null);
            studentDto.setCourse(null);
        }

        Optional<Allocation> allocationOptional = allocationRepository.findByStudentId(student.getId());
        allocationOptional.ifPresent(allocation -> studentDto.setAllocateTutorId(allocation.getTutor().getUser().getId()));

        return studentDto;
    }
}
