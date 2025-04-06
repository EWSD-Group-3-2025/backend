package org.teamSmurfs.backend.features.chat.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.teamSmurfs.backend.features.chat.dto.ChatMessageDto;
import org.teamSmurfs.backend.features.chat.service.ChatService;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private static final CopyOnWriteArraySet<WebSocketSession> sessions = new CopyOnWriteArraySet<>();
    private final ChatService chatService;
    private final ObjectMapper objectMapper;

    public ChatWebSocketHandler(ChatService chatService, ObjectMapper objectMapper) {
        this.chatService = chatService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
        System.out.println("New connection established: " + session.getId());
    }

    @Override
    protected void handleTextMessage(@NotNull WebSocketSession session, TextMessage message) throws IOException {
        System.out.println("Received message: " + message.getPayload());

        try {
            JsonNode jsonNode = objectMapper.readTree(message.getPayload());
            Long chatRoomId = jsonNode.get("chatRoomId").asLong();
            Long senderId = jsonNode.get("senderId").asLong();
            String content = jsonNode.get("content").asText();

            ChatMessageDto chatMessageDto = chatService.sendMessage(chatRoomId, senderId, content);
            String chatMessageJson = objectMapper.writeValueAsString(chatMessageDto);

            for (WebSocketSession webSocketSession : sessions) {
                if (webSocketSession.isOpen()) {
                    webSocketSession.sendMessage(
                            new TextMessage(
                                    chatMessageJson
                            )
                    );
                }
            }
        } catch (Exception e) {
            System.err.println("Invalid message format: " + e.getMessage());
            session.sendMessage(new TextMessage("Error: Invalid message format"));
        }
    }

    @Override
    public void afterConnectionClosed(@NotNull WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        System.out.println("Connection closed: " + session.getId());
    }
}
