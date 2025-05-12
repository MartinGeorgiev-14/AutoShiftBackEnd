package com.cars.carSaleWebsite.config.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.stereotype.Component;

@Configuration
@EnableWebSecurity
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
                .simpTypeMatchers(SimpMessageType.CONNECT).authenticated()
                .simpSubscribeDestMatchers("/topic/**").permitAll()
                .simpDestMatchers("/app/**").permitAll()
                .simpTypeMatchers(SimpMessageType.DISCONNECT).permitAll()
                .anyMessage().authenticated();
    }
    @Override
    protected boolean sameOriginDisabled() {
        // Disable CSRF for WebSocket communication
        return true;
    }
}

