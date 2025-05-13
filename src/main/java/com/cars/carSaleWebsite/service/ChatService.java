package com.cars.carSaleWebsite.service;

import com.cars.carSaleWebsite.dto.Listing.CarPaginationResponse;
import com.cars.carSaleWebsite.dto.chat.ChatMessageDTO;
import com.cars.carSaleWebsite.dto.chat.ChatMessagePaginationDTO;
import com.cars.carSaleWebsite.dto.chat.ConversationDto;
import com.cars.carSaleWebsite.models.entities.chat.ChatMessage;
import com.cars.carSaleWebsite.models.entities.chat.Conversation;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.List;
import java.util.UUID;

public interface ChatService {
//    public void processMessage(@Payload ChatMessageDTO message);
    public ConversationDto getOrCreateConversation(UUID listingId, UUID buyerId);
    public boolean canAccessConversation(UUID conversationId, String username);
    public ChatMessage sendMessage(UUID conversationId, String senderUsername, String content);
    public ChatMessagePaginationDTO getConversationMessages(UUID conversationId, String senderUsername, Integer pageNo, Integer pageSize);
    public List<Conversation> getBuyerConversations(UUID buyerId);
    public List<Conversation> getListingConversations(UUID listingId, UUID sellerId);
    public List<Conversation> getSellerAllConversations(UUID sellerId);
    public ConversationDto readConversation(UUID conversationId, String senderUsername);
}
