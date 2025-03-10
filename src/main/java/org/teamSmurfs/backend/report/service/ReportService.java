package org.teamSmurfs.backend.report.service;

import org.teamSmurfs.backend.api.user.dto.UserDto;
import org.teamSmurfs.backend.report.dto.BrowserUsageDto;
import org.teamSmurfs.backend.report.dto.RouteUsageDto;

import java.util.List;

public interface ReportService {
    List<BrowserUsageDto> getUniqueUserCountByBrowser();
    List<RouteUsageDto> getTop5VisitedRoutes();
    List<Object> getMostVisitedUsers();
}
