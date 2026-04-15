package com.hoangnam.theMediaVault.infrastructure.adapter.out.persistence;

import com.hoangnam.theMediaVault.application.port.out.GetTokenExpirationPort;
import com.hoangnam.theMediaVault.application.port.out.TokenGeneratorPort;
import com.hoangnam.theMediaVault.infrastructure.service.JWTService;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenPrersistenceAdapter implements TokenGeneratorPort, GetTokenExpirationPort {
    
    private final JWTService jwtService;
    
    @Value("${jwt.token.expiration}")
    private long expiration;
    
    @Override
    public String generateToken(String subject) {
        return jwtService.generateToken(subject, expiration);
    }

    @Override
    public long milisecond() {
        return expiration;
    }

}
