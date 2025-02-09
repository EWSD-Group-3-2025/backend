package org.teamSmurfs.backend.api.chat.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.teamSmurfs.backend.api.chat.dto.ChatMessageDto;
import org.teamSmurfs.backend.api.chat.dto.ChatRoomDto;
import org.teamSmurfs.backend.api.chat.model.ChatMessage;
import org.teamSmurfs.backend.api.chat.model.ChatRoom;
import org.teamSmurfs.backend.api.chat.repository.ChatMessageRepository;
import org.teamSmurfs.backend.api.chat.repository.ChatRoomRepository;
import org.teamSmurfs.backend.api.chat.service.ChatService;
import org.teamSmurfs.backend.api.user.model.User;
import org.teamSmurfs.backend.api.user.repository.UserRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ChatRoomDto createOrGetChatRoom(Long senderId, Long receiverId) {
        User sender = getUserById(senderId);
        User receiver = getUserById(receiverId);
        String roomKey = generateRoomKey(senderId, receiverId);

        ChatRoom chatRoom = chatRoomRepository.findByRoomKey(roomKey)
                .orElseGet(() -> createNewChatRoom(roomKey, Set.of(sender, receiver)));

        return mapToChatRoomDto(chatRoom);
    }

    @Override
    @Transactional
    public ChatRoomDto createGroupChat(String groupName, Set<Long> participantIds) {
        Set<User> participants = participantIds.stream()
                .map(this::getUserById)
                .collect(Collectors.toSet());

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.builder()
                .roomKey("GROUP_" + System.currentTimeMillis())
                .participants(participants)
                .build());

        return mapToChatRoomDto(chatRoom);
    }

    @Override
    @Transactional
    public ChatMessageDto sendMessage(Long chatRoomId, Long senderId, String content) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("Chat Room not found"));
        User sender = getUserById(senderId);

        ChatMessage message = chatMessageRepository.save(ChatMessage.builder()
                .chatRoom(chatRoom)
                .sender(sender)
                .content(content)
                .build());

        return mapToChatMessageDto(message);
    }

    @Override
    public List<ChatMessageDto> getMessagesByChatRoom(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("Chat Room not found"));

        return chatMessageRepository.findByChatRoom(chatRoom).stream()
                .map(this::mapToChatMessageDto)
                .collect(Collectors.toList());
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private String generateRoomKey(Long userId1, Long userId2) {
        return userId1 < userId2 ? userId1 + "_" + userId2 : userId2 + "_" + userId1;
    }

    private ChatRoom createNewChatRoom(String roomKey, Set<User> participants) {
        return chatRoomRepository.save(ChatRoom.builder()
                .roomKey(roomKey)
                .participants(participants)
                .build());
    }

    private ChatRoomDto mapToChatRoomDto(ChatRoom chatRoom) {
        return ChatRoomDto.builder()
                .id(chatRoom.getId())
                .roomKey(chatRoom.getRoomKey())
                .participantIds(chatRoom.getParticipants().stream()
                        .map(User::getId)
                        .collect(Collectors.toSet()))
                .build();
    }

    private ChatMessageDto mapToChatMessageDto(ChatMessage message) {
        return ChatMessageDto.builder()
                .id(message.getId())
                .senderId(message.getSender().getId())
                .senderUsername(message.getSender().getUsername())
                .chatRoomId(message.getChatRoom().getId())
                .content(message.getContent())
                .timestamp(message.getTimestamp())
                .build();
    }
}
