package com.cars.carSaleWebsite.dto.Listing;

import com.cars.carSaleWebsite.models.entities.userFavorites.FavoriteFilter;
import lombok.Data;

import java.util.List;

@Data
public class FilterPaginationResponse {
    private List<FavoriteFilterDto> content;
    private int PageNo;
    private int PageSize;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;
}
