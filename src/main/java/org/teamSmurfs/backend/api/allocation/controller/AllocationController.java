package org.teamSmurfs.backend.api.allocation.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.teamSmurfs.backend.api.allocation.dto.AllocationDto;
import org.teamSmurfs.backend.api.allocation.service.AllocationService;
import org.teamSmurfs.backend.api.request.RequestUtils;
import org.teamSmurfs.backend.api.response.dto.ApiResponse;
import org.teamSmurfs.backend.api.response.utils.ResponseUtil;

@RestController
@RequestMapping("/api/v1/allocate")
@RequiredArgsConstructor
@Slf4j
public class AllocationController {

    private final AllocationService allocationService;

    @PostMapping
    public ResponseEntity<ApiResponse> allocateOrReallocate(
            @RequestParam("normal") boolean isNormal, 
            HttpServletRequest request
    ) {
        log.info("Processing {} request", isNormal ? "allocation" : "reallocation");

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        AllocationDto allocationResponse = isNormal 
                ? allocationService.allocate() 
                : allocationService.reallocate();

        log.info("{} successful for student: {}", isNormal ? "Allocation" : "Reallocation", allocationResponse.getStudentName());

        ApiResponse response = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(allocationResponse)
                .message(isNormal ? "Allocation successful" : "Reallocation successful")
                .build();

        return ResponseUtil.buildResponse(request, response, requestStartTime);
    }
}
