package org.teamSmurfs.backend.api.visit_log.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.teamSmurfs.backend.api.visit_log.model.VisitLog;

public interface VisitLogRepository extends JpaRepository<VisitLog, Long> {
}
