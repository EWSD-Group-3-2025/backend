package org.teamSmurfs.backend.config.beans;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private static final String APPLICATION_DEST_PREFIX = "/app";
    private static final String BROKER_DEST_PREFIX = "/topic";
    private static final String STOMP_ENDPOINT = "/ws-chat";

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker(BROKER_DEST_PREFIX);
        registry.setApplicationDestinationPrefixes(APPLICATION_DEST_PREFIX);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(STOMP_ENDPOINT).setAllowedOrigins("*").withSockJS();
    }
}