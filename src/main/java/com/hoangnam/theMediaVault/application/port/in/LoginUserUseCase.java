package com.hoangnam.theMediaVault.application.port.in;

import com.hoangnam.theMediaVault.application.port.in.dto.out.AuthenticatedIdentity;
import com.hoangnam.theMediaVault.application.port.in.dto.in.LoginCommand;

/**
 *
 * 
 */
public interface LoginUserUseCase {
    AuthenticatedIdentity execute(LoginCommand comand);
}
