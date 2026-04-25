package com.hoangnam.theMediaVault.application.port.in;

import com.hoangnam.theMediaVault.application.port.in.dto.result.AuthenticatedIdentity;
import com.hoangnam.theMediaVault.application.port.in.dto.command.LoginCommand;

/**
 *
 * 
 */
public interface LoginUserUseCase {
    AuthenticatedIdentity execute(LoginCommand comand);
}
