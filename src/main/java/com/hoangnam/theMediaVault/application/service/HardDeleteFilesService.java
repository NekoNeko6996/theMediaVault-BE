package com.hoangnam.theMediaVault.application.service;

import com.hoangnam.theMediaVault.application.port.in.HardDeleteFilesUseCase;
import com.hoangnam.theMediaVault.application.port.in.dto.command.HardDeleteFilesCommand;
import com.hoangnam.theMediaVault.application.port.in.dto.objects.FileIdAndReason;
import com.hoangnam.theMediaVault.application.port.in.dto.result.FailedHardDeleteFilesResult;
import com.hoangnam.theMediaVault.application.port.out.FileAndUserTransactionPort;
import com.hoangnam.theMediaVault.application.port.out.FilePersistencePort;
import com.hoangnam.theMediaVault.application.port.out.StoragePort;
import com.hoangnam.theMediaVault.domain.model.File;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HardDeleteFilesService implements HardDeleteFilesUseCase {

    private static final Logger logger = Logger.getLogger(HardDeleteFilesService.class.getName());
    private final StoragePort storagePort;
    private final FilePersistencePort filePersistencePort;
    private final FileAndUserTransactionPort fileAndUserTransactionPort;
    
    @Override
    @Transactional
    public FailedHardDeleteFilesResult execute(HardDeleteFilesCommand command) {
        command.validate();
        
        List<FileIdAndReason> errors = new ArrayList<>();
        
        // 1. Lấy toàn bộ ID (bao gồm cả thư mục cha và các con cháu)
        List<String> fileAndAllChilds = filePersistencePort.findAllChildIds(command.getFileIdsToDelete());
        if(fileAndAllChilds == null || fileAndAllChilds.isEmpty()) {
            return new FailedHardDeleteFilesResult(errors);
        }

        // 2. Load toàn bộ entity lên bằng 1 câu SELECT duy nhất
        List<File> allItemsToDelete = filePersistencePort.findByOwnerAndFileIds(command.getOwnerId(), fileAndAllChilds);
        
        List<File> successfullyProcessedItems = new ArrayList<>();
        long refundQuota = 0;

        // 3. Phân loại và xử lý MinIO
        for(File item : allItemsToDelete) {
            if (item.isFile()) {
                try {
                    // Nếu <= 1 (Chỉ có mình nó xài) -> Xóa vật lý trên MinIO
                    if (filePersistencePort.countByStoragePath(item.getStoragePath()) <= 1) {
                        storagePort.delete(item.getStoragePath());
                    }
                    
                    // Dù có xóa vật lý hay không, vẫn phải xóa DB và hoàn dung lượng
                    refundQuota += item.getSizeBytes();
                    successfullyProcessedItems.add(item);
                    
                } catch (Exception e) {
                    logger.severe(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
                    errors.add(new FileIdAndReason(item.getId(), "Failed to delete physical file: " + e.getMessage()));
                }
            } else {
                // Nếu là Folder -> Không có file vật lý để xóa -> Đưa thẳng vào danh sách chờ xóa DB
                successfullyProcessedItems.add(item);
            }
        }
        
        // 4. Cập nhật Database & Quota cùng 1 lúc cho cả File và Folder
        if(!successfullyProcessedItems.isEmpty()) {
            fileAndUserTransactionPort.deleteFilesAndRefundQuotaStorage(
                    command.getOwnerId(), 
                    successfullyProcessedItems, 
                    refundQuota
            );
        }
        
        return new FailedHardDeleteFilesResult(errors);
    }
}