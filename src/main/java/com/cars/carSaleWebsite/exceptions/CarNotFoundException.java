package com.cars.carSaleWebsite.exceptions;

public class CarNotFoundException extends RuntimeException
{
    private static final long serialVersionUID = 1;

    public CarNotFoundException(String message){
        super(message);
    }


}
