package com.hoangnam.theMediaVault.application.port.in.dto.result;

import lombok.AllArgsConstructor;
import lombok.Value;


@Value
@AllArgsConstructor
public class ToggleStarredFileResult {
    String targetFileId;
    boolean isStarred;
}
