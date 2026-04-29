package com.hoangnam.theMediaVault.application.service;

import com.hoangnam.theMediaVault.application.port.in.MoveFilesUseCase;
import com.hoangnam.theMediaVault.application.port.in.dto.command.MoveFilesCommand;
import com.hoangnam.theMediaVault.application.port.in.dto.list_object.FileIdAndReason;
import com.hoangnam.theMediaVault.application.port.in.dto.result.FailedMoveFilesResult;
import com.hoangnam.theMediaVault.application.port.out.FilePersistencePort;
import com.hoangnam.theMediaVault.domain.exception.DomainException;
import com.hoangnam.theMediaVault.domain.model.File;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class MoveFilesService implements MoveFilesUseCase {

    private final FilePersistencePort filePersistencePort;
    
    @Override
    @Transactional
    public FailedMoveFilesResult execute(MoveFilesCommand command) {
        command.validate();
        
        File targetParent = null; // Khởi tạo là null cho thư mục Root
        String targetParentId = command.getNewParentFolderId();

        // Chuẩn hóa ID (nếu client gửi chuỗi rỗng thì coi như null)
        if (targetParentId != null && targetParentId.trim().isEmpty()) {
            targetParentId = null;
        }
        
        // 1. Kiểm tra thư mục đích (Chặn lỗi nếu target là File hoặc ko có quyền)
        if (targetParentId != null) {
            targetParent = filePersistencePort.findById(targetParentId)
                    .orElseThrow(() -> new DomainException("Target Folder doesn't exist."));
                    
            if (!targetParent.getOwner().getId().equals(command.getOwnerId())) {
                throw new DomainException("You don't have any permission to move file to new DIR.");
            }
            if (targetParent.isFile()) {
                throw new DomainException("It is not possible to move a file inside another file.");
            }
        }
                
        // 2. CHẶN THAM CHIẾU VÒNG (Circular Reference)
        if (targetParent != null) {
            List<String> allDescendantIds = filePersistencePort.findAllChildIds(command.getFilesTobeMove());
            if (allDescendantIds.contains(targetParent.getId()) || command.getFilesTobeMove().contains(targetParent.getId())) {
                throw new DomainException("Cannot move a folder into itself or its sub-folders.");
            }
        }

        // 3. Lấy Object File nguyên bản thay vì Map String để giữ lại Extension
        List<File> filesToMove = filePersistencePort.findByOwnerAndFileIds(command.getOwnerId(), command.getFilesTobeMove());
        Map<String, File> filesToMoveMap = filesToMove.stream()
                .collect(Collectors.toMap(File::getId, f -> f));

        // Lấy danh sách tên file trong thư mục đích (Lưu vào Set để check O(1))
        List<File> filesInNewDir = filePersistencePort.findByParentIdAndOwnerId(targetParent.getId(), command.getOwnerId());
        Set<String> existingNamesInTarget = filesInNewDir.stream()
                .map(this::getFullName)
                .collect(Collectors.toSet());

        List<String> simpleMoveIds = new ArrayList<>();
        List<FileIdAndReason> error = new ArrayList<>();

        // 4. Duyệt và xử lý từng file
        for (String idToMove : command.getFilesTobeMove()) {
            if (!filesToMoveMap.containsKey(idToMove)) {
                error.add(new FileIdAndReason(idToMove, "File not found or permission denied."));
                continue;
            }

            File fileToMove = filesToMoveMap.get(idToMove);
            String originalFullName = getFullName(fileToMove);

            // Kiểm tra trùng tên
            if (existingNamesInTarget.contains(originalFullName)) {
                // Tự động sinh tên mới (VD: anh (1).png)
                String newUniqueName = generateUniqueName(fileToMove.getName(), fileToMove.getExtension(), existingNamesInTarget);
                
                // Đổi tên & Move xuống DB
                filePersistencePort.renameAndMove(idToMove, newUniqueName, targetParentId);
                
                // Cập nhật Set để file tiếp theo trong vòng lặp không bị trùng vào tên vừa sinh ra
                existingNamesInTarget.add(newUniqueName + (fileToMove.getExtension() != null ? fileToMove.getExtension() : ""));
            } else {
                // Không trùng tên, gom lại để update parent_id hàng loạt cho tối ưu
                simpleMoveIds.add(idToMove);
                existingNamesInTarget.add(originalFullName);
            }
        }

        // 5. Update hàng loạt cho các file không bị trùng tên
        if (!simpleMoveIds.isEmpty()) {
            filePersistencePort.moveFilesToNewDir(simpleMoveIds, targetParentId);
        }

        return new FailedMoveFilesResult(error);
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
