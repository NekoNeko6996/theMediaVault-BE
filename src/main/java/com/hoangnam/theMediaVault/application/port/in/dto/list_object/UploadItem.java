package com.hoangnam.theMediaVault.application.port.in.dto.list_object;

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
}
