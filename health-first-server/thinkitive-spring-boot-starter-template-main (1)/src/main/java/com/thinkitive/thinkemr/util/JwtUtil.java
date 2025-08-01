package com.thinkitive.thinkemr.util;

import com.thinkitive.thinkemr.entity.Provider;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    private final SecretKey jwtSecret;
    private final int jwtExpirationSeconds;
    private final int patientExpirationSeconds = 1800; // 30 minutes

    public JwtUtil(
            @Value("${jwt.secret:my-super-secret-key-which-should-be-long}") String secret,
            @Value("${jwt.expiration:3600}") int expirationSeconds
    ) {
        this.jwtSecret = Keys.hmacShaKeyFor(secret.getBytes());
        this.jwtExpirationSeconds = expirationSeconds;
    }

    public String generateToken(Provider provider) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("provider_id", provider.getId().toString());
        claims.put("email", provider.getEmail());
        claims.put("role", "PROVIDER");
        claims.put("specialization", provider.getSpecialization().name());
        claims.put("verification_status", provider.getVerificationStatus().name());
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtExpirationSeconds * 1000L);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(provider.getEmail())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(jwtSecret)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(jwtSecret).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(jwtSecret).build().parseClaimsJws(token).getBody();
    }

    public int getExpirationSeconds() {
        return jwtExpirationSeconds;
    }

    public String generatePatientToken(com.thinkitive.thinkemr.entity.Patient patient) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("patient_id", patient.getId().toString());
        claims.put("email", patient.getEmail());
        claims.put("role", "PATIENT");
        Date now = new Date();
        Date expiry = new Date(now.getTime() + patientExpirationSeconds * 1000L);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(patient.getEmail())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(jwtSecret)
                .compact();
    }

    public int getPatientExpirationSeconds() {
        return patientExpirationSeconds;
    }
} 