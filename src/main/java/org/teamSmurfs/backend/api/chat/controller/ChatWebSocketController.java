package org.teamSmurfs.backend.api.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.teamSmurfs.backend.api.chat.dto.ChatMessageDto;
import org.teamSmurfs.backend.api.chat.model.ChatMessage;
import org.teamSmurfs.backend.api.chat.service.ChatService;
import org.teamSmurfs.backend.api.response.dto.ApiResponse;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/{roomId}/send")
    public void sendMessage(ChatMessage message) {
        ChatMessageDto chatMessageDto = chatService.sendMessage(
                message.getChatRoom().getId(),
                message.getSender().getId(),
                message.getContent()
        );

        ApiResponse response = ApiResponse.builder()
                .success(1)
                .code(200)
                .data(chatMessageDto)
                .message("Message sent successfully")
                .build();

        messagingTemplate.convertAndSend("/topic/room/" + message.getChatRoom().getId(), response);
    }
}
