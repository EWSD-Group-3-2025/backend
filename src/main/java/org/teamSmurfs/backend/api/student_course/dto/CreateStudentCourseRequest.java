package org.teamSmurfs.backend.api.student_course.dto;

import lombok.Data;

@Data
public class CreateStudentCourseRequest {
    private Long studentId;
    private Long courseId;
}
