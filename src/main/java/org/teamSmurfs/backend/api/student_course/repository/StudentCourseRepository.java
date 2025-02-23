package org.teamSmurfs.backend.api.student_course.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.teamSmurfs.backend.api.student_course.model.StudentCourse;
import org.teamSmurfs.backend.api.user.model.Student;
import org.teamSmurfs.backend.api.user.model.User;

@Repository
public interface StudentCourseRepository extends JpaRepository<StudentCourse, Long> {
	
	Optional<StudentCourse> findByStudentId(Long id);
}
