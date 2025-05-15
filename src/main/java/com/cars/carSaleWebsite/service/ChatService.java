package com.cars.carSaleWebsite.service;

import com.cars.carSaleWebsite.dto.Listing.CarPaginationResponse;
import com.cars.carSaleWebsite.dto.chat.ChatMessageDTO;
import com.cars.carSaleWebsite.dto.chat.ChatMessagePaginationDTO;
import com.cars.carSaleWebsite.dto.chat.ConversationDto;
import com.cars.carSaleWebsite.dto.chat.ConversationPaginationDto;
import com.cars.carSaleWebsite.models.entities.chat.ChatMessage;
import com.cars.carSaleWebsite.models.entities.chat.Conversation;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ChatService {
//    public void processMessage(@Payload ChatMessageDTO message);
    public Map<String, Object> getOrCreateConversation(UUID listingId, UUID buyerId);
    public boolean canAccessConversation(UUID conversationId, String username);
    public Map<String, Object> sendMessage(UUID conversationId, String senderUsername, String content);
    public Map<String, Object> getConversationMessages(UUID conversationId, String senderUsername, Integer pageNo, Integer pageSize);
    public Map<String, Object> getUserConversations(Integer pageNo, Integer pageSize, Boolean whatUser);
    public Map<String, Object> getListingConversations(UUID listingId, UUID sellerId, Integer pageNo, Integer pageSize);
    public List<Conversation> getSellerAllConversations(UUID sellerId);
    public ConversationDto readConversation(UUID conversationId, String senderUsername);
}
