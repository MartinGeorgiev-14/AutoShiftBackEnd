package com.cars.carSaleWebsite.controllers;

import com.cars.carSaleWebsite.dto.Listing.CRUD.FilterDto;
import com.cars.carSaleWebsite.service.UserFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RequestMapping("/api/app/favorite/")
@Controller
public class UserFavoriteController {

    private UserFavoriteService userFavoriteService;

    @Autowired
    public UserFavoriteController(UserFavoriteService userFavoriteService) {
        this.userFavoriteService = userFavoriteService;
    }

    @GetMapping("listings")
    public ResponseEntity<Map<String, Object>> getAllFavoriteListings(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "price", required = false) String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = "ASC", required = false) String sortDirection){

        Map<String, Object> body = userFavoriteService.getAllFavoriteListings(pageNo, pageSize, sortBy, sortDirection);
        Integer status = (Integer) body.get("status");

        if(status != 200)
        {
            return new ResponseEntity<>(body, HttpStatus.valueOf(status));
        }

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @PostMapping("listing/add/{id}")
    public ResponseEntity<Map<String, Object>> addFavoriteListingToUser(@PathVariable UUID id){

        Map<String, Object> body = userFavoriteService.addFavoriteListingToUser(id);
        Integer status = (Integer) body.get("status");

        if(status != 200){
            return new ResponseEntity<>(body, HttpStatus.valueOf(status));
        }

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @PatchMapping("listing/change/notify/{id}")
    public ResponseEntity<Map<String, Object>> changeFavoriteListingNotify(@PathVariable UUID id){
        Map<String, Object> body = userFavoriteService.changeFavoriteListingNotify(id);
        Integer status = (Integer) body.get("status");

        if(status != 200)
        {
            return new ResponseEntity<>(body, HttpStatus.valueOf(status));
        }

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @DeleteMapping("listing/remove/{id}")
    public ResponseEntity<Map<String, Object>> removeFavoriteListingFromUser(@PathVariable UUID id){

        Map<String, Object> body = userFavoriteService.removeFavoriteListingFromUser(id);
        Integer status = (Integer) body.get("status");

        if(status != 200){
            return new ResponseEntity<>(body, HttpStatus.valueOf(status));
        }

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @GetMapping("filters")
    public ResponseEntity<Map<String, Object>> getAllFavoriteFilters(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize){

        Map<String, Object> body = userFavoriteService.getAllFavoriteFilters(pageNo, pageSize);
        Integer status = (Integer) body.get("status");

        if(status != 200)
        {
            return new ResponseEntity<>(body, HttpStatus.valueOf(status));
        }

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @PostMapping("filters/add")
    public ResponseEntity<Map<String, Object>> addFavoriteFilterToUser(@RequestBody FilterDto filter){

        Map<String, Object> body = userFavoriteService.addFavoriteFilterToUser(filter);
        Integer status = (Integer) body.get("status");

        if(status != 200)
        {
            return new ResponseEntity<>(body, HttpStatus.valueOf(status));
        }

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @DeleteMapping("filters/remove/{id}")
    public ResponseEntity<Map<String, Object>> removeFavoriteFilterFromUser(@PathVariable UUID id){
        Map<String, Object> body = userFavoriteService.removeFavoriteFilterFromUser(id);
        Integer status = (Integer) body.get("status");

        if(status != 200)
        {
            return new ResponseEntity<>(body, HttpStatus.valueOf(status));
        }

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @PatchMapping("filters/change/name/{id}")
    public ResponseEntity<Map<String, Object>> changeFavoriteFilterName(@RequestBody Map<String, Object> request, @PathVariable UUID id){
        String name = (String) request.get("name");
        Map<String, Object> body = userFavoriteService.changeFavoriteFilterName(id, name);
        Integer status = (Integer) body.get("status");

        if(status != 200)
        {
            return new ResponseEntity<>(body, HttpStatus.valueOf(status));
        }

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @PatchMapping("filters/change/notify/{id}")
    public ResponseEntity<Map<String, Object>> changeFavoriteFilterNotify(@PathVariable UUID id){
        Map<String, Object> body = userFavoriteService.changeFavoriteFilterNotify(id);
        Integer status = (Integer) body.get("status");

        if(status != 200)
        {
            return new ResponseEntity<>(body, HttpStatus.valueOf(status));
        }

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

}
