package com.cars.carSaleWebsite.dto;

import lombok.Data;

import java.util.HashSet;
import java.util.List;

@Data
public class CarPaginationResponse {
    private HashSet<ListingCarDto> content;
    private int PageNo;
    private int PageSize;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;
}
