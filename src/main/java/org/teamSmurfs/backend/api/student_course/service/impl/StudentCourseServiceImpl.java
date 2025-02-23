package org.teamSmurfs.backend.api.student_course.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.teamSmurfs.backend.api.student_course.dto.CreateStudentCourseRequest;
import org.teamSmurfs.backend.api.student_course.model.StudentCourse;
import org.teamSmurfs.backend.api.student_course.repository.StudentCourseRepository;
import org.teamSmurfs.backend.api.student_course.service.StudentCourseService;

@AllArgsConstructor
@Service
public class StudentCourseServiceImpl {
    private final StudentCourseRepository repository;


}
