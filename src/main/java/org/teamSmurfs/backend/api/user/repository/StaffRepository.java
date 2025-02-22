package org.teamSmurfs.backend.api.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.teamSmurfs.backend.api.user.model.Staff;
import org.teamSmurfs.backend.api.user.model.User;

import java.util.List;

public interface StaffRepository extends JpaRepository<Staff, Long> {
    boolean existsByUserId(Long id);

    List<Staff> findByUser(User user);
}
