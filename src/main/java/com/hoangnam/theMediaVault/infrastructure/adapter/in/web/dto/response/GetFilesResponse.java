package com.hoangnam.theMediaVault.infrastructure.adapter.in.web.dto.response;

import com.hoangnam.theMediaVault.domain.model.File;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Value;


@Value
@AllArgsConstructor
public class GetFilesResponse {
    List<GetFileResponse> files;
    
    public static List<GetFileResponse> fromDomain(List<File> files) {
        return files.stream().map(GetFileResponse::fromDomain).collect(Collectors.toList());
    }
}
