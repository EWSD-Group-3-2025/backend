package org.teamSmurfs.backend.features.report.service;

import org.teamSmurfs.backend.features.report.dto.BrowserUsageDto;
import org.teamSmurfs.backend.features.report.dto.RouteUsageDto;

import java.util.List;

public interface ReportService {
    List<BrowserUsageDto> getUniqueUserCountByBrowser();
    List<RouteUsageDto> getTop5VisitedRoutes();
    List<Object> getMostVisitedUsers();
    List<Object> getMostActiveUsers();
	List<Object> findInactiveUsersBetweenDates();
	
}
