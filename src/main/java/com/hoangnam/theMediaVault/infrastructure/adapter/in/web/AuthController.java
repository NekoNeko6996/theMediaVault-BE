package com.hoangnam.theMediaVault.infrastructure.adapter.in.web;

import com.hoangnam.theMediaVault.application.port.in.LoginUserUseCase;
import com.hoangnam.theMediaVault.application.port.in.RegisterUserUseCase;
import com.hoangnam.theMediaVault.application.port.in.dto.result.AuthenticatedIdentity;
import com.hoangnam.theMediaVault.application.port.in.dto.command.LoginCommand;
import com.hoangnam.theMediaVault.application.port.in.dto.command.RegisterCommand;
import com.hoangnam.theMediaVault.infrastructure.adapter.in.web.dto.response.AuthenticatedResponse;
import com.hoangnam.theMediaVault.infrastructure.service.JWTService;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUserUseCase loginUserUseCase;
    
    private final JWTService jwtService;
    
    @Value("${jwt.token.expiration}")
    private long expirationTime;
    
    private final String TOKEN_TYPE = "Bearer";
    
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterCommand request) {
        AuthenticatedIdentity identity = registerUserUseCase.execute(request);
        
        // tạo token
        String token = jwtService.generateToken(identity.getUser().getId(), expirationTime);
        Date expiredIn = jwtService.extractExpiration(token);
        
        return new ResponseEntity(new AuthenticatedResponse(TOKEN_TYPE, token, expiredIn), HttpStatus.CREATED);
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginCommand request) {
        AuthenticatedIdentity identity = loginUserUseCase.execute(request);
        
        // tạo token
        String token = jwtService.generateToken(identity.getUser().getId(), expirationTime);
        Date expiredIn = jwtService.extractExpiration(token);
        
        return new ResponseEntity(new AuthenticatedResponse(TOKEN_TYPE, token, expiredIn), HttpStatus.CREATED);
    }
}
