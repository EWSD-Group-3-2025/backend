package org.teamSmurfs.backend.api.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.teamSmurfs.backend.api.user.model.Staff;

public interface StaffRepository extends JpaRepository<Staff, Long> {
    boolean existsByUserId(Long id);
}
