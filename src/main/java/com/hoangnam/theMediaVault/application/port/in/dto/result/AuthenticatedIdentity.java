package com.hoangnam.theMediaVault.application.port.in.dto.result;

import com.hoangnam.theMediaVault.domain.model.User;
import lombok.Value;

@Value
public class AuthenticatedIdentity {
    User user;
    boolean isFirstTimeLogin;
}
