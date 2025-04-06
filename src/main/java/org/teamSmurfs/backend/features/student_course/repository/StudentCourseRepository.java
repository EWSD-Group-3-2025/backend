package org.teamSmurfs.backend.features.student_course.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.teamSmurfs.backend.features.student_course.model.StudentCourse;

@Repository
public interface StudentCourseRepository extends JpaRepository<StudentCourse, Long> {
	
	Optional<StudentCourse> findByStudentId(Long id);

    List<StudentCourse> findByCourseId(Long courseId);
}
