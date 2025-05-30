package com.cars.carSaleWebsite.dto.UserFavorites;

import com.cars.carSaleWebsite.dto.Vehicle.*;
import com.cars.carSaleWebsite.dto.Vehicle.EngineDto;
import lombok.Data;
import org.springframework.objenesis.instantiator.util.UnsafeUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Data
public class FavoriteFilterResponseDto {

    private UUID id;
    private String name;
    private Boolean isNotify;
    private BigDecimal priceStart;
    private BigDecimal priceEnd;
    private LocalDate manufactureDateStart;
    private LocalDate manufactureDateEnd;
    private Integer horsepowerStart;
    private Integer horsepowerEnd;
    private Integer mileageStart;
    private Integer mileageEnd;
    private Integer engineDisplacementStart;
    private Integer engineDisplacementEnd;

    private BrandModelDto brandModel;
    private EngineDto engine;
    private GearboxDto gearbox;
    private TypeBodyDto typeBody;
    private ColorDto color;
    private EuroStandardDto euroStandard;
    private RegionLocationDto regionLocation;
}
