package com.hoangnam.theMediaVault.application.port.in.dto.out;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Value;


@Value
@AllArgsConstructor
public class FailedFileUploadsResult {
    List<UploadError> errors;

    @Value
    @AllArgsConstructor
    public static class UploadError {
        String fileName;
        String reason; // Ví dụ: "Storage limit exceeded"
    }
}