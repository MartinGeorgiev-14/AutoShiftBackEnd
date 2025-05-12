package com.cars.carSaleWebsite.controllers;

import com.cars.carSaleWebsite.helpers.UserIdentificator;
import com.cars.carSaleWebsite.models.entities.email.EmailDetails;
import com.cars.carSaleWebsite.repository.userFavorite.FavoriteFilterRepository;
import com.cars.carSaleWebsite.repository.listing.ListingVehicleRepository;
import com.cars.carSaleWebsite.repository.user.UserEntityRepository;
import com.cars.carSaleWebsite.service.EmailService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/email/")
@RestController
public class TESTEmailController {

    private final FavoriteFilterRepository favoriteFilterRepository;
    private final ListingVehicleRepository listingVehicleRepository;
    private final UserIdentificator userIdentificator;
    private final UserEntityRepository userEntityRepository;
    private final EmailService emailService;

    public TESTEmailController(FavoriteFilterRepository favoriteFilterRepository, ListingVehicleRepository listingVehicleRepository,
                               UserIdentificator userIdentificator, UserEntityRepository userEntityRepository, EmailService emailService) {
        this.favoriteFilterRepository = favoriteFilterRepository;
        this.listingVehicleRepository = listingVehicleRepository;
        this.userIdentificator = userIdentificator;
        this.userEntityRepository = userEntityRepository;
        this.emailService = emailService;
    }

//    @PostMapping("app/search")
//    public void sendDailyFavoriteFilterNotification(){
//        String userId = userIdentificator.getCurrentUserId();
//        UserEntity user = userEntityRepository.getReferenceById(UUID.fromString(userId));
//        List<FavoriteFilter> filters = favoriteFilterRepository.findFavoriteFiltersByUserEntity(user);
//
//
//    }

    @PostMapping("app/send")
    public String sendMail(@RequestBody EmailDetails details){
        String status = emailService.sendSimpleMail(details);

        return status;
    }

    @PostMapping("app/send/attachment")
    public String sendMailWithAttachment(@RequestBody EmailDetails details) {
        String status = emailService.sendMailWithAttachment(details);

        return status;
    }

    @PostMapping("app/send/dailyFilter")
    public void sendDailyFavoriteFilterNotification() {
        emailService.sendDailyFavoriteFilterNotification();
    }

    @PostMapping("app/send/dailyListings")
    public void sendDailyFavoriteListingNotification(){
        emailService.sendDailyFavoriteListingNotification();
    }


}
