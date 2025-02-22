package org.teamSmurfs.backend.api.course.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.teamSmurfs.backend.api.course.model.Course;

public interface CourseRepository extends JpaRepository<Course,Long>{

	Optional<Course> findByName(String name);

}
