package com.cars.carSaleWebsite.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.Signature;
import java.util.Date;

@Component
public class JWTGenerator {

    public String generateToken(Authentication authentication){
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + SecurityConstant.JWT_EXPIRATION);
        Key key = Keys.hmacShaKeyFor(SecurityConstant.JWT_SECRET.getBytes(StandardCharsets.UTF_8));

        String token = Jwts.builder()
                .subject(username)
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
            throw new AuthenticationCredentialsNotFoundException("Jwt was expired or incorrect");
        }
    }
}
