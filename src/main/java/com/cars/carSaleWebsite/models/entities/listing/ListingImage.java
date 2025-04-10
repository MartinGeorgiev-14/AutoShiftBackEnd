package com.cars.carSaleWebsite.models.entities.listing;

import com.cars.carSaleWebsite.models.abstracts.BaseAbstract;
import com.cars.carSaleWebsite.models.abstracts.ListingAbstract;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
public class ListingImage extends BaseAbstract {
    private boolean isMain;
    private String type;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(length = 10_000_000)
    private String url;

    @ManyToOne
    @JoinColumn(name = "listing_id", nullable = false, columnDefinition = "UUID")
    private ListingVehicle listingId;

    public boolean getIsMain() {
        return isMain;
    }
}
