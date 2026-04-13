package com.hoangnam.theMediaVault.infrastructure.adapter.out.persistence.entity;

import com.hoangnam.theMediaVault.domain.model.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    private String id;  // UUID
    
    @Column(nullable = false, unique = true, length = 50)
    String username;
    
    @Column(nullable = false, unique = true)
    String email;
    
    @Column(name = "password_hash", nullable = false)
    String passwordHash;
    
    @Column(name = "is_active", nullable = false)
    boolean isActive;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    UserRole role;
    
    @Column(name = "email_verified_at")
    LocalDateTime emailVerifiedAt;
    
    @Column(name = "storage_limit", nullable = false)
    long storageLimit; // bytes
    @Column(name = "used_storage", nullable = false)
    long usedStorage;  // bytes
            
    @Column(name = "create_at", nullable = false, updatable = false)
    LocalDateTime createAt;
    @Column(name = "update_at")
    LocalDateTime updateAt;
    @Column(name = "last_login_at")
    LocalDateTime lastLoginAt;
    @Column(name = "deleted_at")
    LocalDateTime deletedAt;
}
