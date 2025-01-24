/*
 * @Author : Thant Htoo Aung
 * @Date : 1/24/2025
 * @Time : 09:00 AM
 */
package org.teamSmurfs.backend.security.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
}