package com.example.websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Enables a simple in-memory message broker to carry messages back to the client on destinations prefixed with "/topic".
        config.enableSimpleBroker("/topic");
        // Designates the "/app" prefix for messages that are bound for @MessageMapping-annotated methods.
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Registers the "/gs-guide-websocket" endpoint, enabling SockJS fallback options so that alternate transports may be used if WebSocket is not available.
        registry.addEndpoint("/gs-guide-websocket").withSockJS();
    }

}
