package org.teamSmurfs.backend.report;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.teamSmurfs.backend.api.request.RequestUtils;
import org.teamSmurfs.backend.api.response.dto.ApiResponse;
import org.teamSmurfs.backend.api.response.utils.ResponseUtil;
import org.teamSmurfs.backend.api.user.dto.UserDto;
import org.teamSmurfs.backend.api.visit_log.service.VisitLogService;
import org.teamSmurfs.backend.report.dto.BrowserUsageDto;
import org.teamSmurfs.backend.report.dto.RouteUsageDto;
import org.teamSmurfs.backend.report.service.ReportService;

import java.util.Collections;
import java.util.List;


@RestController
@RequestMapping("/${api.base.path}")
@RequiredArgsConstructor
@Slf4j
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/admin/report/browser-count")
    public ResponseEntity<ApiResponse> getUniqueUserCountByBrowser(final HttpServletRequest request) {
        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        List<BrowserUsageDto> browserUsageList = reportService.getUniqueUserCountByBrowser();

        ApiResponse response = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(browserUsageList.isEmpty() ? Collections.emptyList() : browserUsageList) // Ensures empty array
                .message(browserUsageList.isEmpty() ? "No browser usage data found" :
                        "Unique user count by browser retrieved successfully")
                .build();

        return ResponseUtil.buildResponse(request, response, requestStartTime);
    }

    @GetMapping("/admin/report/top-routes")
    public ResponseEntity<ApiResponse> getTopVisitedRoutes(final HttpServletRequest request) {
        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        List<RouteUsageDto> topRoutes = reportService.getTop5VisitedRoutes();

        ApiResponse response = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(topRoutes.isEmpty() ? Collections.emptyList() : topRoutes) // Ensures empty array
                .message(topRoutes.isEmpty() ? "No top visited routes found" :
                        "Top 5 visited routes retrieved successfully")
                .build();

        return ResponseUtil.buildResponse(request, response, requestStartTime);
    }

    @GetMapping("/admin/report/top20-visited-users")
    public ResponseEntity<ApiResponse> getMostVisitedUsers(final HttpServletRequest request) {
        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        List<Object> mostVisitedUsers = reportService.getMostVisitedUsers();

        ApiResponse response = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(mostVisitedUsers.isEmpty() ? Collections.emptyList() : mostVisitedUsers) // Ensures empty array
                .message(mostVisitedUsers.isEmpty() ? "No most visited users found" :
                        "Most visited users retrieved successfully")
                .build();

        return ResponseUtil.buildResponse(request, response, requestStartTime);
    }
}
