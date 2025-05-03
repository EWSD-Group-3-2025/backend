package org.teamSmurfs.backend.features.meeting.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.teamSmurfs.backend.features.meeting.dto.MeetingResponse;
import org.teamSmurfs.backend.features.request.RequestUtils;
import org.teamSmurfs.backend.features.response.dto.ApiResponse;
import org.teamSmurfs.backend.features.response.utils.ResponseUtil;
import org.teamSmurfs.backend.features.meeting.dto.MeetingDto;
import org.teamSmurfs.backend.features.meeting.service.MeetingService;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/${api.base.path}/meetings")
@RequiredArgsConstructor
@Slf4j
public class MeetingController {

    private final MeetingService meetingService;

    @PostMapping
//    @PreAuthorize("hasRole('TUTOR')")
    public ResponseEntity<ApiResponse> createMeeting(
            @RequestBody MeetingDto meetingDto,
            HttpServletRequest request
    ) {
        log.info("Creating a new meeting: {}", meetingDto);

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        MeetingResponse meetingResponse = meetingService.create(meetingDto);

        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(meetingResponse != null ? meetingResponse : Collections.emptyList())
                .message("Meeting created successfully")
                .build();

        return ResponseUtil.buildResponse(request, successResponse, requestStartTime);
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasRole('TUTOR')")
    public ResponseEntity<ApiResponse> updateMeeting(
            @PathVariable Long id,
            @RequestBody MeetingDto meetingDto,
            HttpServletRequest request
    ) {
        log.info("Updating meeting with ID: {}", id);

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        MeetingResponse updatedMeeting = meetingService.update(id, meetingDto);

        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(updatedMeeting)
                .message("Meeting updated successfully")
                .build();

        return ResponseUtil.buildResponse(request, successResponse, requestStartTime);
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse> deleteMeeting(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        log.info("Deleting meeting with ID: {}", id);

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        meetingService.delete(id);

        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.NO_CONTENT.value())
                .data(true)
                .message("Meeting deleted successfully")
                .build();

        return ResponseUtil.buildResponse(request, successResponse, requestStartTime);
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasRole('TUTOR')")
    public ResponseEntity<ApiResponse> getMeetingById(
            @PathVariable Long id,
            HttpServletRequest request,
            @RequestHeader("Authorization") String authHeader
    ) {
        log.info("Retrieving meeting with ID: {}", id);

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        MeetingResponse meeting = meetingService.getById(id, authHeader);

        log.info("Retrieved meeting: {}", meeting);

        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(meeting)
                .message("Meeting retrieved successfully")
                .build();

        return ResponseUtil.buildResponse(request, successResponse, requestStartTime);
    }

    @GetMapping
//    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF') or hasRole('TUTOR') or hasRole('STUDENT')")
    public ResponseEntity<ApiResponse> getAllMeetings(
            HttpServletRequest request,
            @RequestHeader("Authorization") String authHeader
    ) {
        log.info("Retrieving all meetings");

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        List<MeetingResponse> meetings = meetingService.getAll(authHeader);

        log.info("Retrieved meetings: {}", meetings);

        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(meetings)
                .message("Meetings retrieved successfully")
                .build();

        return ResponseUtil.buildResponse(request, successResponse, requestStartTime);
    }

    @PatchMapping("/{id}/mark-done")
//    @PreAuthorize("hasRole('TUTOR')")
    public ResponseEntity<ApiResponse> markMeetingAsDone(@PathVariable Long id,
                                                    HttpServletRequest request) {

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        meetingService.markMeetingAsDone(id);

        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(true)
                .message("Meeting marked as done successfully")
                .build();

        return ResponseUtil.buildResponse(request, successResponse, requestStartTime);

    }

}
