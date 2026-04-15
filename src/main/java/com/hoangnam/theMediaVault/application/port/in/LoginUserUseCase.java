package com.hoangnam.theMediaVault.application.port.in;

import com.hoangnam.theMediaVault.infrastructure.adapter.in.dto.AuthenticationResponse;
import com.hoangnam.theMediaVault.infrastructure.adapter.in.dto.LoginRequest;

/**
 *
 * 
 */
public interface LoginUserUseCase {
    AuthenticationResponse execute(LoginRequest request);
}
