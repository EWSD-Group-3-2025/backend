package org.teamSmurfs.backend.api.chat.service;

import org.teamSmurfs.backend.api.chat.dto.ChatMessageDto;
import org.teamSmurfs.backend.api.chat.dto.ChatRoomDto;

import java.util.List;
import java.util.Set;

public interface ChatService {
    ChatRoomDto createOrGetChatRoom(Long senderId, Long receiverId);
    ChatRoomDto createGroupChat(String groupName, Set<Long> participantIds);
    ChatMessageDto sendMessage(Long chatRoomId, Long senderId, String content);
    List<ChatMessageDto> getMessagesByChatRoom(Long chatRoomId);
}
