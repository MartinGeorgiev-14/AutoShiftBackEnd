package com.cars.carSaleWebsite.dto.chat;

import com.cars.carSaleWebsite.models.entities.chat.Conversation;
import lombok.Data;

import java.util.List;

@Data
public class ConversationPaginationDto {
    List<ConversationDto> conversations;
    private int PageNo;
    private int PageSize;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;
}
