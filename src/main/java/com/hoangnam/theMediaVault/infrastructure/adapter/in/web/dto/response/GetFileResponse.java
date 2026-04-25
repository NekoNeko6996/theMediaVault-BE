package com.hoangnam.theMediaVault.infrastructure.adapter.in.web.dto.response;

import com.hoangnam.theMediaVault.domain.model.File;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Value;


@Value
@Builder
public class GetFileResponse {
    String id;
    String parentId;
    String name;
    String itemType;
    String mimeType;
    String extension;
    Long sizeBytes;
    String storagePath;
    String fileHash;
    boolean isStarred;
    LocalDateTime createAt;
    LocalDateTime updateAt;
    
    public static GetFileResponse fromDomain(File file) {
        return GetFileResponse.builder()
                .id(file.getId())
                .parentId(file.getParent() != null? file.getParent().getId() : null)
                .name(file.getName())
                .itemType(file.getItemType().toString())
                .mimeType(file.getMimeType())
                .extension(file.getExtension())
                .sizeBytes(file.getSizeBytes())
                .storagePath(file.getStoragePath())
                .fileHash(file.getFileHash())
                .isStarred(file.isStarred())
                .createAt(file.getCreateAt())
                .updateAt(file.getUpdateAt())
                .build();
    }
}