
package com.klu.tourist;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey key;

    public JwtUtil(
            @Value("${app.jwt.secret}") String secret) {
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // Generate a token for lab testing
    public String generateToken() {
        return Jwts.builder()
                .subject("lab-user")
                .issuedAt(new Date())
                .expiration(new Date(
                        System.currentTimeMillis()
                                + 60 * 60 * 1000))
                .signWith(key)
                .compact();
    }

    // Validate token signature and expiration
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
