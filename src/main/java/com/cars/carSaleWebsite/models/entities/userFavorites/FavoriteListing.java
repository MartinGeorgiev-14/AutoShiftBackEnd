package com.cars.carSaleWebsite.models.entities.userFavorites;

import com.cars.carSaleWebsite.models.abstracts.BaseAbstract;
import com.cars.carSaleWebsite.models.entities.listing.ListingVehicle;
import com.cars.carSaleWebsite.models.entities.user.UserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class FavoriteListing extends BaseAbstract {

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    private ListingVehicle listingVehicle;

}
