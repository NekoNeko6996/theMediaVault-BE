package com.hoangnam.theMediaVault.application.port.in.dto.result;

import com.hoangnam.theMediaVault.application.port.in.dto.objects.UploadError;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Value;


@Value
@AllArgsConstructor
public class FailedFileUploadsResult {
    List<UploadError> errors;
}