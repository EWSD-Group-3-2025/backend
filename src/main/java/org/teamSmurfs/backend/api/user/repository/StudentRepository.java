package org.teamSmurfs.backend.api.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.teamSmurfs.backend.api.user.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
