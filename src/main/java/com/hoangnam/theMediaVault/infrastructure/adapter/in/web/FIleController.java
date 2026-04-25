package com.hoangnam.theMediaVault.infrastructure.adapter.in.web;

import com.hoangnam.theMediaVault.application.port.in.CreateFolderUseCase;
import com.hoangnam.theMediaVault.application.port.in.MoveAllToTrashUseCase;
import com.hoangnam.theMediaVault.application.port.in.UploadFilesUseCase;
import com.hoangnam.theMediaVault.application.port.in.dto.command.CreateFolderCommand;
import com.hoangnam.theMediaVault.application.port.in.dto.command.MoveAllToTrashCommand;
import com.hoangnam.theMediaVault.application.port.in.dto.command.UploadFilesCommand;
import com.hoangnam.theMediaVault.application.port.in.dto.result.CreateFolderResult;
import com.hoangnam.theMediaVault.application.port.in.dto.result.FailedFileUploadsResult;
import com.hoangnam.theMediaVault.application.port.in.dto.result.FailedMoveAllToTrashResult;
import com.hoangnam.theMediaVault.infrastructure.adapter.in.web.dto.request.CreateFolderRequest;
import com.hoangnam.theMediaVault.infrastructure.adapter.in.web.dto.request.MoveToTrashRequest;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FIleController {
    
    private final CreateFolderUseCase createFolderUseCase;
    private final UploadFilesUseCase uploadFilesUseCase;
    private final MoveAllToTrashUseCase moveAllToTrashUseCase;

    @PostMapping("/create/foler")
    public ResponseEntity<?> createFoler(@Validated @RequestBody CreateFolderRequest request) {
        
        CreateFolderCommand command = CreateFolderCommand.builder()
                .folderName(request.getFolderName())
                .ownerId(request.getOwnerId())
                .parentId(request.getParentId())
                .build();
        
        CreateFolderResult result = createFolderUseCase.execute(command);
        
        return new ResponseEntity(result, HttpStatus.CREATED);
    }
    
    @PostMapping("/uploads") 
    public ResponseEntity<?> uploadFiles(
            @RequestParam("userId") String userId, 
            @RequestParam("folderId") String folderId, 
            @RequestParam("files") List<MultipartFile> files) {
        try {
            List<UploadFilesCommand.UploadItem> items = new ArrayList<>();
            
            for(MultipartFile file : files) {
                if(file.isEmpty()) continue;
                
                String originalFileName = file.getOriginalFilename();
                String extension = "";
                String fileNameWithoutEx = originalFileName;
                
                if(originalFileName != null && originalFileName.contains(".")) {
                    int lastDotIdx = originalFileName.lastIndexOf(".");
                    extension = originalFileName.substring(lastDotIdx);
                    fileNameWithoutEx = originalFileName.substring(0, lastDotIdx);
                }
                
                items.add(
                        UploadFilesCommand.UploadItem.builder()
                        .fileName(fileNameWithoutEx)
                        .extension(extension)
                        .contentType(file.getContentType())
                        .size(file.getSize())
                        .inputStream(file.getInputStream())
                        .build()
                );
            }
            
            UploadFilesCommand command = UploadFilesCommand.builder()
                    .items(items)
                    .parentId(folderId)
                    .ownerId(userId)
                    .build();
            
            FailedFileUploadsResult result = uploadFilesUseCase.execute(command);
            
            return ResponseEntity.ok(result);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    
    @PostMapping("/drop/files")
    public ResponseEntity<?> moveFilesToTrash(@Validated @RequestBody MoveToTrashRequest request) {
        
        FailedMoveAllToTrashResult result = moveAllToTrashUseCase.execute(
                MoveAllToTrashCommand.builder()
                .ownerId(request.ownerId)
                .fileIds(request.fileIds)
                .build()
        );
        
        return ResponseEntity.ok(result);
    }
    
}
