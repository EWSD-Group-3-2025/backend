package org.teamSmurfs.backend.api.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.teamSmurfs.backend.api.user.model.Student;
import org.teamSmurfs.backend.api.user.model.User;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByUser(User user);

    Optional<Student> findById(Long studentId);

    Optional<Student> findByUserId(Long userId);

    @Query("SELECT s FROM Student s WHERE s.id NOT IN (SELECT a.student.id FROM Allocation a)")
    List<Student> findStudentsNotInAnyAllocation();

    List<Student> findAllByUserIdIn(List<Long> studentIds);
}
