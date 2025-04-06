package org.teamSmurfs.backend.features.department.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.teamSmurfs.backend.features.department.model.Department;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findByName(String departmentName);
    List<Department> findAllByOrderByCreatedAtDesc();
	boolean existsByName(String departmentName);
}
