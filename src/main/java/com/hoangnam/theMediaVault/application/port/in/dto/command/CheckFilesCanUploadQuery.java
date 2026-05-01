package com.hoangnam.theMediaVault.application.port.in.dto.command;

import com.hoangnam.theMediaVault.application.port.in.dto.ApplicationDTOCommand;
import com.hoangnam.theMediaVault.application.port.in.dto.objects.FilesHashAndSize;
import com.hoangnam.theMediaVault.domain.exception.DomainException;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Value;


@Value
@AllArgsConstructor
public class CheckFilesCanUploadQuery implements ApplicationDTOCommand {
    String ownerId;
    List<FilesHashAndSize> filesHashAndSize;

    @Override
    public void validate() {
        if(ownerId == null || ownerId.trim().isEmpty()) {
            throw new DomainException("Invalid ownerId.");
        }
        
        if(filesHashAndSize == null || filesHashAndSize.size() <= 0) {
            throw new DomainException("Files hash and size is require.");
        }
        
        for(FilesHashAndSize item : filesHashAndSize) {
            if(item.getFileHash() == null || item.getFileHash().trim().isEmpty() || item.getSizeBytes() <= 0L) {
                throw new DomainException("Invalid files hash and size.");
            }
        }
    }
    
    
}
