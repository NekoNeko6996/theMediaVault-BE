package com.hoangnam.theMediaVault.application.port.in.dto.result;

import com.hoangnam.theMediaVault.application.port.in.dto.objects.FileHashAndReason;
import com.hoangnam.theMediaVault.application.port.in.dto.objects.FilesHashAndSize;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class CheckFilesCanUploadResult {
     
    List<String> existingFilesHash;
    List<FilesHashAndSize> canUploads;
    List<FileHashAndReason> refuseFilesHash;
}
