package org.teamSmurfs.backend.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardChatMessage {
    private String senderUsername;
    private String content;
    private LocalDateTime timestamp;
}
