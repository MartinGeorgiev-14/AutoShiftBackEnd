package com.cars.carSaleWebsite.dto.chat;

import com.cars.carSaleWebsite.dto.Authentication.UserEntityDto;
import com.cars.carSaleWebsite.dto.Listing.ListingCarDto;
import com.cars.carSaleWebsite.dto.Vehicle.BrandModelDto;
import com.cars.carSaleWebsite.models.entities.user.UserEntity;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ConversationDto {
    private UUID id;
    private ListingCarDto listingCarDto;
    private String firstName;
    private String lastName;
    private String phone;
    private UserEntityDto buyer;
    private LocalDateTime createdAt;
    private Boolean isReadByBuyer;
    private Boolean isReadBySeller;
    private Integer newMessageCounterBuyer;
    private Integer newMessageCounterSeller;
    private LocalDateTime lastTimeChatted;
}
