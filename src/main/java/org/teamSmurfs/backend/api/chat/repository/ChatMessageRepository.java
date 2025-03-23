package org.teamSmurfs.backend.api.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.teamSmurfs.backend.api.chat.model.ChatMessage;
import org.teamSmurfs.backend.api.chat.model.ChatRoom;
import org.teamSmurfs.backend.dashboard.dto.DashboardChatMessage;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatRoom(ChatRoom chatRoom);

    @Query("SELECT COUNT(m) FROM ChatMessage m WHERE m.sender.id = :userId AND m.timestamp >= :startOfDay")
    int countMessagesForToday(@Param("userId") Long userId, @Param("startOfDay") LocalDateTime startOfDay);

    @Query("""
        SELECT new org.teamSmurfs.backend.dashboard.dto.DashboardChatMessage(m.sender.username, m.content, m.timestamp)
        FROM ChatMessage m
        JOIN m.chatRoom cr
        JOIN cr.participants p
        WHERE p.id = :receiverId AND m.sender.id <> :receiverId
        ORDER BY m.timestamp DESC
        LIMIT 3
    """)
    List<DashboardChatMessage> retrieveLastThreeChatMessagesByReceiverId(@Param("receiverId") Long receiverId);

}
