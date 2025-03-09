package org.teamSmurfs.backend.report.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.teamSmurfs.backend.api.visit_log.model.VisitLog;
import org.teamSmurfs.backend.api.visit_log.repository.VisitLogRepository;
import org.teamSmurfs.backend.report.service.ReportService;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportServiceImpl implements ReportService {

    private final VisitLogRepository visitLogRepository;

    @Override
    public Map<String, Long> getUniqueUserCountByBrowser() {
        return visitLogRepository.findAll().stream()
                .filter(log -> log.getBrowserName() != null && log.getUser() != null) // Filter out null browser names and users
                .collect(Collectors.groupingBy(
                        VisitLog::getBrowserName,
                        Collectors.mapping(log -> log.getUser().getId(), Collectors.toSet()) // Collect unique user IDs per browser
                )).entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> (long) entry.getValue().size())); // Convert to count of unique users
    }


    @Override
    public Map<String, Long> getTop5VisitedRoutes() {
        return visitLogRepository.findAll().stream()
                .filter(log -> log.getRouteName() != null) // Ensure no null route names
                .collect(Collectors.groupingBy(
                        VisitLog::getRouteName,
                        Collectors.counting() // Count occurrences of each route
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed()) // Sort by count (desc)
                .limit(5) // Get the top 5
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }


}
