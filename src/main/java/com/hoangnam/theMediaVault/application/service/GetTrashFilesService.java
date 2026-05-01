package com.hoangnam.theMediaVault.application.service;

import com.hoangnam.theMediaVault.application.port.in.GetTrashFilesUseCase;
import com.hoangnam.theMediaVault.application.port.in.dto.command.GetTrashFilesQuery;
import com.hoangnam.theMediaVault.application.port.out.FilePersistencePort;
import com.hoangnam.theMediaVault.domain.model.File;
import java.util.List;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class GetTrashFilesService implements GetTrashFilesUseCase {

    private final FilePersistencePort filePersistencePort;
    
    @Override
    public List<File> execute(GetTrashFilesQuery query) {
        query.validate();
        return filePersistencePort.findAllTrashFilesByOwnerId(query.getOwnerId());
    }

}
