package com.cars.carSaleWebsite.models.entities.chat;

import com.cars.carSaleWebsite.models.abstracts.BaseAbstract;
import com.cars.carSaleWebsite.models.entities.listing.ListingVehicle;
import com.cars.carSaleWebsite.models.entities.user.UserEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.Fetch;

import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(
        name = "conversation",
        uniqueConstraints = @UniqueConstraint(columnNames = {"listing_vehicle_id", "buyer_id"})
)
public class Conversation {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    private ListingVehicle listingVehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity buyer;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    private Boolean isReadBySeller;
    private Boolean isReadByBuyer;

    private Integer newMessageCounterBuyer;
    private Integer newMessageCounterSeller;

    private LocalDateTime lastTimeChatted;

//    @Column(name = "unique_identifier", unique = true)
//    private String uniqueIdentifier;
}
