package com.cars.carSaleWebsite.models.entities.listing;

import com.cars.carSaleWebsite.models.entities.user.UserEntity;
import com.cars.carSaleWebsite.models.entities.vehicle.Engine;
import com.cars.carSaleWebsite.models.entities.vehicle.Gearbox;
import com.cars.carSaleWebsite.models.entities.vehicle.Model;
import com.cars.carSaleWebsite.models.entities.vehicle.Type;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
public class ListingVehicle {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id")
    private Model model;

    private BigDecimal price;
    private Date createdAt;
    private Date editedAt;
    private int horsepower;
    private int mileage;
    @Column(nullable = false)
    private String description;
    @Column(nullable = true)
    private int engineDisplacement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "engine_id", nullable = false)
    private Engine engine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gearbox_id", nullable = false)
    private Gearbox gearbox;

    @OneToMany(mappedBy = "id", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<ListingImage> images = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", nullable = false)
    private Type type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

}
