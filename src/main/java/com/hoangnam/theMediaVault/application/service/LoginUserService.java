package com.hoangnam.theMediaVault.application.service;

import com.hoangnam.theMediaVault.application.port.in.LoginUserUseCase;
import com.hoangnam.theMediaVault.application.port.out.CheckTokenValidPort;
import com.hoangnam.theMediaVault.application.port.out.GetTokenSubjectPort;
import com.hoangnam.theMediaVault.application.port.out.LoadUserPort;
import com.hoangnam.theMediaVault.infrastructure.adapter.in.dto.AuthenticationResponse;
import com.hoangnam.theMediaVault.infrastructure.adapter.in.dto.LoginRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoginUserService implements LoginUserUseCase {

    private final LoadUserPort loadUserPort;
    private final CheckTokenValidPort checkTokenValidPort;
    private final GetTokenSubjectPort getTokenSubjectPort;
    
    
    @Override
    public AuthenticationResponse execute(LoginRequest request) {
        // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
