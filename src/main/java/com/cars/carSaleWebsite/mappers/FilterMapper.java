package com.cars.carSaleWebsite.mappers;

import com.cars.carSaleWebsite.dto.Listing.FilterDto;
import com.cars.carSaleWebsite.dto.Listing.CRUD.SearchDto;
import com.cars.carSaleWebsite.models.entities.vehicle.*;
import org.springframework.stereotype.Component;

@Component
public class FilterMapper {

    public FilterDto searchToFilter(SearchDto searchDto, Model model, Engine engine, Gearbox gearbox, Body body, Location location){
        FilterDto filter = new FilterDto();

//        filter.setLocation(location.getId() != null ? location.getId() : null);
//        filter.setGearbox(gearbox.getId() != null ? gearbox.getId() : null);
//        filter.setEngine(engine.getId() != null ? engine.getId() : null);
//        filter.setModel(model.getId() != null ? model.getId() : null);
//        filter.setMake(model.getMake().getId());
//        filter.setRegion(location.getRegion().getId());
//        filter.setEndYear(searchDto.getEndYear());
//        filter.setStartYear(searchDto.getStartYear());
//        filter.setStartPrice(searchDto.getStartPrice());
//        filter.setEndPrice(searchDto.getEndPrice());

        return filter;
    }
}
