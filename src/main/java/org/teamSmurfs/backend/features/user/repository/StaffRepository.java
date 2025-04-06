package org.teamSmurfs.backend.features.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.teamSmurfs.backend.features.user.model.Staff;
import org.teamSmurfs.backend.features.user.model.User;

import java.util.List;

public interface StaffRepository extends JpaRepository<Staff, Long> {
    boolean existsByUserId(Long id);

    List<Staff> findByUser(User user);

    @Query("SELECT s FROM Staff s JOIN FETCH s.user WHERE s.department.id = :departmentId")
    List<Staff> findByDepartmentId(@Param("departmentId") Long departmentId);
}
