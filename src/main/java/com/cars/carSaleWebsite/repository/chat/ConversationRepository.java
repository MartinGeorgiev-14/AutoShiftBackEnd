package com.cars.carSaleWebsite.repository.chat;

import com.cars.carSaleWebsite.models.entities.chat.Conversation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConversationRepository extends JpaRepository<Conversation, UUID> {


//    @Query("SELECT c FROM Conversation c WHERE " +
//            "c.buyer.id = :buyer AND " +
//            "c.seller.id = :seller AND " +
//            "c.listingVehicle.id = :listing")
//    Conversation findByBuyerAndSellerAndListing(@Param("buyer") UUID buyer,
//                                                @Param("seller") UUID seller,
//                                                @Param("listing") UUID listing);

    Optional<Conversation> findByListingVehicleIdAndBuyerId(UUID listingId, UUID buyerId);
    Page<Conversation> findByBuyerId(UUID buyerId, Pageable pageable);

    @Query("SELECT cr FROM Conversation cr WHERE cr.listingVehicle.userEntity.id = :sellerId")
    Page<Conversation> findBySellerId(@Param("sellerId") UUID sellerId, Pageable pageable);

    Page<Conversation> findByListingVehicleId(UUID listingId, Pageable pageable);
//    Optional<Conversation> findByUniqueIdentifier(String uniqueIdentifier);
    // Added to help find rooms by listing's seller ID

}
