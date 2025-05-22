package com.cars.carSaleWebsite.dto.chat;

import com.cars.carSaleWebsite.dto.Authentication.UserEntityDto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ChatMessageDTO {
    private UUID id;
    private UUID conversationId;
    private UUID senderId;
    private String senderUsername;
    private String content;
    private LocalDateTime timestamp;
    private UserEntityDto senderUser;
}
