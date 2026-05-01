package com.hoangnam.theMediaVault.application.port.in.dto.objects;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class FileIdAndReason {
    String fileId;
    String reason;
}
