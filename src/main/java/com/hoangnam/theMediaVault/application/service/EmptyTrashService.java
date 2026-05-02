package com.hoangnam.theMediaVault.application.service;

import com.hoangnam.theMediaVault.application.port.in.EmptyTrashUseCase;
import com.hoangnam.theMediaVault.application.port.in.dto.command.EmptyTrashCommand;
import com.hoangnam.theMediaVault.application.port.in.dto.objects.FileIdAndReason;
import com.hoangnam.theMediaVault.application.port.in.dto.result.EmptyTrashResult;
import com.hoangnam.theMediaVault.application.port.out.FileAndUserTransactionPort;
import com.hoangnam.theMediaVault.application.port.out.FilePersistencePort;
import com.hoangnam.theMediaVault.application.port.out.StoragePort;
import com.hoangnam.theMediaVault.domain.model.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EmptyTrashService implements EmptyTrashUseCase {

    private static final Logger logger = Logger.getLogger(HardDeleteFilesService.class.getName());
    private final StoragePort storagePort;
    private final FilePersistencePort filePersistencePort;
    private final FileAndUserTransactionPort fileAndUserTransactionPort;

    @Override
    public EmptyTrashResult execute(EmptyTrashCommand command) {
        command.validate();

        List<FileIdAndReason> errors = new ArrayList<>();
        List<File> allTrashFilesToDelete = filePersistencePort.findAllTrashFilesByOwnerId(command.getOwnerId());
        
        if (allTrashFilesToDelete == null || allTrashFilesToDelete.isEmpty()) {
            return new EmptyTrashResult(errors);
        }
        
        List<String> storagePaths = allTrashFilesToDelete.stream()
                .filter(File::isFile)
                .map(File::getStoragePath)
                .collect(Collectors.toList());
        
        // điếm số lần dùng của storage path
        Map<String, Long> storagePathCounts = filePersistencePort.countByStoragePaths(storagePaths);
        
        List<File> successfullyProcessedItems = new ArrayList<>();
        long refundQuota = 0;
        for (File file : allTrashFilesToDelete) {
            if (file.isFile()) {
                try {
                    long referenceCount = storagePathCounts.getOrDefault(file.getStoragePath(), 0L);
                    
                    if(referenceCount <= 1) {
                        storagePort.delete(file.getStoragePath());
                    }
                    
                    refundQuota += file.getSizeBytes();
                    successfullyProcessedItems.add(file);
                } catch (Exception e) {
                    logger.severe(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
                    errors.add(new FileIdAndReason(file.getId(), "Failed to delete physical file: " + e.getMessage()));
                }
            }
            else {
                successfullyProcessedItems.add(file);
            }
        }
        
        if(!successfullyProcessedItems.isEmpty()) {
            fileAndUserTransactionPort.deleteFilesAndRefundQuotaStorage(command.getOwnerId(), successfullyProcessedItems, refundQuota);
        }
        
        return new EmptyTrashResult(errors);
    }

}
