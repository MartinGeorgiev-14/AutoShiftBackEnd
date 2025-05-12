package com.cars.carSaleWebsite.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class JWTGenerator {

    public String generateToken(Authentication authentication, UUID userId){
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + SecurityConstant.JWT_EXPIRATION);
        Key key = Keys.hmacShaKeyFor(SecurityConstant.JWT_SECRET.getBytes(StandardCharsets.UTF_8));

        String token = Jwts.builder()
                .subject(username)
                .claim("userId", userId)
                .issuedAt(new Date())
                .expiration(expireDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();


        return token;
    }

    public String getUsernameFromJWT(String token){
        SecretKey key = Keys.hmacShaKeyFor(SecurityConstant.JWT_SECRET.getBytes(StandardCharsets.UTF_8));
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    public boolean validateToken(String token){
        SecretKey key = Keys.hmacShaKeyFor(SecurityConstant.JWT_SECRET.getBytes(StandardCharsets.UTF_8));
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        }catch (Exception ex){
           return false;
        }
    }

    public String getUserIdFromJWT(String token){
        SecretKey key = Keys.hmacShaKeyFor(SecurityConstant.JWT_SECRET.getBytes(StandardCharsets.UTF_8));
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.get("userId", String.class);
    }

    public Authentication getAuthentication(String token){
        String username = getUsernameFromJWT(token);
        return new UsernamePasswordAuthenticationToken(username, "", List.of());
    }

}
