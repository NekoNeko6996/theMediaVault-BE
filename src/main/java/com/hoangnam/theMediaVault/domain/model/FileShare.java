package com.hoangnam.theMediaVault.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class FileShare {
    private String id;
    private File file;
    private User sharedBy;
    private User sharedWith;
    private FilePermission permission;
    private String publicToken;
    private LocalDateTime expiresAt;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    
    public boolean isExpired() {
        return this.expiresAt.isBefore(LocalDateTime.now());
    }
    
    public boolean canAccess(User visitors) {
        if(this.sharedWith == null) return true;
        return visitors != null && this.sharedWith.getId().equals(visitors.getId());
    }
    
    /**
     * 
     * @param fileToShare
     * @param sharedBy
     * @param sharedWith nếu nó null thì tạo token chia sẽ cho mọi người có link.
     * @param permission
     * @param expiresAt
     * @return 
     */
    public static FileShare create(
            File fileToShare,
            User sharedBy,
            User sharedWith, 
            FilePermission permission,
            LocalDateTime expiresAt) 
    {
        return FileShare.builder()  
                .id(UUID.randomUUID().toString())
                .file(fileToShare)
                .sharedBy(sharedBy)
                .sharedWith(sharedWith)
                .permission(permission)
                .publicToken(sharedWith == null ? UUID.randomUUID().toString() : null)
                .expiresAt(expiresAt)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();
    }
}
