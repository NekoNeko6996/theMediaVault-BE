package com.hoangnam.theMediaVault.application.service;

import com.hoangnam.theMediaVault.application.port.in.CheckFilesExistsUseCase;
import com.hoangnam.theMediaVault.application.port.in.dto.command.CheckFilesExistsQuery;
import com.hoangnam.theMediaVault.application.port.in.dto.list_object.FilesHashAndSize;
import com.hoangnam.theMediaVault.application.port.in.dto.result.CheckFilesExistsResult;
import com.hoangnam.theMediaVault.application.port.out.FilePersistencePort;
import com.hoangnam.theMediaVault.domain.model.File;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CheckFilesExistsService implements CheckFilesExistsUseCase {

    private final FilePersistencePort filePersistencePort;

    @Override
    public CheckFilesExistsResult execute(CheckFilesExistsQuery query) {
        query.validate();

        List<File> exists = filePersistencePort.findExistingFiles(query.getOwnerId(), query.getHashes());

        List<FilesHashAndSize> result = new ArrayList<>();
        for (File file : exists) {
            result.add(new FilesHashAndSize(file.getFileHash(), file.getSizeBytes()));
        }

        return new CheckFilesExistsResult(result);
    }

}
