package org.teamSmurfs.backend.features.react.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.teamSmurfs.backend.features.react.dto.ReactRequest;
import org.teamSmurfs.backend.features.react.service.ReactService;
import org.teamSmurfs.backend.features.request.RequestUtils;
import org.teamSmurfs.backend.features.response.dto.ApiResponse;
import org.teamSmurfs.backend.features.response.utils.ResponseUtil;

@RestController
@RequestMapping("${api.base.path}/reacts")
@RequiredArgsConstructor
@Slf4j
public class ReactController {
    private final ReactService reactService;

    @PostMapping
    public ResponseEntity<ApiResponse> handleReaction(
            @RequestHeader(value = "Authorization", required = false) final String authHeader,
            @Validated @RequestBody final ReactRequest reactRequest,
            final HttpServletRequest request) {

        log.info("Processing react with authenticated user.");

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        String maskedAuthHeader = authHeader != null ? authHeader.substring(0, Math.min(10, authHeader.length())) + "***" : "N/A";
        log.info("Processing react for Authorization: {}", maskedAuthHeader);

        final boolean isDeleteReaction = this.reactService.isReactionExists(authHeader, reactRequest.getEntityId(), reactRequest.getEntityType()) && reactRequest.getReact() == null;

        this.reactService.handleReaction(authHeader, reactRequest.getEntityId(), reactRequest.getEntityType(), reactRequest.getReact(), !isDeleteReaction);

        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(!isDeleteReaction ? HttpStatus.CREATED.value() : HttpStatus.NO_CONTENT.value())
                .data(true)
                .message("React " + (!isDeleteReaction ? "submitted" : "deleted") + " successfully")
                .build();

        return ResponseUtil.buildResponse(request, successResponse, requestStartTime);
    }
}
