package org.teamSmurfs.backend.api.allocation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.teamSmurfs.backend.api.allocation.model.Allocation;
import org.teamSmurfs.backend.api.user.model.Student;

import java.util.Optional;

public interface AllocationRepository extends JpaRepository<Allocation, Long> {
    Optional<Allocation> findByStudentAndActiveTrue(Student student);
}
