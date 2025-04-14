package com.cars.carSaleWebsite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.util.Map;

@SpringBootApplication
public class CarSaleWebsiteApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(CarSaleWebsiteApplication.class, args);
		Dotenv dotenv = Dotenv.load();
		Cloudinary cloudinary = new Cloudinary(dotenv.get("CLOUDINARY_URL"));
	}

}
