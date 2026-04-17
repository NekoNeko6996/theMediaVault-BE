package com.hoangnam.theMediaVault.application.port.in.dto.in;

import lombok.Value;


@Value
public class RegisterCommand {
    String username;
    String email;
    String password;
}
