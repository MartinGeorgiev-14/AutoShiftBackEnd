package com.cars.carSaleWebsite.models.abstracts;

import com.cars.carSaleWebsite.models.entities.listing.ListingImage;
import com.cars.carSaleWebsite.models.entities.vehicle.Engine;
import com.cars.carSaleWebsite.models.entities.vehicle.Gearbox;
import com.cars.carSaleWebsite.models.entities.vehicle.Model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.*;

@MappedSuperclass
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class ListingAbstract {

    @Id
    @GeneratedValue
    @Column(name = "listing_id")
    protected UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id")
    private Model model;
    private BigDecimal price;
    private Date date;
    private int horsepower;
    private int mileage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "engine_id")
    private Engine engine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gearbox_id")
    private Gearbox gearbox;
    private String description;


}
