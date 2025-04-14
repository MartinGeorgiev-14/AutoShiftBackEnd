package com.cars.carSaleWebsite.dto.Listing.CRUD;

import com.cars.carSaleWebsite.models.entities.vehicle.*;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    private HashSet<EuroStandard> euroStandardOptions;

    private HashSet<Color> colorOptions;
}
