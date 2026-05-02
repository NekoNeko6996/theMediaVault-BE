package com.hoangnam.theMediaVault.application.service;

import com.hoangnam.theMediaVault.application.port.in.GetFileInfoUseCase;
import com.hoangnam.theMediaVault.application.port.in.dto.command.GetFileInfoQuery;
import com.hoangnam.theMediaVault.application.port.out.FilePersistencePort;
import com.hoangnam.theMediaVault.domain.exception.DomainException;
import com.hoangnam.theMediaVault.domain.model.File;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class GetFileInfoService implements GetFileInfoUseCase {
    
    private final FilePersistencePort filePersistencePort;

    @Override
    public File execute(GetFileInfoQuery query) {
        query.validate();
        return filePersistencePort.findByIdAndOwnerId(query.getFileId(), query.getOwnerId())
                .orElseThrow(() -> new DomainException("File not found or your don't have permission to see this."));
    }

}
