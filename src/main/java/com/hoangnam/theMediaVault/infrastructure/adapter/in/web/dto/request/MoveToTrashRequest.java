package com.hoangnam.theMediaVault.infrastructure.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class MoveToTrashRequest {
    public String ownerId;
    public List<String> fileIds;
}
