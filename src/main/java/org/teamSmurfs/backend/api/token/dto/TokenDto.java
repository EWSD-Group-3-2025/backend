package org.teamSmurfs.backend.api.token.dto;

import java.time.Instant;

import org.teamSmurfs.backend.api.token.dto.TokenDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto {
    private Long id;
    private Long userId;
    private String refreshtoken;
    private Instant expiredAt;
}