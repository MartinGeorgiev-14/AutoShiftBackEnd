package com.cars.carSaleWebsite.dto.UserFavorites;

import lombok.Data;

import java.util.List;

@Data
public class FilterPaginationResponse {
    private List<FavoriteFilterResponseDto> content;
    private int PageNo;
    private int PageSize;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;
}
