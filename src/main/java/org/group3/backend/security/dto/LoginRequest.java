/*
 * @Author : Thant Htoo Aung
 * @Date : 1/13/2024
 * @Time : 09:56 PM
 */
package org.group3.backend.security.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequest {
    private String email;
    private String password;
    private long requestTime;
}
