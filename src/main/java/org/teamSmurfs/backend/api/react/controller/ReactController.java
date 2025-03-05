package org.teamSmurfs.backend.api.react.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.teamSmurfs.backend.api.react.dto.CreateReactRequest;
import org.teamSmurfs.backend.api.react.dto.DeleteReactRequest;
import org.teamSmurfs.backend.api.react.service.ReactService;
import org.teamSmurfs.backend.api.request.RequestUtils;
import org.teamSmurfs.backend.api.response.dto.ApiResponse;
import org.teamSmurfs.backend.api.response.utils.ResponseUtil;

@RestController
@RequestMapping("${api.base.path}/reacts")
@RequiredArgsConstructor
@Slf4j
public class ReactController {
    private final ReactService reactService;

    @PostMapping
    public ResponseEntity<ApiResponse> giveReaction(
            @Validated @RequestBody final CreateReactRequest createReactRequest,
            final HttpServletRequest request
    ) {
        log.info("Giving react: {}", createReactRequest);

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        this.reactService.createReact(createReactRequest);

        log.info("React Submission Done: {}", createReactRequest);

        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.CREATED.value())
                .data(true)
                .message("React submitted successfully")
                .build();

        return ResponseUtil.buildResponse(request, successResponse, requestStartTime);
    }

    @DeleteMapping("/{entityId}")
    public ResponseEntity<ApiResponse> deleteReaction(
            @PathVariable final Long entityId,
            @Validated @RequestBody final DeleteReactRequest deleteReactRequest,
            final HttpServletRequest request
    ) {
        log.info("Deleting react: {}", deleteReactRequest);

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        deleteReactRequest.setEntityId(entityId);
        this.reactService.deleteReact(deleteReactRequest);

        log.info("React deletion done: {}", deleteReactRequest);

        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.NO_CONTENT.value())
                .data(true)
                .message("Blog deleted successfully")
                .build();

        return ResponseUtil.buildResponse(request, successResponse, requestStartTime);
    }
}
