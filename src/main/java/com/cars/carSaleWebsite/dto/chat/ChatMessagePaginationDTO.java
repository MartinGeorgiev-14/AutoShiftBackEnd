package com.cars.carSaleWebsite.dto.chat;

import lombok.Data;

import java.util.List;

@Data
public class ChatMessagePaginationDTO {
    List<ChatMessageDTO> messages;
    private int PageNo;
    private int PageSize;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;
 }
