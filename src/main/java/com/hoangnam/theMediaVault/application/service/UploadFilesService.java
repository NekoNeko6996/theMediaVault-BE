package com.hoangnam.theMediaVault.application.service;

import com.hoangnam.theMediaVault.application.port.in.dto.command.UploadFilesCommand;
import com.hoangnam.theMediaVault.application.port.in.dto.result.FailedFileUploadsResult;
import com.hoangnam.theMediaVault.application.port.in.UploadFilesUseCase;
import com.hoangnam.theMediaVault.application.port.out.FilePersistencePort;
import com.hoangnam.theMediaVault.application.port.out.LoadUserPort;
import com.hoangnam.theMediaVault.application.port.out.StoragePort;
import com.hoangnam.theMediaVault.domain.exception.DomainException;
import com.hoangnam.theMediaVault.domain.model.File;
import com.hoangnam.theMediaVault.domain.model.User;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UploadFilesService implements UploadFilesUseCase {

    private static final Logger logger = Logger.getLogger(UploadFilesService.class.getName());
    
    private final LoadUserPort loadUserPort;
    private final FilePersistencePort filePersistencePort;
    private final StoragePort storagePort;

    @Override
    public FailedFileUploadsResult execute(UploadFilesCommand command) {
        command.validate();

        User owner = loadUserPort.findById(command.getOwnerId()).orElseThrow(() -> new DomainException("User not found."));

        String normalizedParentId = (command.getParentId() == null || command.getParentId().trim().isEmpty()) ? null : command.getParentId();
        File parent = null;
        if (normalizedParentId != null) {
            parent = filePersistencePort.findById(normalizedParentId).orElseThrow(() -> new DomainException("Parent folder not found."));
        }

        List<File> files = new ArrayList();

        // save file vật lý và chuẩn hóa dữ liệu
        List<FailedFileUploadsResult.UploadError> error = new ArrayList<>();
        for (UploadFilesCommand.UploadItem item : command.getItems()) {
            String extension = item.getExtension().startsWith(".")? item.getExtension() : "." + item.getExtension();
            String path = (parent == null ? owner.getRootDir() : parent.getStoragePath()) + item.getFileName() + item.getExtension();
            try {
                String fileHash = storagePort.upload(path, item.getInputStream(), item.getSize(), item.getContentType());

                files.add(File.createFile(
                        owner,
                        parent,
                        item.getFileName(),
                        item.getContentType(),
                        extension,
                        item.getSize(),
                        path,
                        fileHash
                ));
            } catch (Exception e) {
                error.add(new FailedFileUploadsResult.UploadError(item.getFileName(), e.getMessage()));
            }
        }

        // lưu file db
        if (!files.isEmpty()) {
            try {
                filePersistencePort.saves(files);
            } catch (Exception dbException) {
                // Rollback: Xóa ngay các file vừa upload lên MinIO nếu lưu DB thất bại
                for (File file : files) {
                    try {
                        storagePort.delete(file.getStoragePath());
                    } catch (Exception minioException) {
                        logger.severe(() -> "Error delete trash file in minio when failed to save it to database: " + file.getOwner().getId() + " " + file.getName());
                    }
                }
                throw new DomainException("Lỗi khi lưu Database. Đã hủy bỏ các file tải lên.", dbException);
            }
        }

        return new FailedFileUploadsResult(error);
    }

}
