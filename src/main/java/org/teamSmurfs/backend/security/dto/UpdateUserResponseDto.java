package org.teamSmurfs.backend.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserResponseDto {
    private Long id;
    private String email;
    private String username;
    private String createdAt;
    private String updatedAt;
}
