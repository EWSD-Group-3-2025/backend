package org.teamSmurfs.backend.api.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.teamSmurfs.backend.api.user.model.Student;
import org.teamSmurfs.backend.api.user.model.User;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByUser(User user);

    Optional<Student> findById(Long studentId);

    Optional<Student> findByUserId(Long userId);
}
