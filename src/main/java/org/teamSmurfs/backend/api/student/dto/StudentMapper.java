package org.teamSmurfs.backend.api.student.dto;

import java.util.Optional;

import org.springframework.stereotype.Component;
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

    public StudentMapper(StudentCourseRepository studentCourseRepository, CourseRepository courseRepository) {
        this.studentCourseRepository = studentCourseRepository;
        this.courseRepository = courseRepository;
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

        return studentDto;
    }
}
