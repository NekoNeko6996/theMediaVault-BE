package com.hoangnam.theMediaVault.application.port.in.dto.out;

import com.hoangnam.theMediaVault.domain.model.User;
import lombok.Value;

@Value
public class UserProfileResult {
    User user;
}
