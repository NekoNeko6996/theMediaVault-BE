package com.hoangnam.theMediaVault.application.port.in.dto.result;

import com.hoangnam.theMediaVault.application.port.in.dto.list_object.FileHashAndReason;
import com.hoangnam.theMediaVault.application.port.in.dto.list_object.FilesHashAndSize;
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
