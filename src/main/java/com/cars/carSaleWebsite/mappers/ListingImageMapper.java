package com.cars.carSaleWebsite.mappers;

import com.cars.carSaleWebsite.dto.Listing.ListingImageDto;
import com.cars.carSaleWebsite.models.entities.listing.ListingImage;
import org.springframework.stereotype.Component;

@Component
public class ListingImageMapper {

    public ListingImageDto toDTO(ListingImage image){
        ListingImageDto mapped = new ListingImageDto();

        mapped.setMain(image.getIsMain());
//        mapped.setImageData(image.getImageData());
        mapped.setType(image.getType());

        return mapped;
    }
}
