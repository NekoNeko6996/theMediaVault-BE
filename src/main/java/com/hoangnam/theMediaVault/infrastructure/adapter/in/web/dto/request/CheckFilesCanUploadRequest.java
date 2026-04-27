package com.hoangnam.theMediaVault.infrastructure.adapter.in.web.dto.request;

import com.hoangnam.theMediaVault.application.port.in.dto.list_object.FilesHashAndSize;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Value;


@Value
@AllArgsConstructor
public class CheckFilesCanUploadRequest {
    List<FilesHashAndSize> filesHashAndSize;
}
