package org.teamSmurfs.backend.api.department.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.teamSmurfs.backend.api.department.model.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
