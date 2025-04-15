package com.cars.carSaleWebsite.dto.Listing;

import lombok.Data;

import java.util.HashSet;
import java.util.List;

@Data
public class CarPaginationResponse {
    private List<ListingCarDto> content;
    private int PageNo;
    private int PageSize;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;
}
