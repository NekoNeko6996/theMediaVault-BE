package com.hoangnam.theMediaVault.infrastructure.adapter.in.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class AuthenticationResponse {
    String accessToken;
    String tokenType; // Thường là "Bearer"
    Date expiresIn;
}
