package com.example.websocket.controller;

import com.common.bigdata.entity.core.User;
import com.common.bigdata.repository.core.UserRepository;
import com.example.websocket.model.ChatMessage;
import com.example.websocket.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    @GetMapping("/api/users")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage.ChatMessageDTO sendMessage(@Payload ChatMessage.ChatMessageDTO chatMessageDTO, SimpMessageHeaderAccessor headerAccessor) {
        Long userId = (Long) headerAccessor.getSessionAttributes().get("userId");
        if (userId == null) {
            // Simple error handling for now
            return null;
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalStateException("User not found in session"));

        ChatMessage chatMessage = ChatMessage.builder()
                .type(ChatMessage.MessageType.CHAT)
                .content(chatMessageDTO.getContent())
                .sender(user)
                .timestamp(LocalDateTime.now())
                .build();
        chatMessageRepository.save(chatMessage);

        return ChatMessage.ChatMessageDTO.builder()
                .type(ChatMessage.MessageType.CHAT)
                .content(chatMessageDTO.getContent())
                .sender(user.getUsername())
                .avatar(user.getAvatar())
                .build();
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage.ChatMessageDTO addUser(@Payload ChatMessage.ChatMessageDTO chatMessageDTO, SimpMessageHeaderAccessor headerAccessor) {
        // The sender field in the DTO is expected to be the user ID from the frontend
        Long userId = Long.valueOf(chatMessageDTO.getSender());
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + userId));

        // Add userId in web socket session
        Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("userId", user.getId());

        ChatMessage chatMessage = ChatMessage.builder()
                .type(ChatMessage.MessageType.JOIN)
                .sender(user)
                .content(user.getUsername() + " has joined the chat!")
                .timestamp(LocalDateTime.now())
                .build();
        chatMessageRepository.save(chatMessage);

        return ChatMessage.ChatMessageDTO.builder()
                .type(ChatMessage.MessageType.JOIN)
                .content(user.getUsername() + " has joined the chat!")
                .sender(user.getUsername())
                .avatar(user.getAvatar())
                .build();
    }
}
