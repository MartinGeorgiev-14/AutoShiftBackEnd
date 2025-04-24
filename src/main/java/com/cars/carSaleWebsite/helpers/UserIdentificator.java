package com.cars.carSaleWebsite.helpers;

import com.cars.carSaleWebsite.security.JWTGenerator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

@Data
@Component
public class UserIdentificator {

    private final JWTGenerator jwtGenerator;

    @Autowired
    public UserIdentificator(JWTGenerator jwtGenerator) {
        this.jwtGenerator = jwtGenerator;
    }

    public String getCurrentUserId(){
        String token = (String) getJWTFromRequest();

        if(token == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You need to login");
        }

        return jwtGenerator.getUserIdFromJWT(token);
    }

    private String getJWTFromRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String bearerToken = request.getHeader("Authorization");
            if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
                return bearerToken.substring(7);
            }
        }
        return null;
    }
}
