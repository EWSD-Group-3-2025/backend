package org.teamSmurfs.backend.api.meeting.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.teamSmurfs.backend.api.meeting.dto.MeetingResponse;
import org.teamSmurfs.backend.api.request.RequestUtils;
import org.teamSmurfs.backend.api.response.dto.ApiResponse;
import org.teamSmurfs.backend.api.response.utils.ResponseUtil;
import org.teamSmurfs.backend.api.meeting.dto.MeetingDto;
import org.teamSmurfs.backend.api.meeting.service.MeetingService;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/${api.base.path}/meetings")
@RequiredArgsConstructor
@Slf4j
public class MeetingController {

    private final MeetingService meetingService;

    @PostMapping
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
    public ResponseEntity<ApiResponse> deleteMeeting(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        log.info("Deleting meeting with ID: {}", id);

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        meetingService.delete(id);

        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(true)
                .message("Meeting deleted successfully")
                .build();

        return ResponseUtil.buildResponse(request, successResponse, requestStartTime);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getMeetingById(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        log.info("Retrieving meeting with ID: {}", id);

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        MeetingResponse meeting = meetingService.getById(id);

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
    public ResponseEntity<ApiResponse> getAllMeetings(
            HttpServletRequest request
    ) {
        log.info("Retrieving all meetings");

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        List<MeetingResponse> meetings = meetingService.getAll();

        log.info("Retrieved meetings: {}", meetings);

        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(meetings)
                .message("Meetings retrieved successfully")
                .build();

        return ResponseUtil.buildResponse(request, successResponse, requestStartTime);
    }
}
