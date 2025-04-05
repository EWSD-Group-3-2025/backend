package org.teamSmurfs.backend.features.dashboard.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.teamSmurfs.backend.features.chat.service.ChatService;
import org.teamSmurfs.backend.features.meeting.service.MeetingService;
import org.teamSmurfs.backend.features.request.RequestUtils;
import org.teamSmurfs.backend.features.response.dto.ApiResponse;
import org.teamSmurfs.backend.features.response.utils.ResponseUtil;
import org.teamSmurfs.backend.features.user.dto.StudentDto;
import org.teamSmurfs.backend.features.dashboard.dto.student.StudentDashboardResponse;
import org.teamSmurfs.backend.features.dashboard.dto.tutor.TutorDashboardResponse;
import org.teamSmurfs.backend.features.dashboard.service.DashboardService;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/${api.base.path}")
@RequiredArgsConstructor
@Slf4j
public class DashboardController {
    private final DashboardService dashboardService;
    private final MeetingService meetingService;
    private final ChatService chatService;

    @GetMapping("/admin/dashboard")
    public ResponseEntity<ApiResponse> getDashboardData(final HttpServletRequest request) {
        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        Object object = dashboardService.getAdminDashboardData();

        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(object != null ? object : Collections.emptyMap()) // Ensure empty object instead of null
                .message("Admin Dashboard data retrieved successfully")
                .build();

        return ResponseUtil.buildResponse(request, successResponse, requestStartTime);
    }

    @GetMapping("student/dashboard/{userId}")
    public ResponseEntity<ApiResponse> getTutorByStudentId(
            @PathVariable Long userId, final HttpServletRequest request) {

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        final StudentDashboardResponse studentDashboardResponse = new StudentDashboardResponse(
                this.dashboardService.getTutorByStudentId(userId),
                this.meetingService.getTodayMeetingsForStudent(userId),
                this.dashboardService.retrieveDashboardCountByStudentUserId(userId),
                this.chatService.retrieveLastThreeChatMessagesByReceiverId(userId)
        );

        ApiResponse response = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(studentDashboardResponse)
                .message("Successfully retrieved student dashboard")
                .build();

        return ResponseUtil.buildResponse(request, response, requestStartTime);
    }

    @GetMapping("/tutor/dashboard/{userId}")
    public ResponseEntity<ApiResponse> getStudentsByTutorId(@PathVariable Long userId, HttpServletRequest request) {
        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        final TutorDashboardResponse tutorDashboardResponse = new TutorDashboardResponse(
            dashboardService.getStudentsByTutorId(userId),
            this.dashboardService.retrieveDashboardCountByTutorUserId(userId),
            this.meetingService.getTodayMeetingsForTutor(userId)
        );

        ApiResponse response = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(tutorDashboardResponse)
                .message("Successfully retrieved tutor dashboard")
                .build();

        return ResponseUtil.buildResponse(request, response, requestStartTime);
    }

    @GetMapping("/admin/dashboard/get-unassigned-students")
    public ResponseEntity<ApiResponse> getUnassignedStudents(final HttpServletRequest request) {
        double requestStartTime = RequestUtils.extractRequestStartTime(request);
        List<StudentDto> unassignedStudents = dashboardService.getUnassignedStudentsByTutorUserId();

        ApiResponse response = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(unassignedStudents.isEmpty() ? Collections.emptyList() : unassignedStudents) // Ensure empty array
                .message(unassignedStudents.isEmpty() ? "No unassigned students found" :
                        "Unassigned students retrieved successfully")
                .build();

        return ResponseUtil.buildResponse(request, response, requestStartTime);
    }




}
