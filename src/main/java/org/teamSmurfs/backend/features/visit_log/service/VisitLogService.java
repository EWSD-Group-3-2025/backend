package org.teamSmurfs.backend.features.visit_log.service;

import org.teamSmurfs.backend.features.visit_log.dto.VisitLogDto;
import org.teamSmurfs.backend.features.visit_log.model.VisitLog;

import java.util.List;

public interface VisitLogService {
    void save(VisitLog visitLog);

    List<VisitLogDto> retrieveVisitLogs();
}
