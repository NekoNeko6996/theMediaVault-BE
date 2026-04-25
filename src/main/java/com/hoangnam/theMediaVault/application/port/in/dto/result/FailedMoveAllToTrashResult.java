package com.hoangnam.theMediaVault.application.port.in.dto.result;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class FailedMoveAllToTrashResult {
    List<MoveToTrashError> error; 

    @Value
    @AllArgsConstructor
    public static class MoveToTrashError {
        String folderId;
        String reason;
    }
}
