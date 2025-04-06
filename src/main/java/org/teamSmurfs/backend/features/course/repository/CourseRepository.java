package org.teamSmurfs.backend.features.course.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.teamSmurfs.backend.features.course.model.Course;

public interface CourseRepository extends JpaRepository<Course,Long>{

	Optional<Course> findByName(String name);

    List<Course> findAllByOrderByCreatedAtDesc();

	boolean existsByName(String courseName);
}
