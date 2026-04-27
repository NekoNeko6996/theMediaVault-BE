package com.hoangnam.theMediaVault.application.service;

import com.hoangnam.theMediaVault.application.port.in.dto.command.UploadFilesCommand;
import com.hoangnam.theMediaVault.application.port.in.dto.result.FailedFileUploadsResult;
import com.hoangnam.theMediaVault.application.port.in.UploadFilesUseCase;
import com.hoangnam.theMediaVault.application.port.in.dto.list_object.UploadError;
import com.hoangnam.theMediaVault.application.port.in.dto.list_object.UploadItem;
import com.hoangnam.theMediaVault.application.port.out.FileAndUserTransactionPort;
import com.hoangnam.theMediaVault.application.port.out.FilePersistencePort;
import com.hoangnam.theMediaVault.application.port.out.StoragePort;
import com.hoangnam.theMediaVault.application.port.out.UserPort;
import com.hoangnam.theMediaVault.domain.exception.DomainException;
import com.hoangnam.theMediaVault.domain.model.File;
import com.hoangnam.theMediaVault.domain.model.User;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UploadFilesService implements UploadFilesUseCase {

    private static final Logger logger = Logger.getLogger(UploadFilesService.class.getName());
    
    private final FilePersistencePort filePersistencePort;
    private final StoragePort storagePort;
    private final UserPort userPort;
    private final FileAndUserTransactionPort fileAndUserTransactionPort; 

    @Override
    public FailedFileUploadsResult execute(UploadFilesCommand command) {
        command.validate();

        User owner = userPort.findById(command.getOwnerId()).orElseThrow(() -> new DomainException("User not found."));

        String normalizedParentId = (command.getParentId() == null || command.getParentId().trim().isEmpty()) ? null : command.getParentId();
        File parent = null;
        if (normalizedParentId != null) {
            parent = filePersistencePort.findById(normalizedParentId).orElseThrow(() -> new DomainException("Parent folder not found."));
        }
        
        

        List<File> files = new ArrayList();
        long sizeDelta = 0;

        // save file vật lý và chuẩn hóa dữ liệu
        List<UploadError> error = new ArrayList<>();
        for (UploadItem item : command.getItems()) {
            try {
                // kiểm tra xem có đủ dung lượng để tải file này lên hay không
                if(!owner.canUpload(item.getSize())) {
                    throw new DomainException("Can't upload file " + item.getFileName() + " because used storage has reached the limit " + owner.getStorageLimit());
                }
                
                String extension = item.getExtension().startsWith(".")? item.getExtension() : "." + item.getExtension();
                
                String id = UUID.randomUUID().toString();
                String storgePath = owner.getRootDir() + id + extension;
                String fileHash = storagePort.upload(storgePath, item.getInputStream(), item.getSize(), item.getContentType());

                files.add(File.createFile(
                        id,
                        owner,
                        parent,
                        item.getFileName(),
                        item.getContentType(),
                        extension,
                        item.getSize(),
                        storgePath,
                        fileHash
                ));
                
                sizeDelta += item.getSize();
            } catch (Exception e) {
                error.add(new UploadError(item.getFileName(), e.getMessage()));
            }
        }

        // lưu file db
        if (!files.isEmpty()) {
            try {
                fileAndUserTransactionPort.saveFilesAndUpdateStorage(owner.getId(), files, sizeDelta);
            } catch (Exception dbException) {
                // Rollback: Xóa ngay các file vừa upload lên MinIO nếu lưu DB thất bại
                for (File file : files) {
                    try {
                        storagePort.delete(file.getStoragePath());
                    } catch (Exception minioException) {
                        logger.severe(() -> "Error delete trash file in minio when failed to save it to database: " + file.getOwner().getId() + " " + file.getName());
                        logger.severe(minioException.getMessage());
                    }
                }
                logger.severe(dbException.getMessage());
                throw new DomainException("Lỗi khi lưu Database. Đã hủy bỏ các file tải lên.", dbException);
            }
        }

        return new FailedFileUploadsResult(error);
    }

}
