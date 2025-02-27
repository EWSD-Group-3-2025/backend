package org.teamSmurfs.backend.api.visit_log.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.teamSmurfs.backend.api.user.model.User;
import org.teamSmurfs.backend.api.visit_log.model.VisitLog;

import java.time.LocalDateTime;
import java.util.List;

public interface VisitLogRepository extends JpaRepository<VisitLog, Long> {

    @Query("""
    SELECT v.user FROM VisitLog v
    WHERE v.createdAt = (SELECT MAX(v2.createdAt) FROM VisitLog v2 WHERE v2.user = v.user)
    AND v.createdAt < :cutoffDate
    AND v.user.inactive = false
    AND v.user.status = true
""")
    List<User> findInactiveUsers(LocalDateTime cutoffDate);
}
