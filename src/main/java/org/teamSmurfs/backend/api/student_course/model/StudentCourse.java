package org.teamSmurfs.backend.api.student_course.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class StudentCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Column(name = "is_active")
    private boolean isActive = true;
}
