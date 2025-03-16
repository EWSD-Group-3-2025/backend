package org.teamSmurfs.backend.api.chat.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.teamSmurfs.backend.api.chat.dto.ChatMessageDto;
import org.teamSmurfs.backend.api.chat.dto.ChatRoomData;
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
    public ChatRoomDto createOrGetChatRoom(final Long senderId, final Long receiverId) {
        User sender = this.getUserById(senderId);
        User receiver = this.getUserById(receiverId);
        String roomKey = this.generateRoomKey(senderId, receiverId);

        ChatRoom chatRoom = this.chatRoomRepository.findByRoomKey(roomKey)
                .orElseGet(() -> createNewChatRoom(roomKey, Set.of(sender, receiver)));

        return this.mapToChatRoomDto(chatRoom);
    }

    @Override
    @Transactional
    public ChatRoomDto createGroupChat(final String groupName, final Set<Long> participantIds) {
        Set<User> participants = participantIds.stream()
                .map(this::getUserById)
                .collect(Collectors.toSet());

        ChatRoom chatRoom = this.chatRoomRepository.save(ChatRoom.builder()
                .roomKey("GROUP_" + System.currentTimeMillis())
                .participants(participants)
                .build());

        return this.mapToChatRoomDto(chatRoom);
    }

    @Override
    @Transactional
    public ChatMessageDto sendMessage(final Long chatRoomId, final Long senderId, final String content) {
        ChatRoom chatRoom = this.chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("Chat Room not found"));
        User sender = getUserById(senderId);

        ChatMessage message = this.chatMessageRepository.save(ChatMessage.builder()
                .chatRoom(chatRoom)
                .sender(sender)
                .content(content)
                .build());

        return this.mapToChatMessageDto(message);
    }

    @Override
    public List<ChatMessageDto> getMessagesByChatRoom(final Long chatRoomId) {
        ChatRoom chatRoom = this.chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("Chat Room not found"));

        return this.chatMessageRepository.findByChatRoom(chatRoom).stream()
                .map(this::mapToChatMessageDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ChatRoomData> retrieveChatRoomsByUserId(final Long userId) {
        User currentUser = this.getUserById(userId);

        List<ChatRoom> chatRooms = this.chatRoomRepository.findChatRoomsByUserId(userId);

        return chatRooms.stream().map(chatRoom -> {
            User receiver = chatRoom.getParticipants().stream()
                    .filter(user -> !user.getId().equals(userId))
                    .findFirst()
                    .orElse(null);

            return new ChatRoomData(
                    currentUser.getId(),
                    currentUser.getUsername(),
                    receiver != null ? receiver.getId() : null,
                    receiver != null ? receiver.getUsername() : null,
                    chatRoom.getRoomKey(),
                    chatRoom.getId()
            );
        }).collect(Collectors.toList());
    }

    private User getUserById(Long userId) {
        return this.userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private String generateRoomKey(Long userId1, Long userId2) {
        return userId1 < userId2 ? userId1 + "_" + userId2 : userId2 + "_" + userId1;
    }

    private ChatRoom createNewChatRoom(String roomKey, Set<User> participants) {
        return this.chatRoomRepository.save(ChatRoom.builder()
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