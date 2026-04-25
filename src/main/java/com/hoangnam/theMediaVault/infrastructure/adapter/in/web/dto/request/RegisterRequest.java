package com.hoangnam.theMediaVault.infrastructure.adapter.in.web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Value;


@Value
@AllArgsConstructor
public class RegisterRequest {
    String username;
    String email;
    String password;
}
