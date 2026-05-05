package com.hoangnam.theMediaVault.domain.model;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;


@Value
@Builder(toBuilder = true)
public class Share {

    String id;
    User sharedBy;
    FilePermission permission;
    String publicToken;
    LocalDateTime expiresAt;
    Set<File> files;
    Set<User> recipients;
    LocalDateTime createAt;
    LocalDateTime updateAt;
    
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }
    
    public int numberFiles() {
        return files.size();
    }
    
    public int numberRecipients() {
        return recipients.size();
    }
    
    public static Share create(User sharedBy, FilePermission permission, String publicToken, LocalDateTime expiresAt, Set<File> filesToShare, Set<User> shareToUsers) {
        return Share.builder()
                .id(UUID.randomUUID().toString())
                .sharedBy(sharedBy)
                .permission(permission)
                .publicToken(publicToken)
                .expiresAt(expiresAt)
                .files(filesToShare)
                .recipients(shareToUsers)
                .build();
    }
}
