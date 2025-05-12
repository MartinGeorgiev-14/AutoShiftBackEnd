package com.cars.carSaleWebsite.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JWTGenerator tokenGenerator;
    @Autowired
    private CustomUserDetailService customUserDetailService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = getJWTFromRequest(request);

//        List<String> publicEndpoints = List.of("/api/auth/**", "/api/app/**");
//        String requestPath = request.getRequestURI();
//
//        if (publicEndpoints.stream().anyMatch(requestPath::startsWith)) {
//            // Skip JWT validation for public endpoints
//            filterChain.doFilter(request, response);
//            return;
//        }

        if(StringUtils.hasText(token)){

            if(!tokenGenerator.validateToken(token)){
                handleAuthenticationError(response, "Invalid or expired token.");
                return;
            }

            String username = tokenGenerator.getUsernameFromJWT(token);

            UserDetails userDetails = customUserDetailService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,
                    null, userDetails.getAuthorities());

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request, response);
    }

    private String getJWTFromRequest(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

    private void handleAuthenticationError(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        String jsonResponse = String.format("{\"error\": \"%s\", \"code\": %d}", message, HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(jsonResponse);
    }
}
