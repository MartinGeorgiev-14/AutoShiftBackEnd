package com.cars.carSaleWebsite.mappers;

import com.cars.carSaleWebsite.dto.Authentication.UserEntityDto;
import com.cars.carSaleWebsite.dto.Listing.ListingCarDto;
import com.cars.carSaleWebsite.dto.Listing.ListingImageDto;
import com.cars.carSaleWebsite.dto.chat.*;
import com.cars.carSaleWebsite.models.entities.chat.ChatMessage;
import com.cars.carSaleWebsite.models.entities.chat.Conversation;
import com.cars.carSaleWebsite.models.entities.listing.ListingImage;
import com.cars.carSaleWebsite.models.entities.user.UserEntity;
import com.cars.carSaleWebsite.repository.listing.ListingImageRepository;
import com.cars.carSaleWebsite.repository.user.UserEntityRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChatMapper {
    private final ListingCarMapper listingCarMapper;
    private final UserEntityMapper userEntityMapper;
    private final UserEntityRepository userEntityRepository;
    private final ListingImageRepository listingImageRepository;
    private final ListingImageMapper listingImageMapper;

    public ChatMapper(ListingCarMapper listingCarMapper, UserEntityMapper userEntityMapper, UserEntityRepository userEntityRepository, ListingImageRepository listingImageRepository, ListingImageMapper listingImageMapper) {
        this.listingCarMapper = listingCarMapper;
        this.userEntityMapper = userEntityMapper;
        this.userEntityRepository = userEntityRepository;
        this.listingImageRepository = listingImageRepository;
        this.listingImageMapper = listingImageMapper;
    }

    public ChatMessageDTO convertToChatMessageDto(ChatMessage message){
        ChatMessageDTO dto = new ChatMessageDTO();
        dto.setId(message.getId());
        dto.setConversationId(message.getConversation().getId());
        dto.setSenderId(message.getSender().getId());
        dto.setSenderUsername(message.getSender().getUsername());
        dto.setContent(message.getContent());
        dto.setTimestamp(message.getTimestamp());

        return dto;
    }
    public ChatMessagePaginationDTO toChatMessagePaginationDto(List<ChatMessageDTO> messages, Page<ChatMessage> pageConversation){
        ChatMessagePaginationDTO mapped = new ChatMessagePaginationDTO();

        mapped.setMessages(messages);
        mapped.setPageNo(pageConversation.getNumber());
        mapped.setPageSize(pageConversation.getSize());
        mapped.setTotalPages(pageConversation.getTotalPages());
        mapped.setTotalElements(pageConversation.getTotalElements());
        mapped.setFirst(pageConversation.isFirst());
        mapped.setLast(pageConversation.isLast());

        return mapped;
    }

    public ConversationDto convertToConversationDto(Conversation conversation){
        ConversationDto dto = new ConversationDto();
        UserEntity user = userEntityRepository.getReferenceById(conversation.getListingVehicle().getUserEntity().getId());
        UserEntityDto userDto = userEntityMapper.toDTO(user);
        List<ListingImage> images = listingImageRepository.getAllListingImagesByListing(conversation.getListingVehicle());
        List<ListingImageDto> imagesDto = images.stream().map(listingImageMapper::toDTO).toList();
        ListingCarDto listingDto = listingCarMapper.toDTO(conversation.getListingVehicle(), userDto, imagesDto);

        dto.setId(conversation.getId());
        dto.setListingCarDto(listingDto);
        dto.setFirstName(conversation.getListingVehicle().getUserEntity().getFirstName());
        dto.setLastName(conversation.getListingVehicle().getUserEntity().getLastName());
        dto.setPhone(conversation.getListingVehicle().getUserEntity().getPhone());
        dto.setCreatedAt(conversation.getCreatedAt());
        dto.setIsReadByBuyer(conversation.getIsReadByBuyer());
        dto.setIsReadBySeller(conversation.getIsReadBySeller());
        dto.setNewMessageCounterBuyer(conversation.getNewMessageCounterBuyer());
        dto.setNewMessageCounterSeller(conversation.getNewMessageCounterSeller());

        return dto;
    }

    public ConversationPaginationDto convertToConversationPaginationDto(List<ConversationDto> conversations, Page<Conversation> pageConversation){
        ConversationPaginationDto mapped = new ConversationPaginationDto();

        mapped.setConversations(conversations);
        mapped.setPageNo(pageConversation.getNumber());
        mapped.setPageSize(pageConversation.getSize());
        mapped.setTotalPages(pageConversation.getTotalPages());
        mapped.setTotalElements(pageConversation.getTotalElements());
        mapped.setFirst(pageConversation.isFirst());
        mapped.setLast(pageConversation.isLast());

        return mapped;
    }




}
