package com.cars.carSaleWebsite.dto.Listing.CRUD;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class PatchCarListingDto {
    // private UUID make;
    private UUID model;
    private List<MultipartFile> uploadImages;
    private BigDecimal price;
    //    private Date createdAt;
//    private Date editedAt;
    private Integer horsepower;
    private Integer mileage;
    private String description;
    private UUID engine;
    private UUID gearbox;
    private Integer engineDisplacement;
    private Integer mainImgIndex;
    private UUID mainImgId;
    private List<UUID> imgIdsToRemove;
    private Boolean isActive;
    //    private UUID mainImgId;
//    private List<ListingImageDto> images;
    private UUID color;
    private UUID euroStandard;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate manufactureDate;
//    private Boolean isActive;

    //    private String type;
    private UUID body;
    private UUID location;
//    private String region;
}
