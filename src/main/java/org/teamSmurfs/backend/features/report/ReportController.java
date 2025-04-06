package org.teamSmurfs.backend.features.report;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.teamSmurfs.backend.features.request.RequestUtils;
import org.teamSmurfs.backend.features.response.dto.ApiResponse;
import org.teamSmurfs.backend.features.response.utils.ResponseUtil;
import org.teamSmurfs.backend.features.report.dto.BrowserUsageDto;
import org.teamSmurfs.backend.features.report.dto.RouteUsageDto;
import org.teamSmurfs.backend.features.report.service.ReportService;

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

    
    @GetMapping("/admin/report/most-active-users")
    public ResponseEntity<ApiResponse> getMostActiveUsers(final HttpServletRequest request) {
        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        List<Object> mostActiveUsers = reportService.getMostActiveUsers();

        ApiResponse response = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(mostActiveUsers)
                .message("Most Active Users retrieved successfully")
                .build();

        return ResponseUtil.buildResponse(request, response, requestStartTime);
    }
    
    @GetMapping("/admin/report/inactivity-users-betweendates")
    public ResponseEntity<ApiResponse> findInactiveUsersBetweenDates(final HttpServletRequest request) {
        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        List<Object> InActivityUsers = reportService.findInactiveUsersBetweenDates();

        ApiResponse response = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(InActivityUsers)
                .message("InActivity Users between 7 from 28 days ago retrieved successfully")
                .build();

        return ResponseUtil.buildResponse(request, response, requestStartTime);
    }
}
