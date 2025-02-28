package org.teamSmurfs.backend.api.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.teamSmurfs.backend.api.chat.dto.ChatMessageDto;
import org.teamSmurfs.backend.api.chat.dto.ChatRoomData;
import org.teamSmurfs.backend.api.chat.dto.ChatRoomDto;
import org.teamSmurfs.backend.api.chat.service.ChatService;
import org.teamSmurfs.backend.api.request.RequestUtils;
import org.teamSmurfs.backend.api.response.dto.ApiResponse;
import org.teamSmurfs.backend.api.response.utils.ResponseUtil;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/room")
    public ResponseEntity<ApiResponse> createOrGetChatRoom(
            @RequestParam Long senderId,
            @RequestParam Long receiverId,
            HttpServletRequest request) {

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        ChatRoomDto chatRoom = chatService.createOrGetChatRoom(senderId, receiverId);

        ApiResponse response = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.CREATED.value())
                .data(chatRoom)
                .message("Chat room created or retrieved successfully")
                .build();

        return ResponseUtil.buildResponse(request, response, requestStartTime);
    }

    @PostMapping("/group")
    public ResponseEntity<ApiResponse> createGroupChat(
            @RequestParam String groupName,
            @RequestBody Set<Long> participantIds,
            HttpServletRequest request) {

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        ChatRoomDto chatRoom = chatService.createGroupChat(groupName, participantIds);

        ApiResponse response = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.CREATED.value())
                .data(chatRoom)
                .message("Group chat created successfully")
                .build();

        return ResponseUtil.buildResponse(request, response, requestStartTime);
    }

    @PostMapping("/{roomId}/message")
    public ResponseEntity<ApiResponse> sendMessage(
            @PathVariable Long roomId,
            @RequestParam Long senderId,
            @RequestParam String content,
            HttpServletRequest request) {

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        ChatMessageDto chatMessage = chatService.sendMessage(roomId, senderId, content);

        ApiResponse response = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(chatMessage)
                .message("Message sent successfully")
                .build();

        return ResponseUtil.buildResponse(request, response, requestStartTime);
    }

    @GetMapping("/rooms/{roomId}/messages")
    public ResponseEntity<ApiResponse> getMessagesByRoom(
            @PathVariable Long roomId,
            HttpServletRequest request) {

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        List<ChatMessageDto> messages = chatService.getMessagesByChatRoom(roomId);

        ApiResponse response = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(messages)
                .message("Messages retrieved successfully")
                .build();

        return ResponseUtil.buildResponse(request, response, requestStartTime);
    }

    @GetMapping("/rooms")
    public ResponseEntity<ApiResponse> getRoomsByUserId(
            @RequestParam(value = "userId") final Long userId,
            final HttpServletRequest request
    ) {

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        List<ChatRoomData> chatRoomList = this.chatService.retrieveChatRoomsByUserId(userId);

        ApiResponse response = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(chatRoomList)
                .message("Chat rooms retrieved successfully")
                .build();

        return ResponseUtil.buildResponse(request, response, requestStartTime);
    }
}
