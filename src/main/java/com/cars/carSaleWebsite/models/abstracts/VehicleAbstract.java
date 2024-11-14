package com.cars.carSaleWebsite.models.abstracts;

import com.cars.carSaleWebsite.models.entities.Engine;
import com.cars.carSaleWebsite.models.entities.Gearbox;
import com.cars.carSaleWebsite.models.entities.Make;
import com.cars.carSaleWebsite.models.entities.Model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@MappedSuperclass
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class VehicleAbstract extends BaseAbstract {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id")
    private Model model;
    private BigDecimal price;
    private int year;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "engine_id")
    private Engine engine;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gearbox_id")
    private Gearbox gearbox;
}
