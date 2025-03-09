package org.teamSmurfs.backend.dashboard.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.teamSmurfs.backend.api.request.RequestUtils;
import org.teamSmurfs.backend.api.response.dto.ApiResponse;
import org.teamSmurfs.backend.api.response.utils.ResponseUtil;
import org.teamSmurfs.backend.api.user.dto.StudentDto;
import org.teamSmurfs.backend.api.user.dto.TutorDto;
import org.teamSmurfs.backend.dashboard.dto.AdminDashboardDto;
import org.teamSmurfs.backend.dashboard.service.DashboardService;
import org.teamSmurfs.backend.security.config.SecurityConfig;
import org.teamSmurfs.backend.security.utils.JwtUtil;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@RestController
@RequestMapping("/${api.base.path}")
@RequiredArgsConstructor
@Slf4j
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/admin/dashboard")
    public ResponseEntity<ApiResponse> getDashboardData(final HttpServletRequest request) {

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        Object object = dashboardService.getAdminDashboardData();

        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(object)
                .message("Admin Dashboard data retrieved successfully")
                .build();

        return ResponseUtil.buildResponse(request, successResponse, requestStartTime);

    }

    @GetMapping("student/dashboard/{userId}")
    public ResponseEntity<ApiResponse> getTutorByStudentId(
            @PathVariable Long userId, final HttpServletRequest request) {

        double requestStartTime = RequestUtils.extractRequestStartTime(request);
        TutorDto tutorDto = dashboardService.getTutorByStudentId(userId);

        if (tutorDto == null) {
            ApiResponse emptyResponse = ApiResponse.builder()
                    .success(1)
                    .code(HttpStatus.OK.value())
                    .message("No active tutor found for user ID: " + userId)
                    .build();
            return ResponseUtil.buildResponse(request, emptyResponse, requestStartTime);
        }

        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(tutorDto)
                .message("Tutor details retrieved successfully")
                .build();

        return ResponseUtil.buildResponse(request, successResponse, requestStartTime);
    }

    @GetMapping("/tutor/dashboard/{userId}")
    public ResponseEntity<ApiResponse> getStudentsByTutorId(@PathVariable Long userId, HttpServletRequest request) {
        double requestStartTime = RequestUtils.extractRequestStartTime(request);
        List<StudentDto> students = dashboardService.getStudentsByTutorId(userId);

        if (students.isEmpty()) {
            ApiResponse emptyResponse = ApiResponse.builder()
                    .success(1)
                    .code(HttpStatus.OK.value())
                    .message("No students assigned to tutor ID: " + userId)
                    .build();
            return ResponseUtil.buildResponse(request, emptyResponse, requestStartTime);

        }

        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(students)
                .message("Students retrieved successfully for tutor ID: " + userId)
                .build();

        return ResponseUtil.buildResponse(request, successResponse, requestStartTime);
    }

    @GetMapping("/admin/dashboard/get-unassigned-students")
    public ResponseEntity<List<StudentDto>> getUnassignedStudents() {
        List<StudentDto> unassignedStudents = dashboardService.getUnassignedStudentsByTutorUserId();

        if (unassignedStudents.isEmpty()) {
            return ResponseEntity.noContent().build();  // Return 204 if no unassigned students
        }

        return ResponseEntity.ok(unassignedStudents);  // Return 200 OK with the list of unassigned students
    }

}
