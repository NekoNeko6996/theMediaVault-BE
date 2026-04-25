package com.hoangnam.theMediaVault.application.port.in.dto.command;

import com.hoangnam.theMediaVault.domain.exception.DomainException;
import java.io.InputStream;
import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UploadFilesCommand {

    String ownerId;      // ID người dùng hiện tại
    String parentId;    // ID folder đích (có thể null nếu là root)
    List<UploadItem> items;

    @Value
    @Builder
    public static class UploadItem {
        String fileName;
        String contentType;
        String extension;
        long size;
        InputStream inputStream;
    }

    public void validate() {
        if (this.ownerId == null || this.ownerId.trim().isEmpty()) {
            throw new DomainException("Owner id must not be null or empty");
        }
        if (this.items == null || this.items.isEmpty()) {
            throw new DomainException("Upload items cannot be empty");
        }
        for (UploadItem item : this.items) {
            if (item.fileName == null || item.fileName.trim().isEmpty()) {
                throw new DomainException("File name is missing");
            }
            if(item.extension == null || item.extension.trim().isEmpty()) {
                throw  new DomainException("File extension is missing");
            }
            if (item.size <= 0) {
                throw new DomainException("File size must be greater than 0");
            }
            if (item.inputStream == null) {
                throw new DomainException("File stream is missing");
            }
        }
    }
}
