package com.hoangnam.theMediaVault.infrastructure.adapter.in.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class LoginRequest {
    String username;
    String password;
}
