package org.teamSmurfs.backend.api.visit_log.service;

import jakarta.servlet.http.HttpServletRequest;
import org.teamSmurfs.backend.api.user.model.User;
import org.teamSmurfs.backend.api.visit_log.model.VisitLog;

public interface VisitLogService {
    void save(VisitLog visitLog);
}
