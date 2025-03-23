package org.teamSmurfs.backend.api.chat.service;

import org.teamSmurfs.backend.api.chat.dto.ChatMessageDto;
import org.teamSmurfs.backend.api.chat.dto.ChatRoomData;
import org.teamSmurfs.backend.api.chat.dto.ChatRoomDto;
import org.teamSmurfs.backend.dashboard.dto.DashboardChatMessage;

import java.util.List;
import java.util.Set;

public interface ChatService {
    ChatRoomDto createOrGetChatRoom(final Long senderId, final Long receiverId);
    ChatRoomDto createGroupChat(final String groupName, final Set<Long> participantIds);
    ChatMessageDto sendMessage(final Long chatRoomId, final Long senderId, final String content);
    List<ChatMessageDto> getMessagesByChatRoom(final Long chatRoomId);
    List<ChatRoomData> retrieveChatRoomsByUserId(final Long userId);
    List<DashboardChatMessage> retrieveLastThreeChatMessagesByReceiverId(final Long receiverId);
}
