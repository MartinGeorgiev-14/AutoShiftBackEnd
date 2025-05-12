package com.cars.carSaleWebsite.config.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        if(authException instanceof UsernameNotFoundException){
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
        } else{
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed");
        }
    }
}
