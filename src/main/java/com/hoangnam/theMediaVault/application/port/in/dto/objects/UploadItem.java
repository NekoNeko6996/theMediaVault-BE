package com.hoangnam.theMediaVault.application.port.in.dto.objects;

import java.io.InputStream;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UploadItem {
    String fileName;
    String contentType;
    String extension;
    long size;
    InputStream inputStream;
    String approvedHash;
    long approvedSize;
}
