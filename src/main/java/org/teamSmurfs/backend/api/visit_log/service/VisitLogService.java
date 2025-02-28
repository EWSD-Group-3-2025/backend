package org.teamSmurfs.backend.api.visit_log.service;

import org.teamSmurfs.backend.api.visit_log.dto.VisitLogDto;
import org.teamSmurfs.backend.api.visit_log.model.VisitLog;

import java.util.List;

public interface VisitLogService {
    void save(VisitLog visitLog);

    List<VisitLogDto> retrieveVisitLogs();
}
