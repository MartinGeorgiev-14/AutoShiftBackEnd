package com.cars.carSaleWebsite.service.impl;

import com.cars.carSaleWebsite.dto.Listing.CarPaginationResponse;
import com.cars.carSaleWebsite.dto.chat.ChatMessageDTO;
import com.cars.carSaleWebsite.dto.chat.ChatMessagePaginationDTO;
import com.cars.carSaleWebsite.dto.chat.ConversationDto;
import com.cars.carSaleWebsite.dto.chat.ConversationPaginationDto;
import com.cars.carSaleWebsite.helpers.BodyCreator;
import com.cars.carSaleWebsite.helpers.MessageCreator;
import com.cars.carSaleWebsite.helpers.UserIdentificator;
import com.cars.carSaleWebsite.mappers.ChatMapper;
import com.cars.carSaleWebsite.models.entities.chat.ChatMessage;
import com.cars.carSaleWebsite.models.entities.chat.Conversation;
import com.cars.carSaleWebsite.models.entities.listing.ListingVehicle;
import com.cars.carSaleWebsite.models.entities.user.UserEntity;
import com.cars.carSaleWebsite.models.nonEntity.Message;
import com.cars.carSaleWebsite.repository.chat.ChatMessageRepository;
import com.cars.carSaleWebsite.repository.chat.ConversationRepository;
import com.cars.carSaleWebsite.repository.listing.ListingVehicleRepository;
import com.cars.carSaleWebsite.repository.user.UserEntityRepository;
import com.cars.carSaleWebsite.service.ChatService;
import com.cars.carSaleWebsite.service.ListingVehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChatServiceImpl implements ChatService {

    private final SimpMessagingTemplate messagingTemplate;
    private final ConversationRepository conversationRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserEntityRepository userEntityRepository;
    private final ListingVehicleRepository listingVehicleRepository;
    private final ChatMapper chatMapper;
    private final UserIdentificator userIdentificator;
    private final BodyCreator bodyCreator;
    private final MessageCreator messageCreator;

    @Autowired
    public ChatServiceImpl(SimpMessagingTemplate messagingTemplate, ConversationRepository conversationRepository, ChatMessageRepository chatMessageRepository, UserEntityRepository userEntityRepository, ListingVehicleRepository listingVehicleRepository, ChatMapper chatMapper, UserIdentificator userIdentificator, BodyCreator bodyCreator, MessageCreator messageCreator) {
        this.messagingTemplate = messagingTemplate;
        this.conversationRepository = conversationRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.userEntityRepository = userEntityRepository;
        this.listingVehicleRepository = listingVehicleRepository;
        this.chatMapper = chatMapper;
        this.userIdentificator = userIdentificator;
        this.bodyCreator = bodyCreator;
        this.messageCreator = messageCreator;
    }

//    @Transactional
//    public void processMessage(@Payload ChatMessageDTO message){
//        try{
//            Conversation conversation = conversationRepository.getReferenceById(message.getConversationId());
//            UserEntity sender = userEntityRepository.getReferenceById(message.getSenderId());
//
//            ChatMessage chatMessage = new ChatMessage();
//            chatMessage.setConversation(conversation);
//            chatMessage.setSender(sender);
//            chatMessage.setTimestamp(LocalDateTime.now());
//            chatMessageRepository.save(chatMessage);
//
//            messagingTemplate.convertAndSend("/topic/chat/" + conversation.getId(), message);
//
//        } catch (RuntimeException e) {
//            throw new RuntimeException(e);
//        }
//    }

    @Transactional
    public Map<String, Object> getOrCreateConversation(UUID listingId, UUID buyerId){

        try {

            Optional<Conversation> existingConversation = conversationRepository.findByListingVehicleIdAndBuyerId(listingId, buyerId);
            Map<String, Object> body = bodyCreator.create();
            Message message;


            if (existingConversation.isPresent()) {
                message = messageCreator.create(false, "Conversation Found", "The Conversation has been found", "success");
                body.put("response", chatMapper.convertToConversationDto(existingConversation.get()));
                body.put("message", message);
                body.put("status", HttpStatus.OK.value());
                return body;
            } else {
                Optional<ListingVehicle> listing = Optional.of(listingVehicleRepository.getReferenceById(listingId));
                Optional<UserEntity> buyer = Optional.of(userEntityRepository.getReferenceById(buyerId));

                //Checks if buyer is the same user as the seller
                if (listing.get().getUserEntity().getId().equals(buyer.get().getId())) {
                    throw new IllegalArgumentException("Cannot create conversation");
                }

                if (listing.isPresent() && buyer.isPresent()) {
                    Conversation newConversation = new Conversation();

                    newConversation.setListingVehicle(listing.get());
                    newConversation.setBuyer(buyer.get());
                    newConversation.setCreatedAt(LocalDateTime.now());
                    newConversation.setNewMessageCounterSeller(0);
                    newConversation.setNewMessageCounterBuyer(0);

                    conversationRepository.save(newConversation);

                    message = messageCreator.create(false, "Conversation Created", "The Conversation has been created", "success");
                    body.put("response", chatMapper.convertToConversationDto(newConversation));
                    body.put("message", message);
                    body.put("status", HttpStatus.OK.value());

                    return body;
                } else {
                    throw new IllegalArgumentException("Listing or buyer not found");
                }
            }
        } catch (RuntimeException ex) {
            return messageCreator.createErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public boolean canAccessConversation(UUID conversationId, String userId){

        try{
            Optional<Conversation> conversation = Optional.of(conversationRepository.getReferenceById(conversationId));
            UserEntity user = userEntityRepository.findByUsername(userId);

            if(conversation.isEmpty()){
                return false;
            }

            if(conversation.get().getBuyer().getId().equals(user.getId())){
                return true;
            }

            if(conversation.get().getListingVehicle().getUserEntity().getId().equals(user.getId())){
                return true;
            }

            return false;
        } catch (RuntimeException e) {
            throw new RuntimeException("Cannot access chat room");
        }


    }

    @Transactional
    public ConversationDto readConversation(UUID conversationId, String senderUsername){
        try{
            Optional<Conversation> conversation = Optional.of(conversationRepository.getReferenceById(conversationId));
            Optional<UserEntity> user = Optional.ofNullable(userEntityRepository.findByUserByUsername(senderUsername));

            if(conversation.isPresent() && user.isPresent()){
                if(canAccessConversation(conversationId, senderUsername)){
                    if(user.get().getId().equals(conversation.get().getBuyer().getId())){
                        conversation.get().setIsReadByBuyer(true);
                        conversation.get().setNewMessageCounterBuyer(0);
                    }
                    else{
                        conversation.get().setIsReadBySeller(true);
                        conversation.get().setNewMessageCounterSeller(0);
                    }

                    conversation.get().setLastTimeChatted(LocalDateTime.now());
                    conversationRepository.save(conversation.get());
                    return chatMapper.convertToConversationDto(conversation.get());

                }else {
                    throw new AccessDeniedException("User cannot access this chat room");
                }
            }else{
                throw new IllegalArgumentException("Chat room or sender not found");
            }


        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public Map<String, Object> sendMessage(UUID conversationId, String senderUsername, String content){
        try{
            Optional<Conversation> conversation = Optional.of(conversationRepository.getReferenceById(conversationId));
            Optional<UserEntity> sender = Optional.of(userEntityRepository.findByUserByUsername(senderUsername));

            if(conversation.isPresent() && sender.isPresent()){
                if(canAccessConversation(conversationId, senderUsername)){
                    ChatMessage message = new ChatMessage();
                    message.setConversation(conversation.get());
                    message.setSender(sender.get());
                    message.setContent(content);
                    message.setTimestamp(LocalDateTime.now());
                    conversation.get().setIsReadByBuyer(false);
                    conversation.get().setIsReadBySeller(false);

                    if(sender.get().getId().equals(conversation.get().getBuyer().getId())){
                        conversation.get().setIsReadByBuyer(true);
                        Integer increment = conversation.get().getNewMessageCounterBuyer() + 1;
                        conversation.get().setNewMessageCounterBuyer(increment);
                        conversation.get().setLastTimeChatted(LocalDateTime.now());
                    }
                    else{
                        conversation.get().setIsReadBySeller(true);
                        Integer increment = conversation.get().getNewMessageCounterSeller() + 1;
                        conversation.get().setNewMessageCounterSeller(increment);
                        conversation.get().setLastTimeChatted(LocalDateTime.now());
                    }

                    ChatMessage response = chatMessageRepository.save(message);
                    ChatMessageDTO messageDTO = chatMapper.convertToChatMessageDto(response);

                    Map<String, Object> body = bodyCreator.create();
                    Message messageBody = messageCreator.create(false, "Message send", "The message has been send", "success");


                    body.put("response", messageDTO);
                    body.put("message", messageBody);
                    body.put("status", HttpStatus.OK.value());

                    return body;
                }
                else{
                    throw new AccessDeniedException("User cannot access this chat room");
                }
            }
            else {
                throw new IllegalArgumentException("Chat room or sender not found");
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, Object> getConversationMessages(UUID conversationId, String senderUsername, Integer pageNo, Integer pageSize){

        try{
            if(canAccessConversation(conversationId, senderUsername)){
                Sort sort = Sort.by(Sort.Direction.DESC, "timestamp");
                Pageable pageRequest = PageRequest.of(pageNo, pageSize, sort);
                Page<ChatMessage> pageConversation = chatMessageRepository.findAllByConversationId(conversationId, pageRequest);
                List<ChatMessageDTO> messagesDto = pageConversation.stream().map(chatMapper::convertToChatMessageDto).toList();


                Map<String, Object> body = bodyCreator.create();
                Message message = messageCreator.create(false, "Messages Found", "Messages has been found", "success");

                body.put("response", chatMapper.toChatMessagePaginationDto(messagesDto, pageConversation));
                body.put("message", message);
                body.put("status", HttpStatus.OK.value());

                return body;
            }
            else{
                throw new AccessDeniedException("User cannot access this chat room");
            }
        } catch (RuntimeException ex) {
            return messageCreator.createErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);         
        }
    }

    public Map<String, Object> getUserConversations(Integer pageNo, Integer pageSize, Boolean whatUser){
        try{
            UUID userId = UUID.fromString(userIdentificator.getCurrentUserId());
            UserEntity user = userEntityRepository.getReferenceById(userId);
            Sort sort = Sort.by(Sort.Direction.DESC, "lastTimeChatted");
            Pageable pageRequest = PageRequest.of(pageNo, pageSize, sort);

            Page<Conversation> conversations;
            // Based on whatUser var returns listings for where user is buyer or seller
            if(whatUser){
                conversations = conversationRepository.findByBuyerId(user.getId(), pageRequest);
            }else{
                conversations = conversationRepository.findBySellerId(user.getId(), pageRequest);
            }
            List<ConversationDto> conversationDto = conversations.stream().map(chatMapper::convertToConversationDto).toList();
            ConversationPaginationDto conversationPaginationDto =  chatMapper.convertToConversationPaginationDto(conversationDto, conversations);

            Map<String, Object> body = bodyCreator.create();
            Message message = messageCreator.create(false, "Conversations Found", "The conversations have been found", "success");

            body.put("response", conversationPaginationDto);
            body.put("message", message);
            body.put("status", HttpStatus.OK.value());

            return body;
        } catch (RuntimeException ex) {
            return messageCreator.createErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public Map<String, Object> getListingConversations(UUID listingId, UUID sellerId, Integer pageNo, Integer pageSize){
        try{
            Optional<ListingVehicle> listing = Optional.of(listingVehicleRepository.getReferenceById(listingId));

            if(listing.isPresent() && listing.get().getUserEntity().getId().equals(sellerId)){
                Sort sort = Sort.by(Sort.Direction.DESC, "lastTimeChatted");
                Pageable pageRequest = PageRequest.of(pageNo, pageSize, sort);

                Page<Conversation> conversations = conversationRepository.findByListingVehicleId(listingId, pageRequest);
                List<ConversationDto> conversationDto = conversations.stream().map(chatMapper::convertToConversationDto).toList();
                ConversationPaginationDto conversationPaginationDto =  chatMapper.convertToConversationPaginationDto(conversationDto, conversations);

                Message message = messageCreator.create(false, "Listing Conversations Found", "The listing conversations have been found", "success");
                Map<String, Object> body = bodyCreator.create();

                body.put("response", conversationPaginationDto);
                body.put("message", message);
                body.put("status", HttpStatus.OK.value());

                return body;
            } else {
                throw new AccessDeniedException("User is not the seller of this listing");
            }
        } catch (RuntimeException ex) {
            return messageCreator.createErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public List<Conversation> getSellerAllConversations(UUID sellerId){
        try{
            List<ListingVehicle> sellerListings = listingVehicleRepository.findByUserEntityId(sellerId);

            List<Conversation> allConversations = new ArrayList<>();
//            for (ListingVehicle listing : sellerListings){
//                allConversations.addAll(conversationRepository.findByListingVehicleId(listing.getId()));
//            }

            return allConversations;
        } catch (RuntimeException e) {
            throw new RuntimeException("Cannot get all conversations");
        }

    }
}
