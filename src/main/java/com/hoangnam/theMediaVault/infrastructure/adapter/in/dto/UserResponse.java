package com.hoangnam.theMediaVault.infrastructure.adapter.in.dto;

import com.hoangnam.theMediaVault.domain.model.User;
import lombok.Builder;
import lombok.Value;
import java.time.LocalDateTime;

@Value
@Builder
public class UserResponse {
    String id;
    String username;
    String email;
    String role;
    long storageLimit;
    long usedStorage;
    boolean isActive;
    LocalDateTime createAt;

    // Static factory method để chuyển đổi từ Domain Model sang DTO
    public static UserResponse fromDomain(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername().getValue())
                .email(user.getEmail().getValue())
                .role(user.getRole().name())
                .storageLimit(user.getStorageLimit())
                .usedStorage(user.getUsedStorage())
                .isActive(user.isActive())
                .createAt(user.getCreateAt())
                .build();
    }
}