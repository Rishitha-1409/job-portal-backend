package com.jobportal.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expiration-ms}")
    private long expirationMs;

    // ================= GENERATE TOKEN =================

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(signingKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ================= VALIDATE TOKEN =================

    public boolean isTokenValid(String token, UserDetails userDetails) {

        try {
            return extractUsername(token).equals(userDetails.getUsername())
                    && !isExpired(token);

        } catch (JwtException e) {

            System.out.println("JWT validation failed: " + e.getMessage());

            return false;
        }
    }

    // ================= EXTRACT =================

    public String extractUsername(String token) {

        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {

        return resolver.apply(extractAllClaims(token));
    }

    public long getExpirationMs() {

        return expirationMs;
    }

    // ================= PRIVATE =================

    private boolean isExpired(String token) {

        return extractClaim(token, Claims::getExpiration)
                .before(new Date());
    }

    private Claims extractAllClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(signingKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key signingKey() {

        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(secret)
        );
    }
}