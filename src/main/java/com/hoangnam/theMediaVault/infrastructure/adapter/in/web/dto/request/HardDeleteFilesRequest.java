package com.hoangnam.theMediaVault.infrastructure.adapter.in.web.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Value;


@Value
@AllArgsConstructor
public class HardDeleteFilesRequest {
    List<String> fileIdsToDelete;
}
