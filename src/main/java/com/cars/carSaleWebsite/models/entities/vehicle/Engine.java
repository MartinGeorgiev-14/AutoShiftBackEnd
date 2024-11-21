package com.cars.carSaleWebsite.models.entities.vehicle;

import com.cars.carSaleWebsite.models.abstracts.BaseAbstract;
import com.cars.carSaleWebsite.models.entities.listing.ListingVehicle;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Engine extends BaseAbstract {

    private String type;

//    @OneToMany(mappedBy = "engine", cascade = CascadeType.ALL)
//    private Set<ListingVehicle> vehicleSet = new HashSet<>();
}
