package com.hoangnam.theMediaVault.infrastructure.adapter.in.web;

import com.hoangnam.theMediaVault.application.port.in.CreateFolderUseCase;
import com.hoangnam.theMediaVault.application.port.in.GetFilesUseCase;
import com.hoangnam.theMediaVault.application.port.in.MoveAllToTrashUseCase;
import com.hoangnam.theMediaVault.application.port.in.RenameFileUseCase;
import com.hoangnam.theMediaVault.application.port.in.UploadFilesUseCase;
import com.hoangnam.theMediaVault.application.port.in.dto.command.CheckFilesCanUploadQuery;
import com.hoangnam.theMediaVault.application.port.in.dto.command.CreateFolderCommand;
import com.hoangnam.theMediaVault.application.port.in.dto.command.GetFilesQuery;
import com.hoangnam.theMediaVault.application.port.in.dto.command.MoveAllToTrashCommand;
import com.hoangnam.theMediaVault.application.port.in.dto.command.RenameFileCommand;
import com.hoangnam.theMediaVault.application.port.in.dto.command.UploadFilesCommand;
import com.hoangnam.theMediaVault.application.port.in.dto.list_object.UploadItem;
import com.hoangnam.theMediaVault.application.port.in.dto.result.CheckFilesCanUploadResult;
import com.hoangnam.theMediaVault.application.port.in.dto.result.CreateFolderResult;
import com.hoangnam.theMediaVault.application.port.in.dto.result.FailedFileUploadsResult;
import com.hoangnam.theMediaVault.application.port.in.dto.result.FailedMoveAllToTrashResult;
import com.hoangnam.theMediaVault.domain.model.File;
import com.hoangnam.theMediaVault.infrastructure.adapter.in.web.dto.request.CheckFilesCanUploadRequest;
import com.hoangnam.theMediaVault.infrastructure.adapter.in.web.dto.request.CreateFolderRequest;
import com.hoangnam.theMediaVault.infrastructure.adapter.in.web.dto.request.GetFilesRequest;
import com.hoangnam.theMediaVault.infrastructure.adapter.in.web.dto.request.MoveToTrashRequest;
import com.hoangnam.theMediaVault.infrastructure.adapter.in.web.dto.request.RenameFileRequest;
import com.hoangnam.theMediaVault.infrastructure.adapter.in.web.dto.response.GetFilesResponse;
import com.hoangnam.theMediaVault.infrastructure.security.model.CustomUserDetail;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.hoangnam.theMediaVault.application.port.in.CheckFilesCanUploadUseCase;
import com.hoangnam.theMediaVault.application.port.in.MoveFilesUseCase;
import com.hoangnam.theMediaVault.application.port.in.RestoreFilesFromTrashUseCase;
import com.hoangnam.theMediaVault.application.port.in.ToggleStarredFileUseCase;
import com.hoangnam.theMediaVault.application.port.in.dto.command.MoveFilesCommand;
import com.hoangnam.theMediaVault.application.port.in.dto.command.RestoreFilesFromTrashCommand;
import com.hoangnam.theMediaVault.application.port.in.dto.command.ToggleStarredFileCommand;
import com.hoangnam.theMediaVault.application.port.in.dto.list_object.FilesHashAndSize;
import com.hoangnam.theMediaVault.application.port.in.dto.result.FailedMoveFilesResult;
import com.hoangnam.theMediaVault.application.port.in.dto.result.ToggleStarredFileResult;
import com.hoangnam.theMediaVault.infrastructure.adapter.in.web.dto.request.MoveFilesRequest;
import com.hoangnam.theMediaVault.infrastructure.adapter.in.web.dto.request.RestoreFilesFromTrashRequest;
import com.hoangnam.theMediaVault.infrastructure.adapter.in.web.dto.request.ToggleStarredFileRequest;
import com.hoangnam.theMediaVault.infrastructure.adapter.in.web.dto.response.CheckFilesCanUploadResponse;
import com.hoangnam.theMediaVault.infrastructure.service.JWTService;
import org.springframework.beans.factory.annotation.Value;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {

    private final CreateFolderUseCase createFolderUseCase;
    private final UploadFilesUseCase uploadFilesUseCase;
    private final MoveAllToTrashUseCase moveAllToTrashUseCase;
    private final GetFilesUseCase getFilesUseCase;
    private final RenameFileUseCase renameFileUseCase;
    private final CheckFilesCanUploadUseCase checkFilesExistsUseCase;
    private final MoveFilesUseCase moveFilesUseCase;
    private final ToggleStarredFileUseCase toggleStarredFileUseCase; 
    private final RestoreFilesFromTrashUseCase restoreFilesFromTrashUseCase;

    private final JWTService jwtService;

    @Value("${jwt.upload.token.expiration}")
    private Long jwtUploadExpiration;

    @PostMapping("/create/folder")
    public ResponseEntity<?> createFoler(@AuthenticationPrincipal CustomUserDetail user, @RequestBody CreateFolderRequest request) {

        CreateFolderCommand command = CreateFolderCommand.builder()
                .folderName(request.getFolderName())
                .ownerId(user.getDomainUser().getId())
                .parentId(request.getParentId())
                .build();

        CreateFolderResult result = createFolderUseCase.execute(command);

        return new ResponseEntity(result, HttpStatus.CREATED);
    }

    @PostMapping("/uploads")
    public ResponseEntity<?> uploadFiles(
            @AuthenticationPrincipal CustomUserDetail user,
            @RequestParam("folderId") String folderId,
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("uploadTokens") List<String> uploadTokens) {
        try {
            if (files.size() != uploadTokens.size()) {
                return ResponseEntity.badRequest().body("Invalid upload data: files and uploadTokens must be equal.");
            }

            List<UploadItem> items = new ArrayList<>();

            for (int i = 0; i < files.size(); i++) {
                MultipartFile file = files.get(i);
                String token = uploadTokens.get(i);

                String[] hashAndSize = jwtService.extractSubject(token).split("_");
                String approvedHash = hashAndSize[0];
                long approvedSize = Long.parseLong(hashAndSize[1]);

                if (file.isEmpty()) {
                    continue;
                }

                String originalFileName = file.getOriginalFilename();
                String extension = "";
                String fileNameWithoutEx = originalFileName;

                if (originalFileName != null && originalFileName.contains(".")) {
                    int lastDotIdx = originalFileName.lastIndexOf(".");
                    extension = originalFileName.substring(lastDotIdx);
                    fileNameWithoutEx = originalFileName.substring(0, lastDotIdx);
                }

                items.add(UploadItem.builder()
                        .fileName(fileNameWithoutEx)
                        .extension(extension)
                        .contentType(file.getContentType())
                        .size(file.getSize())
                        .approvedSize(approvedSize)
                        .approvedHash(approvedHash)
                        .inputStream(file.getInputStream())
                        .build()
                );
            }

            UploadFilesCommand command = UploadFilesCommand.builder()
                    .items(items)
                    .parentId(folderId)
                    .ownerId(user.getDomainUser().getId())
                    .build();

            FailedFileUploadsResult result = uploadFilesUseCase.execute(command);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/drop")
    public ResponseEntity<?> moveFilesToTrash(@AuthenticationPrincipal CustomUserDetail user, @RequestBody MoveToTrashRequest request) {

        FailedMoveAllToTrashResult result = moveAllToTrashUseCase.execute(
                MoveAllToTrashCommand.builder()
                        .ownerId(user.getDomainUser().getId())
                        .fileIds(request.fileIds)
                        .build()
        );

        return ResponseEntity.ok(result);
    }

    @PostMapping("/get")
    public ResponseEntity<?> getFiles(@AuthenticationPrincipal CustomUserDetail user, @RequestBody GetFilesRequest request) {
        List<File> files = getFilesUseCase.execute(new GetFilesQuery(user.getDomainUser().getId(), request.getParentId()));

        return ResponseEntity.ok(GetFilesResponse.fromDomain(files));
    }

    @PostMapping("/rename")
    public ResponseEntity<?> rename(@AuthenticationPrincipal CustomUserDetail user, @RequestBody RenameFileRequest request) {
        renameFileUseCase.execute(new RenameFileCommand(request.getFileId(), user.getDomainUser().getId(), request.getNewName()));

        return ResponseEntity.ok("Rename file successfully");
    }

    @PostMapping("/exists")
    public ResponseEntity<?> checkFilesExists(@AuthenticationPrincipal CustomUserDetail user, @RequestBody CheckFilesCanUploadRequest request) {
        CheckFilesCanUploadResult result = checkFilesExistsUseCase.execute(new CheckFilesCanUploadQuery(user.getDomainUser().getId(), request.getFilesHashAndSize()));

        List<CheckFilesCanUploadResponse.RequireUploads> canUploads = new ArrayList();
        for (FilesHashAndSize item : result.getCanUploads()) {
            String token = jwtService.generateToken(item.getFileHash() + "_" + item.getSizeBytes(), jwtUploadExpiration);
            canUploads.add(new CheckFilesCanUploadResponse.RequireUploads(item.getFileHash(), token));
        }

        return ResponseEntity.ok(new CheckFilesCanUploadResponse(result.getExistingFilesHash(), canUploads, result.getRefuseFilesHash()));
    }
    
    @PostMapping("/move") 
    public ResponseEntity<?> moveFiles(@AuthenticationPrincipal CustomUserDetail user, @RequestBody MoveFilesRequest request) {
        FailedMoveFilesResult result = moveFilesUseCase.execute(
                new MoveFilesCommand(user.getDomainUser().getId(), request.getNewParentFileId(), request.getFileToMoveIds())
        );
        
        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/toggle-starred") 
    public ResponseEntity<?> toggleStarred(@AuthenticationPrincipal CustomUserDetail user, @RequestBody ToggleStarredFileRequest request) {
        ToggleStarredFileResult result = toggleStarredFileUseCase.execute(new ToggleStarredFileCommand(user.getDomainUser().getId(), request.getTargetFileId()));
        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/restore")
    public ResponseEntity<?> restoreFileFromTrash(@AuthenticationPrincipal CustomUserDetail user, @RequestBody RestoreFilesFromTrashRequest request) {
        restoreFilesFromTrashUseCase.execute(new RestoreFilesFromTrashCommand(user.getDomainUser().getId(), request.getFileIds()));
        return ResponseEntity.ok("Files restore success.");
    }
}
    