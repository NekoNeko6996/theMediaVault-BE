package com.hoangnam.theMediaVault.infrastructure.service;

import com.hoangnam.theMediaVault.infrastructure.security.model.CustomUserDetail;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JWTService {

    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(String userID, long expiration) {
        Map<String, Object> claims = new HashMap();
        return createToken(claims, userID, expiration);
    }

    private String createToken(Map<String, Object> claims, String userID, long expiration) {
        return Jwts.builder()
                .claims(claims)
                .subject(userID)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignKey())
                .compact();
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Boolean isTokenExpiredOrInvalid(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, CustomUserDetail user) {
        final String userID = extractSubject(token);
        return (userID.equals(user.getDomainUser().getId()) && !isTokenExpiredOrInvalid(token));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
