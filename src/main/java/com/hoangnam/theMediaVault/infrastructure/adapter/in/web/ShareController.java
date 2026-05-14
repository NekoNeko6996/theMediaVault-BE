package com.hoangnam.theMediaVault.infrastructure.adapter.in.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/share")
public class ShareController {
    
    
    @PostMapping
    public ResponseEntity<?> share() {
        return ResponseEntity.ok(null);
    }
}
