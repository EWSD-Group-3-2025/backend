package org.teamSmurfs.backend.api.visit_log.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.teamSmurfs.backend.api.user.model.User;
import org.teamSmurfs.backend.api.visit_log.model.VisitLog;
import org.teamSmurfs.backend.api.visit_log.repository.VisitLogRepository;
import org.teamSmurfs.backend.api.visit_log.service.VisitLogService;
import org.teamSmurfs.backend.config.utils.EntityUtil;

@Service
@RequiredArgsConstructor
public class VisitLogServiceImpl implements VisitLogService {

    private final VisitLogRepository visitLogRepository;

    @Override
    public void save(VisitLog visitLog) {
        EntityUtil.saveEntityWithoutReturn(visitLogRepository, visitLog, "Visit Log");
    }

}
