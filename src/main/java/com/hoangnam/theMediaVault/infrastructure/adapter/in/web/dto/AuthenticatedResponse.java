package com.hoangnam.theMediaVault.infrastructure.adapter.in.web.dto;

import java.util.Date;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@Builder
@RequiredArgsConstructor
public class AuthenticatedResponse {
    String type;
    String token;
    Date expiredIn;
}
