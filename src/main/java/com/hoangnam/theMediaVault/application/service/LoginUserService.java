package com.hoangnam.theMediaVault.application.service;

import com.hoangnam.theMediaVault.application.port.in.LoginUserUseCase;
import com.hoangnam.theMediaVault.application.port.out.AuthTokenPort;
import com.hoangnam.theMediaVault.application.port.out.CheckTokenValidPort;
import com.hoangnam.theMediaVault.application.port.out.LoadUserPort;
import com.hoangnam.theMediaVault.application.port.out.PasswordEncoderPort;
import com.hoangnam.theMediaVault.application.port.out.SaveUserPort;
import com.hoangnam.theMediaVault.domain.exception.DomainException;
import com.hoangnam.theMediaVault.domain.model.User;
import com.hoangnam.theMediaVault.domain.model.UserName;
import com.hoangnam.theMediaVault.infrastructure.adapter.in.dto.AuthenticationResponse;
import com.hoangnam.theMediaVault.infrastructure.adapter.in.dto.LoginRequest;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class LoginUserService implements LoginUserUseCase {

    private final LoadUserPort loadUserPort;
    private final AuthTokenPort authTokenPort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final SaveUserPort saveUserPort;
    

    @Override
    @Transactional
    public AuthenticationResponse execute(LoginRequest request) {
        UserName username = UserName.create(request.getUsername());
        User user = loadUserPort.findByUsername(username.getValue())
                .orElseThrow(() -> new DomainException("Invalid username or password."));
        
        if (!passwordEncoderPort.matches(request.getPassword(), user.getPasswordHash())) {
            throw new DomainException("Invalid username or password.");
        }

        if(!user.isAvailable()) throw new DomainException("The user has been suspended or deleted.");
        
        String token = authTokenPort.generateToken(user.getUsername().getValue());
        Date expirartion = authTokenPort.getExpiration(token);
       
        User newUser = user.withLogin();
        saveUserPort.save(newUser);
        
        return AuthenticationResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .expiresIn(expirartion)
                .build();
    }

}
