package com.hoangnam.theMediaVault.application.service;

import com.hoangnam.theMediaVault.application.port.in.DuplicateFilesUseCase;
import com.hoangnam.theMediaVault.application.port.in.dto.command.DuplicateFilesCommand;
import com.hoangnam.theMediaVault.application.port.out.FileAndUserTransactionPort;
import com.hoangnam.theMediaVault.application.port.out.FilePersistencePort;
import com.hoangnam.theMediaVault.domain.exception.DomainException;
import com.hoangnam.theMediaVault.domain.model.File;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class DuplicateFilesService implements DuplicateFilesUseCase {

    private final FilePersistencePort filePersistencePort;
    private final FileAndUserTransactionPort fileAndUserTransactionPort;
    
    @Override
    @Transactional
    public void execute(DuplicateFilesCommand command) {
        command.validate();
        
        long sizeDelta = 0;
        File targetParent = null; 
        
        // Chuẩn hóa ID
        String targetParentId = (command.getNewParentId() == null || command.getNewParentId().trim().isEmpty()) ? null : command.getNewParentId();
        String oldParentId = (command.getOldParentId() == null || command.getOldParentId().trim().isEmpty()) ? null : command.getOldParentId();

        // 1. Kiểm tra thư mục đích
        if (targetParentId != null) {
            targetParent = filePersistencePort.findById(targetParentId)
                    .orElseThrow(() -> new DomainException("Target Folder doesn't exist."));
                    
            if (!targetParent.getOwner().getId().equals(command.getOwnerId())) {
                throw new DomainException("Permission denied for target folder.");
            }
            if (targetParent.isFile()) {
                throw new DomainException("It is not possible to copy into a file.");
            }
        }

        // Lấy danh sách file cần copy
        List<File> filesToCopy = filePersistencePort.findByOwnerAndFileIds(command.getOwnerId(), command.getFileIds());
        if (filesToCopy.isEmpty()) return;

        // Lấy danh sách tên file ở thư mục đích
        List<File> filesInNewDir = filePersistencePort.findByParentIdAndOwnerId(targetParentId, command.getOwnerId());
        Set<String> existingNamesInTarget = filesInNewDir.stream()
                .map(this::getFullName)
                .collect(Collectors.toSet());

        List<File> filesToDuplicate = new ArrayList<>();
        
        // So sánh an toàn không sợ Null
        boolean isSameFolder = Objects.equals(oldParentId, targetParentId);

        // 2. Vòng lặp xử lý
        for (File file : filesToCopy) {
            String originalFullName = getFullName(file);
            String newUniqueName = file.getName(); // Mặc định giữ nguyên tên

            // Quyết định đổi tên: Nếu paste cùng thư mục, HOẶC bị trùng tên ở thư mục mới
            if (isSameFolder || existingNamesInTarget.contains(originalFullName)) {
                newUniqueName = generateUniqueName(file.getName(), file.getExtension(), existingNamesInTarget);
            }

            // Chốt tên mới vào Set để file tiếp theo trong vòng lặp không giẫm lên
            existingNamesInTarget.add(newUniqueName + (file.getExtension() != null ? file.getExtension() : ""));

            // Luôn luôn cộng Quota dung lượng
            sizeDelta += file.getSizeBytes();

            // Sinh bản sao
            File newCopiedFile = file.duplicate(UUID.randomUUID().toString(), newUniqueName, targetParent);
            filesToDuplicate.add(newCopiedFile);
        }
        
        // 3. Lưu toàn bộ 1 lần
        if(!filesToDuplicate.isEmpty()) {
            fileAndUserTransactionPort.saveFilesAndUpdateStorage(command.getOwnerId(), filesToDuplicate, sizeDelta);
        }
    }

    private String getFullName(File file) {
        if (file.getExtension() != null && !file.getExtension().trim().isEmpty()) {
            return file.getName() + file.getExtension();
        }
        return file.getName();
    }
    
    private String generateUniqueName(String baseName, String extension, Set<String> existingNames) {
        int counter = 1;
        String newName = baseName + " (" + counter + ")";
        String ext = (extension != null) ? extension : "";
        
        // Vòng lặp tăng số lên (1), (2), (3)... cho đến khi tìm được tên chưa ai xài
        while (existingNames.contains(newName + ext)) {
            counter++;
            newName = baseName + " (" + counter + ")";
        }
        return newName;
    }
}
