package org.teamSmurfs.backend.report.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.teamSmurfs.backend.api.visit_log.model.VisitLog;
import org.teamSmurfs.backend.api.visit_log.repository.VisitLogRepository;
import org.teamSmurfs.backend.report.dto.BrowserUsageDto;
import org.teamSmurfs.backend.report.dto.RouteUsageDto;
import org.teamSmurfs.backend.report.service.ReportService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportServiceImpl implements ReportService {

    private final VisitLogRepository visitLogRepository;

    @Override
    public List<BrowserUsageDto> getUniqueUserCountByBrowser() {
        return visitLogRepository.findAll().stream()
                .filter(log -> log.getBrowserName() != null && log.getUser() != null) // Ensure non-null values
                .collect(Collectors.groupingBy(
                        VisitLog::getBrowserName,
                        Collectors.mapping(log -> log.getUser().getId(), Collectors.toSet()) // Get unique user IDs per browser
                )).entrySet().stream()
                .map(entry -> new BrowserUsageDto(entry.getKey(), entry.getValue().size())) // Convert to DTO
                .collect(Collectors.toList()); // Convert to List<BrowserUsageDto>
    }


    @Override
    public List<RouteUsageDto> getTop5VisitedRoutes() {
        return visitLogRepository.findAll().stream()
                .filter(log -> log.getRouteName() != null) // Exclude null route names
                .collect(Collectors.groupingBy(
                        VisitLog::getRouteName,
                        Collectors.counting() // Count visits per route
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed()) // Sort by most visited
                .limit(5) // Keep only the top 5 routes
                .map(entry -> new RouteUsageDto(entry.getKey(), entry.getValue())) // Convert to DTO
                .collect(Collectors.toList()); // Convert to List<RouteUsageDto>
    }



}
