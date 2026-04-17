package com.hoangnam.theMediaVault.application.port.in.dto.in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class GetUserProfileQuery {
    String id;
}
