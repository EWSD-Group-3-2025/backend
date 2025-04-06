/*
 * @Author : Thant Htoo Aung
 * @Date : 1/13/2025
 * @Time : 09:50 PM
 */
package org.teamSmurfs.backend.features.response.dto;

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