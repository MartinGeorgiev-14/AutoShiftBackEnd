package com.cars.carSaleWebsite.models.entities.userFavorites;

import com.cars.carSaleWebsite.models.abstracts.BaseAbstract;
import com.cars.carSaleWebsite.models.entities.user.UserEntity;
import com.cars.carSaleWebsite.models.entities.vehicle.*;
import com.fasterxml.jackson.databind.ser.Serializers;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
public class FavoriteFilter extends BaseAbstract {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id")
    private Model model;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "engine_id")
    private Engine engine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gearbox_id")
    private Gearbox gearbox;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "body_id", nullable = true)
    private Body body;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "color_id", referencedColumnName = "id")
    private Color color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "euro_standard_id", referencedColumnName = "id")
    private EuroStandard euroStandard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

}
