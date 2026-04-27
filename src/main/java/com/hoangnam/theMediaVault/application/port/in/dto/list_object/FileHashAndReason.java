package com.hoangnam.theMediaVault.application.port.in.dto.list_object;

import lombok.AllArgsConstructor;
import lombok.Value;


@Value
@AllArgsConstructor
public class FileHashAndReason {
    String fileHash;
    String reason;
}
