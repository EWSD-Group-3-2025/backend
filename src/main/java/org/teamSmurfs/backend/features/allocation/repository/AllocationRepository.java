package org.teamSmurfs.backend.features.allocation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.teamSmurfs.backend.features.allocation.model.Allocation;
import org.teamSmurfs.backend.features.user.model.Student;

import java.util.List;
import java.util.Optional;

public interface AllocationRepository extends JpaRepository<Allocation, Long> {
    Optional<Allocation> findByStudentAndActiveTrue(Student student);

    Optional<Allocation> findByStudentId(Long studentId);

	List<Allocation> findByTutorId(Long tutorId);

    @Query("SELECT COUNT(DISTINCT a.student.user.id) FROM Allocation a WHERE a.student.user.status = true")
    long countAssignedStudents();


}
