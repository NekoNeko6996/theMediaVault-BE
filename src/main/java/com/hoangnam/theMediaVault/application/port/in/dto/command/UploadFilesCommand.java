package com.hoangnam.theMediaVault.application.port.in.dto.command;

import com.hoangnam.theMediaVault.application.port.in.dto.objects.UploadItem;
import com.hoangnam.theMediaVault.domain.exception.DomainException;
import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UploadFilesCommand {

    String ownerId;      // ID người dùng hiện tại
    String parentId;    // ID folder đích (có thể null nếu là root)
    List<UploadItem> items;

    public void validate() {
        if (this.ownerId == null || this.ownerId.trim().isEmpty()) {
            throw new DomainException("Owner id must not be null or empty");
        }
        if (this.items == null || this.items.isEmpty()) {
            throw new DomainException("Upload items cannot be empty");
        }
        for (UploadItem item : this.items) {
            if (item.getFileName() == null || item.getFileName().trim().isEmpty()) {
                throw new DomainException("File name is missing");
            }
            if(item.getExtension() == null || item.getExtension().trim().isEmpty()) {
                throw  new DomainException("File extension is missing");
            }
            if (item.getSize() <= 0) {
                throw new DomainException("File size must be greater than 0");
            }
            if (item.getInputStream() == null) {
                throw new DomainException("File stream is missing");
            }
            
            if(item.getApprovedHash() == null || item.getApprovedHash().trim().isEmpty()) {
                throw new DomainException("Approved hash is missing.");
            }
            
            if(item.getApprovedSize() <= 0) {
                throw new DomainException("Approved size is missing.");
            }
        }
    }
}
