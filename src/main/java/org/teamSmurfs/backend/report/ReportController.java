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
import org.teamSmurfs.backend.report.service.ReportService;

import java.util.Map;

@RestController
@RequestMapping("/${api.base.path}")
@RequiredArgsConstructor
@Slf4j
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/admin/report/browser-count")
    public ResponseEntity<ApiResponse> getUniqueUserCountByBrowser(final HttpServletRequest request) {

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        Map<String, Long> browserUsage = reportService.getUniqueUserCountByBrowser();

        ApiResponse response = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(browserUsage)
                .message("Unique user count by browser retrieved successfully")
                .build();

        return ResponseUtil.buildResponse(request, response, requestStartTime);
    }

    @GetMapping("/admin/report/top-routes")
    public ResponseEntity<?> getTopVisitedRoutes(final HttpServletRequest request) {

        double requestStartTime = RequestUtils.extractRequestStartTime(request);
        Map<String, Long> topRoutes = reportService.getTop5VisitedRoutes();

        ApiResponse response = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(topRoutes)
                .message("Top 5 visited routes retrieved")
                .build();

        return ResponseUtil.buildResponse(request, response, requestStartTime);

    }


}
