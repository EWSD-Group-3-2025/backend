package org.teamSmurfs.backend.features.specialization.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.teamSmurfs.backend.features.request.RequestUtils;
import org.teamSmurfs.backend.features.response.dto.ApiResponse;
import org.teamSmurfs.backend.features.response.utils.ResponseUtil;
import org.teamSmurfs.backend.features.specialization.dto.CreateSpecializationRequest;
import org.teamSmurfs.backend.features.specialization.dto.SpecializationDto;
import org.teamSmurfs.backend.features.specialization.dto.UpdateSpecializationRequest;
import org.teamSmurfs.backend.features.specialization.service.SpecializationService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/${api.base.path}/specializations")
@RequiredArgsConstructor
@Slf4j
public class SpecializationController {
    private final SpecializationService specializationService;

    @PostMapping
    public ResponseEntity<ApiResponse> create(
            @RequestBody final CreateSpecializationRequest createSpecializationRequest,
            final HttpServletRequest request
    ) {
        log.info("Creating specialization with names: {}", Arrays.toString(createSpecializationRequest.getNames()));

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        this.specializationService.create(createSpecializationRequest);

        log.info("Created specialization successfully: {}", Arrays.toString(createSpecializationRequest.getNames()));

        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.CREATED.value())
                .data(true)
                .message("Specialization created successfully")
                .build();

        return ResponseUtil.buildResponse(request, successResponse, requestStartTime);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> retrieveAllSpecializations(
            final HttpServletRequest request
    ) {
        log.info("Retrieving all specializations");

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        List<SpecializationDto> specializations = this.specializationService.retrieveAll();

        log.info("Retrieved all specializations successfully: {}", (specializations != null) ? specializations.size() : 0);

        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(specializations != null ? specializations : Collections.emptyList())
                .message("Specializations retrieved successfully")
                .build();

        return ResponseUtil.buildResponse(request, successResponse, requestStartTime);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> retrieveSpecialization(
            @PathVariable(value = "id") final Long id,
            final HttpServletRequest request
    ) {
        log.info("Retrieving specialization with id {}", id);

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        SpecializationDto specialization = this.specializationService.retrieveOne(id);

        log.info("Retrieved specialization successfully: {}", specialization);

        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(specialization)
                .message("Specialization retrieved successfully")
                .build();

        return ResponseUtil.buildResponse(request, successResponse, requestStartTime);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse> update(
            @PathVariable(value = "id") final Long id,
            @RequestBody final UpdateSpecializationRequest updateSpecializationRequest,
            final HttpServletRequest request

    ) {
        log.info("Updating specialization with id {}", id);

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        SpecializationDto updatedSpecialization = this.specializationService.update(id, updateSpecializationRequest);

        log.info("Updated specialization successfully: {}", updatedSpecialization.getName());

        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.NO_CONTENT.value())
                .data(true)
                .message("Specialization updated successfully")
                .build();

        return ResponseUtil.buildResponse(request, successResponse, requestStartTime);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(
            @PathVariable(value = "id") final Long id,
            final HttpServletRequest request
    ) {
        log.info("Deleting specialization with id {}", id);

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        this.specializationService.delete(id);

        log.info("Deleted specialization successfully with id {}", id);

        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.NO_CONTENT.value())
                .data(true)
                .message("Specialization deleted successfully")
                .build();

        return ResponseUtil.buildResponse(request, successResponse, requestStartTime);
    }
}
