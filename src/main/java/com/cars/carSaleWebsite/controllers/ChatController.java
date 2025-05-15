package com.cars.carSaleWebsite.controllers;

import com.cars.carSaleWebsite.helpers.UserIdentificator;
import com.cars.carSaleWebsite.helpers.bodyRequestHelpers.WhatUserRequest;
import com.cars.carSaleWebsite.models.entities.chat.Conversation;
import com.cars.carSaleWebsite.repository.user.UserEntityRepository;
import com.cars.carSaleWebsite.service.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
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

    @PostMapping("/conversations/{listingId}")
    public ResponseEntity<Map<String, Object>> getOrCreateChatRoom(
            @PathVariable UUID listingId) {

        User user = userIdentificator.getCurrentUser();
        String userId = userIdentificator.getCurrentUserId();

//        if (!userId.equals(buyerId) /* && !isAdmin(user) */) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//        }

        Map<String, Object> body = chatService.getOrCreateConversation(listingId, UUID.fromString(userId));
        Integer status = (Integer) body.get("status");

        if(status != 200)
        {
            return new ResponseEntity<>(body, HttpStatus.valueOf(status));
        }

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @GetMapping("/conversations/{conversationId}/messages")
    public ResponseEntity<Map<String, Object>> getChatMessages(
            @PathVariable UUID conversationId,
            @RequestParam(required = false, value = "pageNo", defaultValue = "0") Integer pageNo,
            @RequestParam(required = false, value = "pageSize", defaultValue = "10") Integer pageSize) {

        String userId = userIdentificator.getCurrentUserId();
        String username = userEntityRepository.getReferenceById(UUID.fromString(userId)).getUsername();

        if (!chatService.canAccessConversation(conversationId, username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Map<String, Object> body = chatService.getConversationMessages(conversationId, username, pageNo, pageSize);
        Integer status = (Integer) body.get("status");

        if(status != 200)
        {
            return new ResponseEntity<>(body, HttpStatus.valueOf(status));
        }

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @GetMapping("/user/conversations")
    public ResponseEntity<Map<String, Object>> getBuyerConversation(
            @RequestParam(required = false, value = "pageNo", defaultValue = "0") Integer pageNo,
            @RequestParam(required = false, value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestBody WhatUserRequest request
    ){

        Map<String, Object> body = chatService.getUserConversations(pageNo, pageSize, request.getWhatUser());
        Integer status = (Integer) body.get("status");

        if(status != 200)
        {
            return new ResponseEntity<>(body, HttpStatus.valueOf(status));
        }

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @GetMapping("/seller/listings/{listingId}/conversations")
    public ResponseEntity<Map<String, Object>> getListingChatRooms(
            @PathVariable UUID listingId,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false, value = "pageNo", defaultValue = "0") Integer pageNo,
            @RequestParam(required = false, value = "pageSize", defaultValue = "10") Integer pageSize) {

        String userId = userIdentificator.getCurrentUserId();

        Map<String, Object> body = chatService.getListingConversations(listingId, UUID.fromString(userId), pageNo, pageSize);
        Integer status = (Integer) body.get("status");

        if(status != 200)
        {
            return new ResponseEntity<>(body, HttpStatus.valueOf(status));
        }

        return new ResponseEntity<>(body, HttpStatus.OK);
    }
}

//    @MessageMapping("/chat.sendMessage")
//    public void processMessage(@Payload ChatMessageDTO message, Principal principal){
//        String username = principal.getName();
//        chatService.processMessage(message);
//    }
//}
