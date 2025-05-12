package com.cars.carSaleWebsite.models.entities.chat;

import com.cars.carSaleWebsite.models.entities.user.UserEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
public class ChatMessage {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Conversation conversation;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity sender;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime timestamp;

}
