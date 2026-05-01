package com.hoangnam.theMediaVault.infrastructure.adapter.in.web.dto.response;

import com.hoangnam.theMediaVault.application.port.in.dto.objects.FileHashAndReason;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Value;


@Value
@AllArgsConstructor
public class CheckFilesCanUploadResponse {
    List<String> existingFilesHash;
    List<RequireUploads> canUploads;
    List<FileHashAndReason> refuseUploads;
    
    @Value
    @AllArgsConstructor
    public static class RequireUploads {
        String hash;
        String uploadToken;
    }
}
