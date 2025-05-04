package com.cars.carSaleWebsite.service.impl;

import com.cars.carSaleWebsite.dto.Listing.CRUD.FilterDto;
import com.cars.carSaleWebsite.helpers.UserIdentificator;
import com.cars.carSaleWebsite.mappers.FavoritesMapper;
import com.cars.carSaleWebsite.models.entities.email.EmailDetails;
import com.cars.carSaleWebsite.models.entities.listing.EditHistory;
import com.cars.carSaleWebsite.models.entities.listing.ListingVehicle;
import com.cars.carSaleWebsite.models.entities.user.UserEntity;
import com.cars.carSaleWebsite.models.entities.userFavorites.FavoriteFilter;
import com.cars.carSaleWebsite.models.entities.userFavorites.FavoriteListing;
import com.cars.carSaleWebsite.repository.EditHistoryRepository;
import com.cars.carSaleWebsite.repository.FavoriteFilterRepository;
import com.cars.carSaleWebsite.repository.FavoriteListingRepository;
import com.cars.carSaleWebsite.repository.UserEntityRepository;
import com.cars.carSaleWebsite.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.mapstruct.control.MappingControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;

@Service
public class EmailServiceImpl implements EmailService {


    private final JavaMailSender javaMailSender;
    private final UserIdentificator userIdentificator;
    private final UserEntityRepository userEntityRepository;
    private final FavoriteFilterRepository favoriteFilterRepository;
    private final ListingVehicleServiceImpl listingVehicleService;
    private final FavoritesMapper favoritesMapper;
    private final EditHistoryRepository editHistoryRepository;
    private final FavoriteListingRepository favoriteListingRepository;

    @Value("${spring.mail.username}") private String sender;

    @Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender, UserIdentificator userIdentificator,
                            UserEntityRepository userEntityRepository, FavoriteFilterRepository favoriteFilterRepository,
                            ListingVehicleServiceImpl listingVehicleService, FavoritesMapper favoritesMapper, EditHistoryRepository editHistoryRepository, FavoriteListingRepository favoriteListingRepository) {
        this.javaMailSender = javaMailSender;
        this.userIdentificator = userIdentificator;
        this.userEntityRepository = userEntityRepository;
        this.favoriteFilterRepository = favoriteFilterRepository;
        this.listingVehicleService = listingVehicleService;
        this.favoritesMapper = favoritesMapper;
        this.editHistoryRepository = editHistoryRepository;
        this.favoriteListingRepository = favoriteListingRepository;
    }

    @Override
    public String sendSimpleMail(EmailDetails details) {
        try{
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getRecipient());
            mailMessage.setText(details.getMsgBody());
            mailMessage.setSubject(details.getSubject());

            javaMailSender.send(mailMessage);
            return "Mail sent Successfully";
        }
        catch (Exception e){
            return "Error while sending email";
        }
    }

    @Override
    public String sendMailWithAttachment(EmailDetails details) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try{
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(details.getRecipient());
            mimeMessageHelper.setText(details.getMsgBody());
            mimeMessageHelper.setSubject(details.getSubject());

            FileSystemResource file = new FileSystemResource(details.getAttachment());
            mimeMessageHelper.addAttachment(file.getFilename(), file);

            javaMailSender.send(mimeMessage);
            return "Mail has been send successfully";

        } catch (Exception e) {
            return "Error while sending email";
        }
    }

//    @Scheduled(cron = "0 0 8 * * *")
    public void sendDailyFavoriteFilterNotification(){
        try {
            List<UserEntity> users = userEntityRepository.findAll();

            for(UserEntity user : users) {

                List<FavoriteFilter> filters = favoriteFilterRepository.findFavoriteFiltersByUserEntity(user);

                if(filters.isEmpty()) continue;


                MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

                helper.setFrom(sender);
                helper.setTo(user.getEmail());
                helper.setSubject("New listings for saved filters");

                LocalDate date = LocalDate.now();
                String displayDate = date.getDayOfMonth() + "/" + date.getMonthValue() + "/" + date.getYear();
                StringBuilder sb = new StringBuilder();
                String html = "<html>" +
                        "<body>" +
                        "<b>Hello!</b>" +
                        "<p>There are new listings as of " + displayDate + " that match your saved search criteria:</p>" +
                        "<div class='container'></div>";
                String htmlEnd = "</body> </html>";

                sb.append(html);

                for (FavoriteFilter f : filters) {
                    StringBuilder lb = new StringBuilder();

                    FilterDto filterDto = favoritesMapper.toFilterDtoFromFavoriteFilter(f, LocalDate.now().minusDays(1));
                    List<ListingVehicle> listings = listingVehicleService.searchCarByCriteria(filterDto);

                    if(listings.isEmpty()) continue;

                    sb.append("<br><div>");
                    lb.append("?page=1");
                    lb.append("&page=10");
                    lb.append("&sortDirection=ASC");

                    emailHandlerDouble(sb, lb, "Make and Model", "make", "model",
                            f.getMake() != null ? String.valueOf(f.getMake().getId()) : "",
                            f.getModel() != null ? String.valueOf(f.getModel().getId()) : "",
                            f.getMake() != null ? f.getMake().getName() : "",
                            f.getModel() != null ? f.getModel().getName() : "", true);

                    emailHandlerDouble(sb, lb, "Type and Body", "type", "body",
                            f.getType() != null ? String.valueOf(f.getType().getId()) : "",
                            f.getBody() != null ? String.valueOf(f.getBody().getId()) : "",
                            f.getType() != null ? f.getType().getType() : "",
                            f.getBody() != null ? f.getBody().getBody() : "", true);

                    emailHandlerDouble(sb, lb, "Region and Location", "region", "location",
                            f.getRegion() != null ? String.valueOf(f.getRegion().getId()) : "",
                            f.getLocation() != null ? String.valueOf(f.getLocation().getId()) : "",
                            f.getRegion() != null ? f.getRegion().getRegion() : "",
                            f.getLocation() != null ? f.getLocation().getLocation() : "", true);

                    emailHandlerSingle(sb, lb, "Engine", "engine",
                            f.getEngine() != null ? String.valueOf(f.getEngine().getId()) : "",
                            f.getEngine() != null ? f.getEngine().getType() : "");

                    emailHandlerSingle(sb, lb, "Gearbox", "gearbox",
                            f.getGearbox() != null ? String.valueOf(f.getGearbox().getId()) : "",
                            f.getGearbox() != null ? f.getGearbox().getType() : "");

                    emailHandlerSingle(sb, lb, "Color", "color",
                            f.getColor() != null ? String.valueOf(f.getColor().getId()) : "",
                            f.getColor() != null ? f.getColor().getColor() : "");

                    emailHandlerSingle(sb, lb, "Euro Standard", "euroStandard",
                            f.getEuroStandard() != null ? String.valueOf(f.getEuroStandard().getId()) : "",
                            f.getEuroStandard() != null ? f.getEuroStandard().getStandard() : "");

                    emailHandleStartStop(sb, lb, "Price", "$", "priceStart", "priceEnd",
                            f.getPriceStart() != null ? String.valueOf(f.getPriceStart()) : "",
                            f.getPriceEnd() != null ? String.valueOf(f.getPriceEnd()) : "");

                    emailHandleStartStop(sb, lb, "Manufacture Date", "", "manufactureDateStart", "manufactureDateEnd",
                            f.getManufactureDateStart() != null ? String.valueOf(f.getManufactureDateStart().getYear()) : "",
                            f.getManufactureDateEnd() != null ? String.valueOf(f.getManufactureDateEnd().getYear()) : "",
                            f.getManufactureDateStart() != null ? String.valueOf(f.getManufactureDateStart()) : "",
                            f.getManufactureDateEnd() != null ? String.valueOf(f.getManufactureDateEnd()) : "");

                    emailHandleStartStop(sb, lb, "Horsepower", "hp", "horsepowerStart", "horsepowerEnd",
                            f.getHorsepowerStart() != null ? String.valueOf(f.getHorsepowerStart()) : "",
                            f.getHorsepowerEnd() != null ? String.valueOf(f.getHorsepowerEnd()) : "");

                    emailHandleStartStop(sb, lb, "Mileage", "km", "mileageStart", "mileageEnd",
                            f.getMileageStart() != null ? String.valueOf(f.getMileageStart()) : "",
                            f.getMileageEnd() != null ? String.valueOf(f.getMileageEnd()) : "");

                    emailHandleStartStop(sb, lb, "Engine Displacement", "cc", "engineDisplacementStart", "engineDisplacementEnd",
                            f.getEngineDisplacementStart() != null ? String.valueOf(f.getEngineDisplacementStart()) : "",
                            f.getEngineDisplacementEnd() != null ? String.valueOf(f.getEngineDisplacementEnd()) : "");


                    lb.append("&createdStart=").append(LocalDate.now().minusDays(1));

                    sb.append("<a href='http://localhost:8080/api/app/search/fromEmail").append(lb).append("'>See Listings</a>");
                    sb.append("</div>");


                }

                sb.append(htmlEnd);

                helper.setText(sb.toString(), true);

                Boolean tst = sb.toString().contains("</a>");

                if(sb.toString().contains("</a>")){
                    javaMailSender.send(mimeMessage);
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void sendDailyFavoriteListingNotification(){
        try{
            List<UserEntity> users = userEntityRepository.findAll();

            for(UserEntity user : users){
                List<FavoriteListing> listings = favoriteListingRepository.findFavoriteListingsByUser(user);

                if(listings.isEmpty()) continue;

                MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

                helper.setFrom(sender);
                helper.setTo(user.getEmail());
                helper.setSubject("New listings for saved filters");

                LocalDate date = LocalDate.now();
                String displayDate = date.getDayOfMonth() + "/" + date.getMonthValue() + "/" + date.getYear();
                StringBuilder sb = new StringBuilder();
                String html = "<html>" +
                        "<body>" +
                        "<b>Hello!</b>" +
                        "<p>There are new price change notifications from favorite listings for: " + displayDate + "</p>" +
                        "<div class='container'></div>";
                String htmlEnd = "</body> </html>";

                sb.append(html);

                for(FavoriteListing l : listings){

                    EditHistory h = editHistoryRepository.getListingFromYesterday(l.getListingVehicle().getId(), LocalDate.now().minusDays(1));

                    if(h == null || !l.getIsNotify()) continue;

                    sb.append("<br><div>");

                    sb.append("<h2>" + h.getListingVehicle().getModel().getMake().getName() + " " + h.getListingVehicle().getModel().getName() + "</h2>");
                    sb.append("<p>Old price: " + h.getOldPrice() + " New price: " + h.getNewPrice() + "</p>");
                    sb.append("<a href='http://localhost:8080/api/app/" + h.getListingVehicle().getId() + "'>See Listing</a>");
                }

                sb.append(htmlEnd);

                helper.setText(sb.toString(), true);

                Boolean tst = sb.toString().contains("</a>");

                if(sb.toString().contains("</a>")){
                    javaMailSender.send(mimeMessage);
                }

            }

            List<EditHistory> history = editHistoryRepository.getListingFromYesterdayList(LocalDate.now().minusDays(1));
            editHistoryRepository.deleteAll(history);

        } catch (RuntimeException | MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private void emailHandlerSingle(StringBuilder sb, StringBuilder lb, String label, String var, String endPVar, String value){
        if(!Objects.equals(value, "")){
            sb.append("<p><span>" + label + ": </span>" + value + "</p>");
            lb.append("&" + var + "=" + endPVar);
        }
    }

    private void emailHandlerDouble(StringBuilder sb, StringBuilder lb, String label, String var1, String var2, String endPVar1, String endPVar2, String value1, String value2, Boolean coma){
        String comaOrInterval = coma ? ", " : " ";

        if(!Objects.equals(value1, "") && !Objects.equals(value2, "")){
            sb.append("<p><span>" + label + ": </span>" + value1 + comaOrInterval + value2 + "</p>");
            lb.append("&" + var1 + "=" + endPVar1);
            lb.append("&" + var2 + "=" + endPVar2);
        }else if(!Objects.equals(value1, "")){
            sb.append("<p><span>" + label + ": </span> Start: " + value1 + "</p>");
            lb.append("&" + var1 + "=" + endPVar1);
        }else if(!Objects.equals(value2, "")){
            sb.append("<p><span>" + label + ": </span> End: " + value2 + "</p>");
            lb.append("&" + var2 + "=" + endPVar2);
        }
    }

    private void emailHandleStartStop(StringBuilder sb, StringBuilder lb, String label, String notation, String var1, String var2, String value1, String value2){
        if(!Objects.equals(value1, "") && !Objects.equals(value2, "")){
            sb.append("<p><span>" + label + ": </span> Start: " + value1 + notation + " End: " + value2 + notation + "</p>");
            lb.append("&" + var1 + "=" + value1);
            lb.append("&" + var2 + "=" + value2);
        }else if(!Objects.equals(value1, "")){
            sb.append("<p><span>" + label + ": </span> Start: " + value1 + notation + "</p>");
            lb.append("&" + var1 + "=" + value1);
        }else if(!Objects.equals(value2, "")){
            sb.append("<p><span>" + label + ": </span> End: " + value2 + notation + "</p>");
            lb.append("&" + var2 + "=" + value2);
        }
    }

    private void emailHandleStartStop(StringBuilder sb, StringBuilder lb, String label, String notation, String var1, String var2, String value1, String value2, String StartPVar, String EndPVar){
        if(!Objects.equals(value1, "") && !Objects.equals(value2, "")){
            sb.append("<p><span>" + label + ": </span> Start: " + value1 + notation + " End: " + value2 + notation + "</p>");
            lb.append("&" + var1 + "=" + StartPVar);
            lb.append("&" + var2 + "=" + EndPVar);
        }else if(!Objects.equals(value1, "")){
            sb.append("<p><span>" + label + ": </span> Start: " + value1 + notation + "</p>");
            lb.append("&" + var1 + "=" + StartPVar);
        }else if(!Objects.equals(value2, "")){
            sb.append("<p><span>" + label + ": </span> End: " + value2 + notation + "</p>");
            lb.append("&" + var2 + "=" + EndPVar);
        }
    }

    private String emailRowCreatorSingle(String label, String value){
        return "<p><span>" + label + ": </span>" + value + "</p>";

    }

    private String emailRowCreatorDouble(String label, String value, String value2, Boolean coma){
        return coma
                ? "<p><span>" + label + ": </span>" + value + ", " + value2 + "</p>"
                : "<p><span>" + label + ": </span>" + value + " " + value2 + "</p>";
    }

    private String emailRowCreatorStartEnd(String label, String start, String end){
        if(start != null && end != null){
            return "<p><span>" + label + ": </span> Start: " + start + " End: " + end + "</p>";
        }else if(start != null){
            return "<p><span>" + label + ": </span> Start: " + start + "</p>";
        }else if(end != null){
            return "<p><span>" + label + ": </span> End: " + end + "</p>";
        }
        else return "";
    }

    private String emailRowCreatorStartEnd(String label, String start, String end, String notation){
        if(start != null && end != null){
            return "<p><span>" + label + ": </span> Start: " + start + notation + " End: " + end + notation + "</p>";
        }else if(start != null){
            return "<p><span>" + label + ": </span> Start: " + start + notation + "</p>";
        }else if(end != null){
            return "<p><span>" + label + ": </span> End: " + end + notation + "</p>";
        }
        else return "";
    }

    private <T, R> String addIfNotNull(String label, T source, Function<T, R> start, Function<T, R> end, String notation) {
        R startValue = start.apply(source);
        R endValue = end.apply(source);

        if(startValue != null && endValue != null){
            return "<p><span>" + label + ": </span> Start: " + startValue + notation + " End: " + endValue + notation + "</p>";
        }else if(startValue != null){
            return "<p><span>" + label + ": </span> Start: " + startValue + notation + "</p>";
        }else if(endValue != null){
            return "<p><span>" + label + ": </span> End: " + endValue + notation + "</p>";
        }
        else return "";
//        if (source != null) {
//            R value = getter.apply(source);
//            if (value != null) {
//                return "<p><span>" + label + "</span>" + value.toString() + "</p>";
//            }
//        }
    }

    private <T, R, U> void addDeepIfNotNull(StringJoiner joiner, String label, T root, List<Function<T, R>> level1Getter, T rootTwo, List<Function<T, R>> level2Getter) {

        R level1 = null;
        for(Function<T, R> r1 : level1Getter){
            level1 = r1.apply(root);
        }

        R level2 = null;

        for(Function<T, R> r2 : level2Getter){
            level2 = r2.apply(rootTwo);
        }
//        if (root != null) {
//            R level1 = level1Getter.apply(root);
//            if (level1 != null) {
//                U level2 = level2Getter.apply(level1);
//                if (level2 != null) {
//                    joiner.add(label + ": " + level2.toString());
//                }
//            }
//        }
    }



}
