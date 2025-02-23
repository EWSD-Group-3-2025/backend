package org.teamSmurfs.backend.api.department.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.teamSmurfs.backend.api.department.dto.CreateDepartmentRequest;
import org.teamSmurfs.backend.api.department.dto.DepartmentDto;
import org.teamSmurfs.backend.api.department.dto.UpdateDepartmentRequest;
import org.teamSmurfs.backend.api.department.service.DepartmentService;
import org.teamSmurfs.backend.api.request.RequestUtils;
import org.teamSmurfs.backend.api.response.dto.ApiResponse;
import org.teamSmurfs.backend.api.response.utils.ResponseUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/${api.base.path}/departments")
@RequiredArgsConstructor
@Slf4j
public class DepartmentController {
    private final DepartmentService departmentService;

    @PostMapping
    public ResponseEntity<ApiResponse> create(
            @RequestBody final CreateDepartmentRequest createDepartmentRequest,
            final HttpServletRequest request
    ) {
        log.info("Creating department with names: {}", Arrays.toString(createDepartmentRequest.getNames()));

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        this.departmentService.create(createDepartmentRequest);

        log.info("Created department successfully: {}", Arrays.toString(createDepartmentRequest.getNames()));

        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.CREATED.value())
                .data(true)
                .message("Department created successfully")
                .build();

        return ResponseUtil.buildResponse(request, successResponse, requestStartTime);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> retrieveAllDepartments(
            final HttpServletRequest request
    ) {
        log.info("Retrieving all departments");

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        List<DepartmentDto> departments = this.departmentService.retrieveAll();

        log.info("Retrieved all departments successfully: {}", (departments != null) ? departments.size() : 0);

        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(departments != null ? departments : Collections.emptyList())
                .message("Departments retrieved successfully")
                .build();

        return ResponseUtil.buildResponse(request, successResponse, requestStartTime);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> retrieveDepartment(
            @PathVariable(value = "id") final Long id,
            final HttpServletRequest request
    ) {
        log.info("Retrieving department with id {}", id);

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        DepartmentDto department = this.departmentService.retrieveOne(id);

        log.info("Retrieved department successfully: {}", department);

        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(department)
                .message("Department created successfully")
                .build();

        return ResponseUtil.buildResponse(request, successResponse, requestStartTime);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse> update(
            @PathVariable(value = "id") final Long id,
            @RequestBody final UpdateDepartmentRequest updateDepartmentRequest,
            final HttpServletRequest request

    ) {
        log.info("Updating department with id {}", id);

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        DepartmentDto updatedDepartment = this.departmentService.update(id, updateDepartmentRequest);

        log.info("Updated department successfully: {}", updatedDepartment.getName());

        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.NO_CONTENT.value())
                .data(true)
                .message("Department updated successfully")
                .build();

        return ResponseUtil.buildResponse(request, successResponse, requestStartTime);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(
            @PathVariable(value = "id") final Long id,
            final HttpServletRequest request
    ) {
        log.info("Deleting department with id {}", id);

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        this.departmentService.delete(id);

        log.info("Deleted department successfully with id {}", id);

        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.NO_CONTENT.value())
                .data(true)
                .message("Department deleted successfully")
                .build();

        return ResponseUtil.buildResponse(request, successResponse, requestStartTime);
    }
}
