package com.cars.carSaleWebsite.controllers;

import com.cars.carSaleWebsite.dto.chat.ChatMessagePaginationDTO;
import com.cars.carSaleWebsite.dto.chat.ConversationDto;
import com.cars.carSaleWebsite.helpers.UserIdentificator;
import com.cars.carSaleWebsite.models.entities.chat.Conversation;
import com.cars.carSaleWebsite.repository.user.UserEntityRepository;
import com.cars.carSaleWebsite.service.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/chats")
public class ChatController {
    private final ChatService chatService;
    private final UserIdentificator userIdentificator;
    private final UserEntityRepository userEntityRepository;

    public ChatController(ChatService chatService, UserIdentificator userIdentificator, UserEntityRepository userEntityRepository) {
        this.chatService = chatService;
        this.userIdentificator = userIdentificator;
        this.userEntityRepository = userEntityRepository;
    }

    @PostMapping("/rooms/{listingId}")
    public ResponseEntity<ConversationDto> getOrCreateChatRoom(
            @PathVariable UUID listingId) {

        User user = userIdentificator.getCurrentUser();
        String userId = userIdentificator.getCurrentUserId();

//        if (!userId.equals(buyerId) /* && !isAdmin(user) */) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//        }

        ConversationDto conversation = chatService.getOrCreateConversation(listingId, UUID.fromString(userId));
        return new ResponseEntity<>(conversation, HttpStatus.OK);
    }

    @GetMapping("/rooms/{conversationId}/messages")
    public ResponseEntity<ChatMessagePaginationDTO> getChatMessages(
            @PathVariable UUID conversationId,
            @RequestParam(required = false, value = "pageNo", defaultValue = "0") Integer pageNo,
            @RequestParam(required = false, value = "pageSize", defaultValue = "10") Integer pageSize) {

        String userId = userIdentificator.getCurrentUserId();
        String username = userEntityRepository.getReferenceById(UUID.fromString(userId)).getUsername();

        if (!chatService.canAccessConversation(conversationId, username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        ChatMessagePaginationDTO messages = chatService.getConversationMessages(conversationId, username, pageNo, pageSize);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @GetMapping("/buyer/conversations")
    public ResponseEntity<List<Conversation>> getBuyerConversation(){
        String userId = userIdentificator.getCurrentUserId();

        List<Conversation> conversations = chatService.getBuyerConversations(UUID.fromString(userId));

        return new ResponseEntity<>(conversations, HttpStatus.OK);
    }

    @GetMapping("/seller/listings/{listingId}/conversations")
    public ResponseEntity<List<Conversation>> getListingChatRooms(
            @PathVariable UUID listingId,
            @AuthenticationPrincipal UserDetails userDetails) {

        String userId = userIdentificator.getCurrentUserId();

        try {
            List<Conversation> conversations = chatService.getListingConversations(listingId, UUID.fromString(userId));
            return new ResponseEntity<>(conversations, HttpStatus.OK);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping("/seller/conversations")
    public ResponseEntity<List<Conversation>> getAllSellerChatRooms(
            @AuthenticationPrincipal UserDetails userDetails) {

        String userId = userIdentificator.getCurrentUserId();

        List<Conversation> conversations = chatService.getSellerAllConversations(UUID.fromString(userId));
        return new ResponseEntity<>(conversations, HttpStatus.OK);
    }
}

//    @MessageMapping("/chat.sendMessage")
//    public void processMessage(@Payload ChatMessageDTO message, Principal principal){
//        String username = principal.getName();
//        chatService.processMessage(message);
//    }
//}
