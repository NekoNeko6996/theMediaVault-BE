package com.hoangnam.theMediaVault.application.service;

import com.hoangnam.theMediaVault.application.port.in.RestoreFilesFromTrashUseCase;
import com.hoangnam.theMediaVault.application.port.in.dto.command.RestoreFilesFromTrashCommand;
import com.hoangnam.theMediaVault.application.port.out.FilePersistencePort;
import com.hoangnam.theMediaVault.domain.model.File;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class RestoreFilesFromTrashService implements RestoreFilesFromTrashUseCase {
    
    private final FilePersistencePort filePersistencePort;

    @Override
    @Transactional
    public void execute(RestoreFilesFromTrashCommand command) {
        
        List<File> explicitItems = filePersistencePort.findByOwnerAndFileIds(command.getOwnerId(), command.getFileIds());
        
        List<String> idToMoveToRoot = new ArrayList<>();
        for(File item : explicitItems) {
            if(item.getParent() != null && item.getParent().isTrashed()) {
                idToMoveToRoot.add(item.getId());
            }
        }
        
        
        // trường hợp có file nào mà parent đang bị xóa thì đưa DIR của nó ra root(bước này chỉ là 1 bước chuẩn bị chứ chưa khoi phục file) 
        if(!idToMoveToRoot.isEmpty()) {
            filePersistencePort.moveFilesToNewDir(idToMoveToRoot, null);
        }
        
        List<String> allIdsToRestore = filePersistencePort.findAllChildIds(command.getFileIds());
        if(!allIdsToRestore.isEmpty()) {
            filePersistencePort.restoreAllFromTrash(allIdsToRestore);
        }
    }

}
