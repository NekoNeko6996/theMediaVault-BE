package com.hoangnam.theMediaVault.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class User {

    String id;  // UUID
    UserName username;
    Email email;
    
    String passwordHash;
    
    boolean isActive;
    UserRole role;
    
    LocalDateTime emailVerifiedAt;
    
    long storageLimit; // bytes
    long usedStorage;  // bytes
            
    LocalDateTime createAt;
    LocalDateTime updateAt;
    LocalDateTime lastLoginAt;
    LocalDateTime deletedAt;
    
    // --------------------------- //
    public boolean isAdmin() {
        return this.role == UserRole.ADMIN;
    }
    
    public boolean isAvailable() {
        return this.isActive && this.deletedAt == null;
    }
    
    /**
     * 
     * @return user_{id}/
     */
    public String getRootDir() {
        return "user_" + this.id + "/";
    }
    
    /**
     * 
     * @param fileSize in bytes
     * @return true if user can upload this file else return false
     */
    public boolean canUpload(long fileSize) {
        return this.storageLimit > (fileSize + this.usedStorage);
    }
    
    
    public static User create(String username, String email, String passwordHash, long defaultStorageLimit) {
        return User.builder()
                .id(UUID.randomUUID().toString())
                .username(UserName.create(username))
                .email(Email.create(email))
                .passwordHash(passwordHash)
                .role(UserRole.USER) // Mặc định là USER
                .isActive(false)     // Chờ xác thực email
                .storageLimit(defaultStorageLimit)
                .usedStorage(0L)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();
    }
    
    public User withLogin() {
        return this.toBuilder().lastLoginAt(LocalDateTime.now()).build();
    }
    
    /**
     * 
     * @param sizeChange in bytes
     * @return updated User
     */
    public User updateStorageUsage(long sizeChange) {
        long newUsage = this.usedStorage + sizeChange;
        if(newUsage < 0) newUsage = 0;
        return this.toBuilder().usedStorage(newUsage).build();
    }
    
    public User verifyEmail() {
        return this.toBuilder().emailVerifiedAt(LocalDateTime.now()).isActive(true).build();
    }
    
    public User softDelete() {
        return this.toBuilder().deletedAt(LocalDateTime.now()).isActive(false).build();
    }
    
    public boolean isEmailVerified() {
        return this.emailVerifiedAt != null;
    }
}
