package com.cars.carSaleWebsite.dto.UserFavorites;

import com.cars.carSaleWebsite.dto.Listing.ListingCarDto;
import lombok.Data;

import java.util.List;

@Data
public class FavoriteListingPaginationResponseDto {
    private List<FavoriteListingDto> content;
    private int PageNo;
    private int PageSize;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;
}
