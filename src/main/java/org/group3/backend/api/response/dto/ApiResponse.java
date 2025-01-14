/*
 * @Author : Thant Htoo Aung
 * @Date : 1/13/2024
 * @Time : 09:50 PM
 */
package org.group3.backend.api.response.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class ApiResponse {
    private int success;
    private int code;
    private Map<String, Object> meta;
    private Object data;
    private String message;
    private double duration;
}