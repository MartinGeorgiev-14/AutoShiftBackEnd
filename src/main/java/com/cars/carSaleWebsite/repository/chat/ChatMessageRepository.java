package com.cars.carSaleWebsite.repository.chat;

import com.cars.carSaleWebsite.models.entities.chat.ChatMessage;
import com.cars.carSaleWebsite.models.entities.chat.Conversation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {
    List<ChatMessage> findByConversationIdOrderByTimestamp(UUID id);
    Page<ChatMessage> findAllByConversationId(UUID id, Pageable page);

}
