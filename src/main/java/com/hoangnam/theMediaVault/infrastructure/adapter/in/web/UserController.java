package com.hoangnam.theMediaVault.infrastructure.adapter.in.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hoangnam.theMediaVault.infrastructure.adapter.in.web.dto.UserProfileResponse;
import com.hoangnam.theMediaVault.infrastructure.security.model.CustomUserDetail;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    
    @GetMapping("/profile")
    public ResponseEntity<?> profile(@AuthenticationPrincipal CustomUserDetail detail) {
        return new ResponseEntity(UserProfileResponse.fromDomain(detail.getDomainUser()), HttpStatus.OK);
    }
}
