package org.teamSmurfs.backend.features.chat.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class ChatRoomDto {
    private Long id;
    private String roomKey;
    private Set<Long> participantIds;
}
