package org.teamSmurfs.backend.report.service;

import java.util.Map;

public interface ReportService {
    Map<String, Long> getUniqueUserCountByBrowser();
    Map<String, Long> getTop5VisitedRoutes();
}
