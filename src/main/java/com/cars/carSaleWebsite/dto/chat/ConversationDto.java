package com.cars.carSaleWebsite.dto.chat;

import com.cars.carSaleWebsite.dto.Listing.ListingCarDto;
import com.cars.carSaleWebsite.dto.Vehicle.BrandModelDto;
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
    private LocalDateTime createdAt;
    private Boolean isReadByBuyer;
    private Boolean isReadBySeller;
}
