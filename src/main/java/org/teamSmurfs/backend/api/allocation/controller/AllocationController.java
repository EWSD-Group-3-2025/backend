package org.teamSmurfs.backend.api.allocation.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.teamSmurfs.backend.api.allocation.dto.CreateAllocationRequest;
import org.teamSmurfs.backend.api.allocation.dto.TransferStudentRequest;
import org.teamSmurfs.backend.api.allocation.service.AllocationService;
import org.teamSmurfs.backend.api.request.RequestUtils;
import org.teamSmurfs.backend.api.response.dto.ApiResponse;
import org.teamSmurfs.backend.api.response.utils.ResponseUtil;

@RestController
@RequestMapping("/api/v1/allocations")
@RequiredArgsConstructor
@Slf4j
public class AllocationController {

    private final AllocationService allocationService;

    @PostMapping
    public ResponseEntity<ApiResponse> bulkAllocate(
            @Valid @RequestBody CreateAllocationRequest requestPayload,
            final HttpServletRequest request
    ) {
        log.info("Processing bulk allocation request");

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        this.allocationService.allocate(requestPayload);

        log.info("Bulk allocation completed for {} students", requestPayload.getStudentIds().size());

        ApiResponse response = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(true)
                .message("Successful")
                .build();

        return ResponseUtil.buildResponse(request, response, requestStartTime);
    }

    @DeleteMapping("/deallocate-students")
    public ResponseEntity<ApiResponse> deallocateAllStudents(
            @RequestParam(value = "tutorId") final Long tutorId,
            final HttpServletRequest request
    ) {
        log.info("Processing bulk deallocate all-students request for tutor ID: {}", tutorId);

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        this.allocationService.deallocateAllStudents(tutorId);

        log.info("Completed deallocate all-students request for tutor ID: {}", tutorId);

        ApiResponse response = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.NO_CONTENT.value())
                .data(true)
                .message("Successful")
                .build();

        return ResponseUtil.buildResponse(request, response, requestStartTime);
    }

    @PostMapping("/transfer")
    public ResponseEntity<ApiResponse> transferStudents(
            @Valid @RequestBody final TransferStudentRequest transferStudentRequest,
            final HttpServletRequest request
    ) {
        log.info("Processing transfer request between Tutor {} and Tutor {}",
                transferStudentRequest.getFirstTutorId(), transferStudentRequest.getSecondTutorId());

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        this.allocationService.transferStudents(transferStudentRequest);

        log.info("Successfully transferred students between Tutor {} and Tutor {}",
                transferStudentRequest.getFirstTutorId(), transferStudentRequest.getSecondTutorId());

        ApiResponse response = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(true)
                .message("Transfer successful")
                .build();

        return ResponseUtil.buildResponse(request, response, requestStartTime);
    }
}
