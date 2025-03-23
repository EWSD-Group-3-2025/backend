package org.teamSmurfs.backend.api.visit_log.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.teamSmurfs.backend.api.user.model.User;
import org.teamSmurfs.backend.api.visit_log.dto.VisitLogDto;
import org.teamSmurfs.backend.api.visit_log.model.VisitLog;
import org.teamSmurfs.backend.api.visit_log.repository.VisitLogRepository;
import org.teamSmurfs.backend.api.visit_log.service.VisitLogService;
import org.teamSmurfs.backend.config.utils.EntityUtil;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VisitLogServiceImpl implements VisitLogService {
    private final ModelMapper modelMapper;

    private final VisitLogRepository visitLogRepository;

    @Override
    public void save(VisitLog visitLog) {
        EntityUtil.saveEntityWithoutReturn(visitLogRepository, visitLog, "Visit Log");
    }

    @Override
    public List<VisitLogDto> retrieveVisitLogs() {
        log.info("Retrieving Visit Logs");

        return visitLogRepository.findAll().stream()
                .map(v -> new VisitLogDto(v.getRouteName(), v.getBrowserName(), v.getUser().getUsername(), v.getPageName()))
                .collect(Collectors.toList());
    }

}
