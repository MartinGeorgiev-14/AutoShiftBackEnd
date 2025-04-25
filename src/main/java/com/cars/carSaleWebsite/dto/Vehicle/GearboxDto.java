package com.cars.carSaleWebsite.dto.Vehicle;

import lombok.Data;
import org.springframework.objenesis.instantiator.util.UnsafeUtils;

import java.util.UUID;

@Data
public class GearboxDto {
    private UUID id;
    private String gearbox;
}
