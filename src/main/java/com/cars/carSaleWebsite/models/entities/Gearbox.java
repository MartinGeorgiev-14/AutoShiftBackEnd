package com.cars.carSaleWebsite.models.entities;

import com.cars.carSaleWebsite.models.abstracts.BaseAbstract;
import com.cars.carSaleWebsite.models.abstracts.VehicleAbstract;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Gearbox extends BaseAbstract {

    private String type;

//    @OneToMany(mappedBy = "gearbox", cascade = CascadeType.ALL)
//    private Set<VehicleAbstract> vehicleSet;
}
