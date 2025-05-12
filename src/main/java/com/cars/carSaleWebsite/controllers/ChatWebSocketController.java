package com.cars.carSaleWebsite.controllers;

import com.cars.carSaleWebsite.dto.chat.ChatMessageDTO;
import com.cars.carSaleWebsite.dto.chat.ChatMessageRequestDto;
import com.cars.carSaleWebsite.mappers.ChatMapper;
import com.cars.carSaleWebsite.models.entities.chat.ChatMessage;
import com.cars.carSaleWebsite.repository.chat.ChatMessageRepository;
import com.cars.carSaleWebsite.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.UUID;

@Controller
public class ChatWebSocketController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMapper chatMapper;

    @Autowired
    public ChatWebSocketController(ChatService chatService, SimpMessagingTemplate messagingTemplate, ChatMapper chatMapper) {
        this.chatService = chatService;
        this.messagingTemplate = messagingTemplate;
        this.chatMapper = chatMapper;
    }

    @MessageMapping("/chat/{conversationId}/send")
    public void sendMessage(@DestinationVariable UUID conversationId,
                            @Payload ChatMessageRequestDto messageRequest,
                            Principal principal){

        String senderUsername = principal.getName();

        if(chatService.canAccessConversation(conversationId, senderUsername)){
            ChatMessage savedMessage = chatService.sendMessage(conversationId, senderUsername, messageRequest.getContent());

            ChatMessageDTO messageDTO = chatMapper.convertToChatMessageDto(savedMessage);

            messagingTemplate.convertAndSend("/topic/chat/" + conversationId, messageDTO);
        }
    }
}
