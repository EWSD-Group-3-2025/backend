package org.teamSmurfs.backend.features.visit_log.controller;

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
import org.teamSmurfs.backend.features.visit_log.dto.VisitLogDto;
import org.teamSmurfs.backend.features.visit_log.service.VisitLogService;

import java.util.List;

@RestController
@RequestMapping("/${api.base.path}/visit-logs")
@RequiredArgsConstructor
@Slf4j
public class VisitLogController {
    private final VisitLogService visitLogService;

    @GetMapping
    public ResponseEntity<ApiResponse> retrieveUsers(
            HttpServletRequest request
    ) {

        log.info("Retrieving visit-logs");

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        List<VisitLogDto> visitLogs = visitLogService.retrieveVisitLogs();

        log.info("Retrieved visit-logs: {}", visitLogs.toString());

        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(visitLogs)
                .message("Logs retrieved successfully")
                .build();

        return ResponseUtil.buildResponse(request, successResponse, requestStartTime);
    }
}
