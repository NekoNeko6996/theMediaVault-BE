package com.hoangnam.theMediaVault.application.service;

import com.hoangnam.theMediaVault.application.port.out.CheckUserPort;
import com.hoangnam.theMediaVault.application.port.out.SaveUserPort;
import com.hoangnam.theMediaVault.domain.exception.DomainException;
import com.hoangnam.theMediaVault.domain.model.User;
import com.hoangnam.theMediaVault.infrastructure.adapter.in.dto.CreateUserRequest;
import lombok.RequiredArgsConstructor;
import com.hoangnam.theMediaVault.application.port.in.RegisterUserUseCase;
import com.hoangnam.theMediaVault.infrastructure.adapter.in.dto.AuthenticationResponse;
import org.springframework.transaction.annotation.Transactional;
import com.hoangnam.theMediaVault.application.port.out.AuthTokenPort;
import com.hoangnam.theMediaVault.application.port.out.PasswordEncoderPort;
import java.util.Date;

@RequiredArgsConstructor
public class RegisterUserService implements RegisterUserUseCase {
    private final SaveUserPort saveUserPort;
    private final CheckUserPort checkUserPort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final AuthTokenPort authTokenPort;
    
    private final long STORAGE_LIMIT = 5L * 1024 * 1024 * 1024; // 5GB

    @Override
    @Transactional
    public AuthenticationResponse execute(CreateUserRequest request) {
        // check nghiệp vụ
        if(checkUserPort.existsByEmail(request.getEmail()))
            throw new DomainException("The email already exists.");
        if(checkUserPort.existsByUserName(request.getUsername()))
            throw new DomainException("The username already exists.");

        String hash = passwordEncoderPort.encode(request.getPassword());
        User user = User.create(request.getUsername(), request.getEmail(), hash, STORAGE_LIMIT);
        saveUserPort.save(user);
        
        // generate token
        String token = authTokenPort.generateToken(request.getUsername());
        Date expirationDate = authTokenPort.getExpiration(token);
        
        return AuthenticationResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .expiresIn(expirationDate)
                .build();
    }
    
}
