package com.hoangnam.theMediaVault.infrastructure.adapter.in.web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Value;


@Value
@AllArgsConstructor
public class RenameFileRequest {
    String fileId;
    String newName;
}
