package org.teamSmurfs.backend.report.service;

import org.teamSmurfs.backend.report.dto.BrowserUsageDto;
import org.teamSmurfs.backend.report.dto.RouteUsageDto;

import java.util.List;
import java.util.Map;

public interface ReportService {
    List<BrowserUsageDto> getUniqueUserCountByBrowser();
    List<RouteUsageDto> getTop5VisitedRoutes();
}
