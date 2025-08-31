package com.example.websocket.model;

import com.common.bigdata.entity.core.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chat_message")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageType type;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User sender;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }

    // Custom DTO for WebSocket communication to avoid circular dependencies with User entity
    @Data
    @Builder
    public static class ChatMessageDTO {
        private MessageType type;
        private String content;
        private String sender;
        private String avatar;
    }
}
