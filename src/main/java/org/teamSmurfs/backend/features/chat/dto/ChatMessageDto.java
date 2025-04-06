package org.teamSmurfs.backend.features.chat.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ChatMessageDto {
    private Long id;
    private Long senderId;
    private String senderUsername;
    private Long chatRoomId;
    private String content;
    private LocalDateTime timestamp;
}
