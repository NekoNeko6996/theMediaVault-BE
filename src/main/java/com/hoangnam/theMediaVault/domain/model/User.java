package com.hoangnam.theMediaVault.domain.model;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class User {

    String id;  // UUID
    String username;
    String email;
    
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
     * @param fileSize in bytes
     * @return true if user can upload this file else return false
     */
    public boolean canUpload(long fileSize) {
        return this.storageLimit > (fileSize + this.usedStorage);
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
