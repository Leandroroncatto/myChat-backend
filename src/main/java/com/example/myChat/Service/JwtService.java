package com.example.myChat.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.github.cdimascio.dotenv.Dotenv;

import javax.crypto.SecretKey;

import java.nio.charset.StandardCharsets;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    private final Dotenv dotenv = Dotenv.load();
    private final String dotEnvKey = dotenv.get("JWT_SECRET");
    private final SecretKey secretkey = Keys.hmacShaKeyFor(dotEnvKey.getBytes(StandardCharsets.UTF_8));

    public String generateToken(UUID id) {
        Date createdAt = new Date();
        Date expiration = new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000);

        return Jwts.builder()
                .subject(id.toString())
                .issuedAt(createdAt)
                .expiration(expiration)
                .signWith(this.secretkey, Jwts.SIG.HS256)
                .compact();
    }

    public UUID extractUserId(String token) {
        String subject = Jwts.parser()
                .verifyWith(secretkey)
                .build().parseSignedClaims(token)
                .getPayload()
                .getSubject();
        return UUID.fromString(subject);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(secretkey).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            System.out.println("ERRO: TOKEN INVALIDO!!!!!! ERERERERE");
            return false;
        }
    }

    public Instant extractExpirationInstant(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretkey)
                .build().parseSignedClaims(token)
                .getPayload();

        long expiration = claims.getExpiration().getTime();

        return Instant.ofEpochMilli(expiration);
    }
}
