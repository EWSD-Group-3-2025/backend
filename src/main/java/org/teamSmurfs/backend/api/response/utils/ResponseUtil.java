/*
 * @Author : Thant Htoo Aung
 * @Date : 1/13/2025
 * @Time : 09:50 PM
 */
package org.teamSmurfs.backend.api.response.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.teamSmurfs.backend.api.response.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.HashMap;

public class ResponseUtil {

    public static ResponseEntity<ApiResponse> buildResponse(HttpServletRequest request, ApiResponse response, double requestTime) {
        HttpStatus status = HttpStatus.valueOf(response.getCode());

        if (response.getMeta() == null) {
            String method = request.getMethod();
            String endpoint = request.getRequestURI();
            response.setMeta(new HashMap<>());
            response.getMeta().put("method", method);
            response.getMeta().put("endpoint", endpoint);
        }

        response.setDuration(Instant.now().getEpochSecond() - requestTime);
        return new ResponseEntity<>(response, status);
    }
}