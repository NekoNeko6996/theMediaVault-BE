package com.hoangnam.theMediaVault.infrastructure.adapter.in.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;


@Value
public class CreateUserRequest {
    @NotBlank @Size(min = 5, max = 50)
    String username;

    @NotBlank @Email
    String email;

    @NotBlank @Size(min = 8)
    String password;
}
