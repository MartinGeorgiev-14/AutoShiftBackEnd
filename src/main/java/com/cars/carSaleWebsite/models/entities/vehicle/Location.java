package com.cars.carSaleWebsite.models.entities.vehicle;

import com.cars.carSaleWebsite.models.abstracts.BaseAbstract;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Location extends BaseAbstract {

    private String location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    Region region;
}
