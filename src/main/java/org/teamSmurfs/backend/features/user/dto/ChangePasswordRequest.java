/*
 * @Author : Thant Htoo Aung
 * @Date : 1/14/2025
 * @Time : 10:46 AM (UTC)
 */
package org.teamSmurfs.backend.features.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {
    @NotBlank(message = "Old password cannot be empty")
    private String oldPassword;

    @NotBlank(message = "New password cannot be empty")
    private String newPassword;
}
