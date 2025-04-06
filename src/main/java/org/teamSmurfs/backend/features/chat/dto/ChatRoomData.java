package org.teamSmurfs.backend.features.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomData {
    private Long currentUserId;
    private String currentUserName;
    private Long receiverId;
    private String receiverName;
    private String roomKey;
    private Long chatRoomId;
}
