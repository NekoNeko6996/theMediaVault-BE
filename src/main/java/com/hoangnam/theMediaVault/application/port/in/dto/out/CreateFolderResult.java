package com.hoangnam.theMediaVault.application.port.in.dto.out;

import java.time.LocalDateTime;
import lombok.Value;


@Value
public class CreateFolderResult {
    String id;
    String name;
    String parentId;
    LocalDateTime createdAt;
}