package com.hoangnam.theMediaVault.infrastructure.adapter.in.dto;

import com.hoangnam.theMediaVault.domain.model.User;
import com.hoangnam.theMediaVault.domain.model.UserRole;
import lombok.Builder;
import lombok.Value;
import java.time.LocalDateTime;

@Value
@Builder
public class LoadUserResponse {
    String id;  // UUID
    String username;
    String email;
    
    String passwordHash;
    
    boolean isActive;
    String role;
    
    LocalDateTime emailVerifiedAt;
    
    long storageLimit; // bytes
    long usedStorage;  // bytes
            
    LocalDateTime createAt;
    LocalDateTime updateAt;
    LocalDateTime lastLoginAt;
    LocalDateTime deletedAt;

    // Static factory method để chuyển đổi từ Domain Model sang DTO
    public static LoadUserResponse fromDomain(User user) {
        return LoadUserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .passwordHash(user.getPasswordHash())
                .isActive(user.isActive())
                .role(user.getRole().name())
                .emailVerifiedAt(user.getEmailVerifiedAt())
                .storageLimit(user.getStorageLimit())
                .usedStorage(user.getUsedStorage())
                .createAt(user.getCreateAt())
                .updateAt(user.getUpdateAt())
                .lastLoginAt(user.getLastLoginAt())
                .deletedAt(user.getDeletedAt())
                .build();
    }
}