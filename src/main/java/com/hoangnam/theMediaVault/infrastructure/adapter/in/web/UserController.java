package com.hoangnam.theMediaVault.infrastructure.adapter.in.web;

import com.hoangnam.theMediaVault.domain.exception.DomainException;
import com.hoangnam.theMediaVault.infrastructure.adapter.in.dto.CreateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hoangnam.theMediaVault.application.port.in.RegisterUserUseCase;
import com.hoangnam.theMediaVault.infrastructure.adapter.in.dto.AuthenticationResponse;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final RegisterUserUseCase registerUserUseCase;
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody CreateUserRequest request) {
        try {
            AuthenticationResponse registerResponse = registerUserUseCase.execute(request);
            return new ResponseEntity<>(registerResponse, HttpStatus.CREATED);
        } catch (DomainException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
