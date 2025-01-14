/*
 * @Author : Thant Htoo Aung
 * @Date : 1/13/2024
 * @Time : 09:50 PM
 */
package org.group3.backend.api.response.utils;

import org.group3.backend.api.response.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {

    public static ResponseEntity<ApiResponse> buildResponse(ApiResponse response) {
        HttpStatus status = HttpStatus.valueOf(response.getCode());
        return new ResponseEntity<>(response, status);
    }
}