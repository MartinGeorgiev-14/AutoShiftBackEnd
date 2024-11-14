package com.cars.carSaleWebsite.models.entities;

import com.cars.carSaleWebsite.models.abstracts.BaseAbstract;
import com.cars.carSaleWebsite.models.abstracts.VehicleAbstract;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Engine extends BaseAbstract {

    private String type;
//
//    @OneToMany(mappedBy = "engine", cascade = CascadeType.ALL)
//    private Set<VehicleAbstract> vehicleSet;
}
