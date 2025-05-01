package com.cars.carSaleWebsite.mappers;

import com.cars.carSaleWebsite.dto.Authentication.UserEntityDto;
import com.cars.carSaleWebsite.dto.Listing.CRUD.FilterDto;
import com.cars.carSaleWebsite.dto.Listing.ListingImageDto;
import com.cars.carSaleWebsite.dto.UserFavorites.*;
import com.cars.carSaleWebsite.dto.Vehicle.*;
import com.cars.carSaleWebsite.models.entities.listing.ListingVehicle;
import com.cars.carSaleWebsite.models.entities.user.UserEntity;
import com.cars.carSaleWebsite.models.entities.userFavorites.FavoriteFilter;
import com.cars.carSaleWebsite.models.entities.userFavorites.FavoriteListing;
import com.cars.carSaleWebsite.models.entities.vehicle.*;
import com.cars.carSaleWebsite.repository.*;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;

@Component
@Data
public class FavoritesMapper {

    private ModelRepository modelRepository;
    private EngineRepository engineRepository;
    private GearboxRepository gearboxRepository;
    private BodyRepository bodyRepository;
    private ColorRepository colorRepository;
    private EuroStandardRepository euroStandardRepository;
    private LocationRepository locationRepository;
    private MakeRepository makeRepository;
    private RegionRepository regionRepository;
    private TypeRepository typeRepository;

    public FavoritesMapper(ModelRepository modelRepository, EngineRepository engineRepository,
                           GearboxRepository gearboxRepository, BodyRepository bodyRepository,
                           ColorRepository colorRepository, EuroStandardRepository euroStandardRepository,
                           LocationRepository locationRepository, MakeRepository makeRepository,
                           RegionRepository regionRepository, TypeRepository typeRepository) {
        this.modelRepository = modelRepository;
        this.engineRepository = engineRepository;
        this.gearboxRepository = gearboxRepository;
        this.bodyRepository = bodyRepository;
        this.colorRepository = colorRepository;
        this.euroStandardRepository = euroStandardRepository;
        this.locationRepository = locationRepository;
        this.makeRepository = makeRepository;
        this.regionRepository = regionRepository;
        this.typeRepository = typeRepository;
    }

    public FilterPaginationResponseDto toFilterPagination(Page<FavoriteFilter> listings, List<FavoriteFilterResponseDto> content){
        FilterPaginationResponseDto mapped = new FilterPaginationResponseDto();

        mapped.setContent(content);
        mapped.setPageNo(listings.getNumber());
        mapped.setPageSize(listings.getSize());
        mapped.setTotalPages(listings.getTotalPages());
        mapped.setTotalElements(listings.getTotalElements());
        mapped.setFirst(listings.isFirst());
        mapped.setLast(listings.isLast());

        return mapped;
    }

    public FavoriteFilterResponseDto toFavoriteFilterDto(FavoriteFilter filter){
        FavoriteFilterResponseDto mapped = new FavoriteFilterResponseDto();

        mapped.setName(filter.getName());
        mapped.setIsNotify(filter.getIsNotify());
        mapped.setPriceStart(filter.getPriceStart());
        mapped.setPriceEnd(filter.getPriceEnd());
        mapped.setManufactureDateStart(filter.getManufactureDateStart());
        mapped.setManufactureDateEnd(filter.getManufactureDateEnd());
        mapped.setHorsepowerStart(filter.getHorsepowerStart());
        mapped.setHorsepowerEnd(filter.getHorsepowerEnd());
        mapped.setMileageStart(filter.getMileageStart());
        mapped.setMileageEnd(filter.getMileageEnd());
        mapped.setEngineDisplacementStart(filter.getEngineDisplacementStart());
        mapped.setEngineDisplacementEnd(filter.getEngineDisplacementEnd());
//
        BrandModelDto brandModel = new BrandModelDto();
        brandModel.setModelId(safeGet(filter.getModel(), Model::getId));
        brandModel.setModel(safeGet(filter.getModel(), Model::getName));
        brandModel.setBrandId(safeGet(safeGet(filter.getModel(), Model::getMake), Make::getId));
        brandModel.setBrand(safeGet(safeGet(filter.getModel(), Model::getMake), Make::getName));
        mapped.setBrandModel(brandModel);

        EngineDto engine = new EngineDto();
        engine.setId(safeGet(filter.getEngine(), Engine::getId));
        engine.setEngine(safeGet(filter.getEngine(), Engine::getType));
        mapped.setEngine(engine);

        GearboxDto gearbox = new GearboxDto();
        gearbox.setId(safeGet(filter.getGearbox(), Gearbox::getId));
        gearbox.setGearbox(safeGet(filter.getGearbox(), Gearbox::getType));
        mapped.setGearbox(gearbox);

        TypeBodyDto typeBody = new TypeBodyDto();
        typeBody.setTypeId(safeGet(safeGet(filter.getBody(), Body::getType), Type::getId));
        typeBody.setType(safeGet(safeGet(filter.getBody(), Body::getType), Type::getType));
        typeBody.setBodyId(safeGet(filter.getBody(), Body::getId));
        typeBody.setBody(safeGet(filter.getBody(), Body::getBody));
        mapped.setTypeBody(typeBody);

        ColorDto color = new ColorDto();
        color.setId(safeGet(filter.getColor(), Color::getId));
        color.setColor(safeGet(filter.getColor(), Color::getColor));
        mapped.setColor(color);

        EuroStandardDto euroStandard = new EuroStandardDto();
        euroStandard.setId(safeGet(filter.getEuroStandard(), EuroStandard::getId));
        euroStandard.setEuroStandard(safeGet(filter.getEuroStandard(), EuroStandard::getStandard));
        mapped.setEuroStandard(euroStandard);

        RegionLocationDto regionLocation = new RegionLocationDto();
        regionLocation.setRegionId(safeGet(safeGet(filter.getLocation(), Location::getRegion), Region::getId));
        regionLocation.setRegion(safeGet(safeGet(filter.getLocation(), Location::getRegion), Region::getRegion));
        regionLocation.setLocationId(safeGet(filter.getLocation(), Location::getId));
        regionLocation.setLocation(safeGet(filter.getLocation(), Location::getLocation));
        mapped.setRegionLocation(regionLocation);

        return mapped;
    }

    public FavoriteFilter toEntity(FilterDto filter, UserEntity user){
        FavoriteFilter mapped = new FavoriteFilter();

        Make make = makeRepository.findByIdOrNull(filter.getMake()).orElse(null);
        Model model = modelRepository.findByIdOrNull(filter.getModel()).orElse(null);
        Engine engine = engineRepository.findByIdOrNull(filter.getEngine()).orElse(null);
        Gearbox gearbox = gearboxRepository.findByIdOrNull(filter.getGearbox()).orElse(null);
        Type type = typeRepository.findByIdOrNull(filter.getType()).orElse(null);
        Body body = bodyRepository.findByIdOrNull(filter.getBody()).orElse(null);
        Color color = colorRepository.findByIdOrNull(filter.getColor()).orElse(null);
        EuroStandard euroStandard = euroStandardRepository.findByIdOrNull(filter.getEuroStandard()).orElse(null);
        Region region = regionRepository.findByIdOrNull(filter.getRegion()).orElse(null);
        Location location = locationRepository.findByIdOrNull(filter.getLocation()).orElse(null);

        mapped.setUserEntity(user);
        mapped.setName(filter.getName());
        mapped.setIsNotify(true);
        mapped.setMake(make);
        mapped.setModel(model);
        mapped.setEngine(engine);
        mapped.setGearbox(gearbox);
        mapped.setType(type);
        mapped.setBody(body);
        mapped.setColor(color);
        mapped.setEuroStandard(euroStandard);
        mapped.setRegion(region);
        mapped.setLocation(location);

        mapped.setPriceStart(filter.getPriceStart());
        mapped.setPriceEnd(filter.getPriceEnd());
        mapped.setManufactureDateStart(filter.getManufactureDateStart());
        mapped.setManufactureDateEnd(filter.getManufactureDateEnd());
        mapped.setHorsepowerStart(filter.getHorsepowerStart());
        mapped.setHorsepowerEnd(filter.getHorsepowerEnd());
        mapped.setMileageStart(filter.getMileageStart());
        mapped.setMileageEnd(filter.getMileageEnd());
        mapped.setEngineDisplacementStart(filter.getMileageStart());
        mapped.setEngineDisplacementEnd(filter.getEngineDisplacementEnd());

        return mapped;
    }

    public FavoriteListingDto toFavoriteListingDto(ListingVehicle vehicle, UserEntityDto mappedUser,
                                                   List<ListingImageDto> images, FavoriteListing fav){
        FavoriteListingDto car = new FavoriteListingDto();

        car.setBody(vehicle.getBody().getBody());
        car.setId(vehicle.getId());
        car.setDescription(vehicle.getDescription());
        car.setEngine(vehicle.getEngine().getType());
        car.setGearbox(vehicle.getGearbox().getType());
        car.setMake(vehicle.getModel().getMake().getName());
        car.setHorsepower(vehicle.getHorsepower());
        car.setMileage(vehicle.getMileage());
        car.setModel(vehicle.getModel().getName());
        car.setType(vehicle.getBody().getType().getType());
        car.setPrice(vehicle.getPrice());
        car.setCreatedAt(vehicle.getCreatedAt());
        car.setEditedAt(vehicle.getEditedAt());
        car.setEngineDisplacement(vehicle.getEngineDisplacement());
        car.setIsActive(vehicle.getIsActive());
        car.setManufactureDate(vehicle.getManufactureDate());
        car.setColor(vehicle.getColor().getColor());
        car.setEuroStandard(vehicle.getEuroStandard().getStandard());
        car.setUser(mappedUser);
        car.setRegion(vehicle.getLocation().getRegion().getRegion());
        car.setLocation(vehicle.getLocation().getLocation());
        car.setIsNotify(fav.getIsNotify());
        car.setImages(images);

        return car;
    }

    public FavoriteListingPaginationResponseDto toFavoriteListingPagination(Page<ListingVehicle> listings, List<FavoriteListingDto> content){
        FavoriteListingPaginationResponseDto mapped = new FavoriteListingPaginationResponseDto();

        mapped.setContent(content);
        mapped.setPageNo(listings.getNumber());
        mapped.setPageSize(listings.getSize());
        mapped.setTotalPages(listings.getTotalPages());
        mapped.setTotalElements(listings.getTotalElements());
        mapped.setFirst(listings.isFirst());
        mapped.setLast(listings.isLast());

        return mapped;
    }

    public FilterDto toFilterDtoFromFavoriteFilter(FavoriteFilter favorite, LocalDate startDate){
        FilterDto mapped = new FilterDto();

        mapped.setMake(safeGet(favorite.getMake(), Make::getId));
        mapped.setModel(safeGet(favorite.getModel(), Model::getId));
        mapped.setGearbox(safeGet(favorite.getGearbox(), Gearbox::getId));
        mapped.setType(safeGet(favorite.getType(), Type::getId));
        mapped.setBody(safeGet(favorite.getBody(), Body::getId));
        mapped.setColor(safeGet(favorite.getColor(), Color::getId));
        mapped.setEuroStandard(safeGet(favorite.getEuroStandard(), EuroStandard::getId));
        mapped.setRegion(safeGet(favorite.getRegion(), Region::getId));
        mapped.setLocation(safeGet(favorite.getLocation(), Location::getId));

        mapped.setManufactureDateEnd(favorite.getManufactureDateStart());
        mapped.setManufactureDateEnd(favorite.getManufactureDateEnd());
        mapped.setPriceStart(favorite.getPriceStart());
        mapped.setPriceEnd(favorite.getPriceEnd());
        mapped.setHorsepowerStart(favorite.getHorsepowerStart());
        mapped.setHorsepowerEnd(favorite.getHorsepowerEnd());
        mapped.setMileageStart(favorite.getMileageStart());
        mapped.setMileageEnd(favorite.getMileageEnd());
        mapped.setEngineDisplacementStart(favorite.getEngineDisplacementStart());
        mapped.setEngineDisplacementEnd(favorite.getEngineDisplacementEnd());
        mapped.setCreatedStart(startDate);
        mapped.setCreatedEnd(LocalDate.now());

        return mapped;
    }

    public <T, R> R safeGet(T obj, Function<T, R> getter) {
        try {
            return (obj != null) ? getter.apply(obj) : null;
        } catch (NullPointerException e) {
            return null;
        }
    }

}
