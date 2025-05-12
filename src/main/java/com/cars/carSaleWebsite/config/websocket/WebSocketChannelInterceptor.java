package com.cars.carSaleWebsite.config.websocket;

import com.cars.carSaleWebsite.config.security.JWTGenerator;
import com.cars.carSaleWebsite.helpers.UserIdentificator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WebSocketChannelInterceptor implements ChannelInterceptor {

    private final UserIdentificator userIdentificator;
    private final JWTGenerator jwtGenerator;

    public WebSocketChannelInterceptor(UserIdentificator userIdentificator, JWTGenerator jwtGenerator) {
        this.userIdentificator = userIdentificator;
        this.jwtGenerator = jwtGenerator;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        System.out.println("Command: " + accessor.getCommand());
        System.out.println("Headers: " + accessor.toNativeHeaderMap());
        System.out.println("User: " + accessor.getUser());

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");
            System.out.println("JWT Header: " + authHeader); // Debug print

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);

                if (jwtGenerator.validateToken(token)) {
                    Authentication auth = jwtGenerator.getAuthentication(token);
                    accessor.setUser(auth);
                    SecurityContextHolder.getContext().setAuthentication(auth); // Optional, but safe
                }
            }
        }

        return message;
    }

    private Authentication validateJwtToken() {
        String username = userIdentificator.getCurrentUser().getUsername();
        return new UsernamePasswordAuthenticationToken(username, null, List.of());
    }

}
