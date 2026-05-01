package com.hoangnam.theMediaVault.application.service;

import com.hoangnam.theMediaVault.application.port.in.GetAllStarredFilesUseCase;
import com.hoangnam.theMediaVault.application.port.in.dto.command.GetAllStarredFilesQuery;
import com.hoangnam.theMediaVault.application.port.out.FilePersistencePort;
import com.hoangnam.theMediaVault.domain.model.File;
import java.util.List;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class GetAllStarredFilesService implements GetAllStarredFilesUseCase {

    private final FilePersistencePort filePersistencePort;
    
    @Override
    public List<File> execute(GetAllStarredFilesQuery query) {
        query.validate();
        return filePersistencePort.findAllStarredFiles(query.getOwnerId());
    }

}
