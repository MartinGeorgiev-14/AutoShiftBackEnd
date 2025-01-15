package com.cars.carSaleWebsite.dto;

import com.cars.carSaleWebsite.models.entities.vehicle.*;
import lombok.Data;

import java.util.HashSet;

@Data
public class FormOptionsDto {
    private HashSet<Engine> engineOptions;
    private HashSet<Gearbox> gearboxOptions;

    private HashSet<Type> typeOptions;
    private HashSet<Body> bodyOptions;

    private HashSet<Make> makeOptions;
    private HashSet<Model> modelOptions;

    private HashSet<Region> regionOptions;
    private HashSet<Location> locationOptions;
}
