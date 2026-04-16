package com.hoangnam.theMediaVault.infrastructure.adapter.in.web;

import com.hoangnam.theMediaVault.application.port.in.LoginUserUseCase;
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
import com.hoangnam.theMediaVault.infrastructure.adapter.in.dto.LoginRequest;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUserUseCase loginUserUseCase;
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody CreateUserRequest request) {
        AuthenticationResponse registerResponse = registerUserUseCase.execute(request);
        return new ResponseEntity<>(registerResponse, HttpStatus.CREATED);
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        AuthenticationResponse loginResponse = loginUserUseCase.execute(request);
        return new ResponseEntity<>(loginResponse, HttpStatus.ACCEPTED);
    }
}
