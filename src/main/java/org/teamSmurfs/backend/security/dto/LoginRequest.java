/*
 * @Author : Thant Htoo Aung
 * @Date : 1/13/2025
 * @Time : 09:56 PM
 */
package org.teamSmurfs.backend.security.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequest {
    private String email;
    private String password;
}
