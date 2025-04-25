package com.cars.carSaleWebsite.dto.UserFavorites;

import com.cars.carSaleWebsite.dto.Vehicle.*;
import com.cars.carSaleWebsite.dto.Vehicle.EngineDto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class FavoriteFilterResponseDto {

    private String name;
    private Boolean isNotify;
    private BigDecimal priceStart;
    private BigDecimal priceEnd;
    private Date manufactureDateStart;
    private Date manufactureDateEnd;
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
