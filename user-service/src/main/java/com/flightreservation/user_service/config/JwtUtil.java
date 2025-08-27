package com.flightreservation.user_service.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey SECRET_KEY;
    private final long EXPIRATION;

    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.expiration}") long expiration) {
        // secret string’den HS256 için uygun SecretKey oluşturuyoruz
        this.SECRET_KEY = Keys.hmacShaKeyFor(secret.getBytes());
        this.EXPIRATION = expiration;
    }

    // Token üretme
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email) // token’in sahibi 
                .setIssuedAt(new Date()) // oluşturulma tarihi
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION)) // geçerlilik süresi
                .signWith(SECRET_KEY)
                .compact();
    }

    //Kullanıcı login olunca bu method çağrılır → token üretir.
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    // Token geçerliliğini kontrol etme
    public boolean validateToken(String token, String email) {
        return extractUsername(token).equals(email) && !isTokenExpired(token);
    }

    // Token süresi dolmuş mu kontrol etme
    private boolean isTokenExpired(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }
}
