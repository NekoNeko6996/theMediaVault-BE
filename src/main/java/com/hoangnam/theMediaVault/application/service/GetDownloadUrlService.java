package com.hoangnam.theMediaVault.application.service;

import com.hoangnam.theMediaVault.application.port.in.dto.command.GetDowloadUrlQuery;
import com.hoangnam.theMediaVault.application.port.out.FilePersistencePort;
import com.hoangnam.theMediaVault.application.port.out.StoragePort;
import com.hoangnam.theMediaVault.domain.exception.DomainException;
import com.hoangnam.theMediaVault.domain.model.File;
import java.util.logging.Logger;
import lombok.RequiredArgsConstructor;
import com.hoangnam.theMediaVault.application.port.in.GetDownloadUrlUseCase;
import com.hoangnam.theMediaVault.application.port.in.dto.result.GetDownLoadUrlResult;


@RequiredArgsConstructor
public class GetDownloadUrlService implements GetDownloadUrlUseCase {

    private static final Logger logger = Logger.getLogger(GetDownloadUrlService.class.getName());
    private final FilePersistencePort filePersistencePort;
    private final StoragePort storagePort;
    
    
    @Override
    public GetDownLoadUrlResult execute(GetDowloadUrlQuery query) {
        query.validate();
        
        File tagetDowload = filePersistencePort.findByIdAndOwnerId(
                query.getFileId(), 
                query.getOwnerId()
        ).orElseThrow(() -> new DomainException("File not found or your don't have any permission to dowload this file."));
        
        try {
            String url = storagePort.getDownloadUrl(tagetDowload.getStoragePath(), query.getType());
            return new GetDownLoadUrlResult(tagetDowload.getName(), tagetDowload.getExtension(), url);
        }
        catch(Exception e) {
            logger.severe(e.getCause().getMessage());
            throw new DomainException(e.getMessage());
        }
    }

}
