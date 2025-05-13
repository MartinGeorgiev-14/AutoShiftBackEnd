package com.cars.carSaleWebsite.service.impl;

import com.cars.carSaleWebsite.dto.Listing.CarPaginationResponse;
import com.cars.carSaleWebsite.dto.chat.ChatMessageDTO;
import com.cars.carSaleWebsite.dto.chat.ChatMessagePaginationDTO;
import com.cars.carSaleWebsite.dto.chat.ConversationDto;
import com.cars.carSaleWebsite.mappers.ChatMapper;
import com.cars.carSaleWebsite.models.entities.chat.ChatMessage;
import com.cars.carSaleWebsite.models.entities.chat.Conversation;
import com.cars.carSaleWebsite.models.entities.listing.ListingVehicle;
import com.cars.carSaleWebsite.models.entities.user.UserEntity;
import com.cars.carSaleWebsite.repository.chat.ChatMessageRepository;
import com.cars.carSaleWebsite.repository.chat.ConversationRepository;
import com.cars.carSaleWebsite.repository.listing.ListingVehicleRepository;
import com.cars.carSaleWebsite.repository.user.UserEntityRepository;
import com.cars.carSaleWebsite.service.ChatService;
import com.cars.carSaleWebsite.service.ListingVehicleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ChatServiceImpl implements ChatService {

    private final SimpMessagingTemplate messagingTemplate;
    private final ConversationRepository conversationRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserEntityRepository userEntityRepository;
    private final ListingVehicleRepository listingVehicleRepository;
    private final ChatMapper chatMapper;

    public ChatServiceImpl(SimpMessagingTemplate messagingTemplate, ConversationRepository conversationRepository, ChatMessageRepository chatMessageRepository, UserEntityRepository userEntityRepository, ListingVehicleRepository listingVehicleRepository, ChatMapper chatMapper) {
        this.messagingTemplate = messagingTemplate;
        this.conversationRepository = conversationRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.userEntityRepository = userEntityRepository;
        this.listingVehicleRepository = listingVehicleRepository;
        this.chatMapper = chatMapper;
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
    public ConversationDto getOrCreateConversation(UUID listingId, UUID buyerId){

        Optional<Conversation> existingConversation = conversationRepository.findByListingVehicleIdAndBuyerId(listingId, buyerId);

        if(existingConversation.isPresent()){
            return chatMapper.convertToConversationDto(existingConversation.get());
        }
        else{
            Optional<ListingVehicle> listing = Optional.of(listingVehicleRepository.getReferenceById(listingId));
            Optional<UserEntity> buyer = Optional.of(userEntityRepository.getReferenceById(buyerId));

            if(listing.get().getUserEntity().getId().equals(buyer.get().getId())){
                throw new IllegalArgumentException("Cannot create conversation");
            }

            if(listing.isPresent() && buyer.isPresent()){
                Conversation newConversation = new Conversation();

                newConversation.setListingVehicle(listing.get());
                newConversation.setBuyer(buyer.get());
                newConversation.setCreatedAt(LocalDateTime.now());

                conversationRepository.save(newConversation);

                return chatMapper.convertToConversationDto(newConversation);
            } else {
                throw new IllegalArgumentException("Listing or buyer not found");
            }
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
                    }
                    else{
                        conversation.get().setIsReadBySeller(true);
                    }

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
    public ChatMessage sendMessage(UUID conversationId, String senderUsername, String content){
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
                    }
                    else{
                        conversation.get().setIsReadBySeller(true);
                    }


                    return chatMessageRepository.save(message);
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

    public ChatMessagePaginationDTO getConversationMessages(UUID conversationId, String senderUsername, Integer pageNo, Integer pageSize){

        try{
            if(canAccessConversation(conversationId, senderUsername)){
                Sort sort = Sort.by(Sort.Direction.DESC, "timestamp");
                Pageable pageRequest = PageRequest.of(pageNo, pageSize, sort);
                Page<ChatMessage> pageConversation = chatMessageRepository.findAllByConversationId(conversationId, pageRequest);
                List<ChatMessageDTO> messagesDto = pageConversation.stream().map(chatMapper::convertToChatMessageDto).toList();

                return chatMapper.toChatMessagePaginationDto(messagesDto, pageConversation);
            }
            else{
                throw new AccessDeniedException("User cannot access this chat room");
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Conversation> getBuyerConversations(UUID buyerId){
        try{
            return conversationRepository.findByBuyerId(buyerId);
        } catch (RuntimeException e) {
            throw new RuntimeException("Cannot get all conversations");
        }
    }

    public List<Conversation> getListingConversations(UUID listingId, UUID sellerId){
        try{
            Optional<ListingVehicle> listing = Optional.of(listingVehicleRepository.getReferenceById(listingId));
            if(listing.isPresent() && listing.get().getUserEntity().getId().equals(sellerId)){
                return conversationRepository.findByListingVehicleId(listingId);
            } else {
                throw new AccessDeniedException("User is not the seller of this listing");
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Conversation> getSellerAllConversations(UUID sellerId){
        try{
            List<ListingVehicle> sellerListings = listingVehicleRepository.findByUserEntityId(sellerId);

            List<Conversation> allConversations = new ArrayList<>();
            for (ListingVehicle listing : sellerListings){
                allConversations.addAll(conversationRepository.findByListingVehicleId(listing.getId()));
            }

            return allConversations;
        } catch (RuntimeException e) {
            throw new RuntimeException("Cannot get all conversations");
        }

    }
}
