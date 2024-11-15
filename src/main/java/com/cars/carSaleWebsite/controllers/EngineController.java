package com.cars.carSaleWebsite.controllers;

import com.cars.carSaleWebsite.dto.CarDto;
import com.cars.carSaleWebsite.dto.EngineDto;
import com.cars.carSaleWebsite.service.EngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.UUID;

@RestController
@RequestMapping("/api/")
public class EngineController {

    private EngineService engineService;

    @Autowired
    public EngineController(EngineService engineService) {
        this.engineService = engineService;
    }

    @GetMapping("/engine/{id}")
    public ResponseEntity<EngineDto> getEngineById(@PathVariable UUID id){
        EngineDto engine = engineService.gerEngineById(id);
        return new ResponseEntity<>(engine, HttpStatus.OK);
    }

    @GetMapping("/carEngine/{id}")
    public ResponseEntity<HashSet<CarDto>> getAllCarsWithIdEngine(@PathVariable UUID id){
        HashSet<CarDto> cars = engineService.getAllCarsWithTypeIdEngine(id);
        return new ResponseEntity<>(cars, HttpStatus.OK);

    }
}
