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
import com.hoangnam.theMediaVault.application.port.in.dto.objects.UploadItem;
import com.hoangnam.theMediaVault.application.port.in.dto.result.CheckFilesCanUploadResult;
import com.hoangnam.theMediaVault.application.port.in.dto.result.CreateFolderResult;
import com.hoangnam.theMediaVault.application.port.in.dto.result.FailedFileUploadsResult;
import com.hoangnam.theMediaVault.application.port.in.dto.result.FailedMoveAllToTrashResult;
import com.hoangnam.theMediaVault.domain.model.File;
import com.hoangnam.theMediaVault.infrastructure.adapter.in.web.dto.request.CheckFilesCanUploadRequest;
import com.hoangnam.theMediaVault.infrastructure.adapter.in.web.dto.request.CreateFolderRequest;
import com.hoangnam.theMediaVault.infrastructure.adapter.in.web.dto.request.MoveToTrashRequest;
import com.hoangnam.theMediaVault.infrastructure.adapter.in.web.dto.request.RenameFileRequest;
import com.hoangnam.theMediaVault.infrastructure.adapter.in.web.dto.response.GetFilesResponse;
import com.hoangnam.theMediaVault.infrastructure.security.model.CustomUserDetail;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.hoangnam.theMediaVault.application.port.in.CheckFilesCanUploadUseCase;
import com.hoangnam.theMediaVault.application.port.in.DuplicateFilesUseCase;
import com.hoangnam.theMediaVault.application.port.in.EmptyTrashUseCase;
import com.hoangnam.theMediaVault.application.port.in.GetAllStarredFilesUseCase;
import com.hoangnam.theMediaVault.application.port.in.GetDownloadUrlUseCase;
import com.hoangnam.theMediaVault.application.port.in.GetFileInfoUseCase;
import com.hoangnam.theMediaVault.application.port.in.GetTrashFilesUseCase;
import com.hoangnam.theMediaVault.application.port.in.HardDeleteFilesUseCase;
import com.hoangnam.theMediaVault.application.port.in.MoveFilesUseCase;
import com.hoangnam.theMediaVault.application.port.in.RestoreFilesFromTrashUseCase;
import com.hoangnam.theMediaVault.application.port.in.SearchFilesByKeywordUseCase;
import com.hoangnam.theMediaVault.application.port.in.ToggleStarredFileUseCase;
import com.hoangnam.theMediaVault.application.port.in.dto.command.DuplicateFilesCommand;
import com.hoangnam.theMediaVault.application.port.in.dto.command.EmptyTrashCommand;
import com.hoangnam.theMediaVault.application.port.in.dto.command.GetAllStarredFilesQuery;
import com.hoangnam.theMediaVault.application.port.in.dto.command.GetDowloadUrlQuery;
import com.hoangnam.theMediaVault.application.port.in.dto.command.GetFileInfoQuery;
import com.hoangnam.theMediaVault.application.port.in.dto.command.GetTrashFilesQuery;
import com.hoangnam.theMediaVault.application.port.in.dto.command.HardDeleteFilesCommand;
import com.hoangnam.theMediaVault.application.port.in.dto.command.MoveFilesCommand;
import com.hoangnam.theMediaVault.application.port.in.dto.command.RestoreFilesFromTrashCommand;
import com.hoangnam.theMediaVault.application.port.in.dto.command.SearchFilesByKeywordQuery;
import com.hoangnam.theMediaVault.application.port.in.dto.command.ToggleStarredFileCommand;
import com.hoangnam.theMediaVault.application.port.in.dto.objects.FilesHashAndSize;
import com.hoangnam.theMediaVault.application.port.in.dto.result.FailedHardDeleteFilesResult;
import com.hoangnam.theMediaVault.application.port.in.dto.result.FailedMoveFilesResult;
import com.hoangnam.theMediaVault.application.port.in.dto.result.GetDownLoadUrlResult;
import com.hoangnam.theMediaVault.application.port.in.dto.result.ToggleStarredFileResult;
import com.hoangnam.theMediaVault.infrastructure.adapter.in.web.dto.request.DuplicateFilesRequest;
import com.hoangnam.theMediaVault.infrastructure.adapter.in.web.dto.request.HardDeleteFilesRequest;
import com.hoangnam.theMediaVault.infrastructure.adapter.in.web.dto.request.MoveFilesRequest;
import com.hoangnam.theMediaVault.infrastructure.adapter.in.web.dto.request.RestoreFilesFromTrashRequest;
import com.hoangnam.theMediaVault.infrastructure.adapter.in.web.dto.response.CheckFilesCanUploadResponse;
import com.hoangnam.theMediaVault.infrastructure.adapter.in.web.dto.response.GetFileResponse;
import com.hoangnam.theMediaVault.infrastructure.adapter.in.web.dto.response.OnlyMessageResponse;
import com.hoangnam.theMediaVault.infrastructure.service.JWTService;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
    private final GetDownloadUrlUseCase getDownloadUrlUseCase; 
    private final GetTrashFilesUseCase getTrashFilesUseCase;
    private final HardDeleteFilesUseCase hardDeleteFilesUseCase;
    private final GetAllStarredFilesUseCase getAllStarredFilesUseCase;
    private final SearchFilesByKeywordUseCase searchFilesByKeywordUseCase;
    private final DuplicateFilesUseCase duplicateFilesUseCase;
    private final EmptyTrashUseCase emptyTrashUseCase;
    private final GetFileInfoUseCase getFileInfoUseCase;
    
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

        return ResponseEntity.ok(result);
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

                String[] hashAndSize;
                try {
                    hashAndSize = jwtService.extractSubject(token).split("_");
                }
                catch(JwtException e) {
                    return ResponseEntity.badRequest().body(new OnlyMessageResponse("Invalid upload token: " + e.getMessage()));
                }
                
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

    // parentId = root thì sẽ lấy ở root
    @GetMapping("/get/files/{parentId}")
    public ResponseEntity<?> getFiles(@AuthenticationPrincipal CustomUserDetail user, @PathVariable("parentId") String parentId) {
        parentId = "root".equals(parentId)? "" : parentId;
        List<File> files = getFilesUseCase.execute(new GetFilesQuery(user.getDomainUser().getId(), parentId));

        return ResponseEntity.ok(GetFilesResponse.fromDomain(files));
    }

    @PostMapping("/rename")
    public ResponseEntity<?> rename(@AuthenticationPrincipal CustomUserDetail user, @RequestBody RenameFileRequest request) {
        renameFileUseCase.execute(new RenameFileCommand(request.getFileId(), user.getDomainUser().getId(), request.getNewName()));

        return ResponseEntity.ok(new OnlyMessageResponse("Rename file successfully"));
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
    
    @PostMapping("/toggle/starred/{fileId}") 
    public ResponseEntity<?> toggleStarred(@AuthenticationPrincipal CustomUserDetail user, @PathVariable("fileId") String fileId) {
        ToggleStarredFileResult result = toggleStarredFileUseCase.execute(new ToggleStarredFileCommand(user.getDomainUser().getId(), fileId));
        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/restore")
    public ResponseEntity<?> restoreFileFromTrash(@AuthenticationPrincipal CustomUserDetail user, @RequestBody RestoreFilesFromTrashRequest request) {
        restoreFilesFromTrashUseCase.execute(new RestoreFilesFromTrashCommand(user.getDomainUser().getId(), request.getFileIds()));
        return ResponseEntity.ok(new OnlyMessageResponse("Files restore success."));
    }
    
    @GetMapping("/download/{fileId}")
    public ResponseEntity<?> getDownLoadUrl(@AuthenticationPrincipal CustomUserDetail user, @PathVariable("fileId") String fileId, @RequestParam("type") String type) {
        GetDownLoadUrlResult result = getDownloadUrlUseCase.execute(new GetDowloadUrlQuery(user.getDomainUser().getId(), fileId, type));
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/get/trash")
    public ResponseEntity<?> getAllTrashFiles(@AuthenticationPrincipal CustomUserDetail user) {
        List<File> trashed = getTrashFilesUseCase.execute(new GetTrashFilesQuery(user.getDomainUser().getId()));
        return ResponseEntity.ok(GetFilesResponse.fromDomain(trashed));
    }
    
    @PostMapping("/delete")  
    public ResponseEntity<?> hardDeleteFiles(@AuthenticationPrincipal CustomUserDetail user, @RequestBody HardDeleteFilesRequest request) {
        FailedHardDeleteFilesResult result = hardDeleteFilesUseCase.execute(new HardDeleteFilesCommand(user.getDomainUser().getId(), request.getFileIdsToDelete()));
        return ResponseEntity.ok(result);
    } 
    
    @GetMapping("/get/starred")
    public ResponseEntity<?> getAllStarredFiles(@AuthenticationPrincipal CustomUserDetail user) {
        return ResponseEntity.ok(GetFilesResponse.fromDomain(getAllStarredFilesUseCase.execute(new GetAllStarredFilesQuery(user.getDomainUser().getId()))));
    }
    
    @GetMapping("/search/{keyword}")
    public ResponseEntity<?> searchFiles(@AuthenticationPrincipal CustomUserDetail user, @PathVariable("keyword") String keyword) {
        String cleanKeyword = (keyword != null)? keyword.trim() : "";
        cleanKeyword = cleanKeyword.replace("%", "").replace("_", "");
        if (cleanKeyword.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid Keyword.");
        }
        
        SearchFilesByKeywordQuery query = new SearchFilesByKeywordQuery(
                user.getDomainUser().getId(), 
                cleanKeyword
        );
        
        List<File> files = searchFilesByKeywordUseCase.execute(query);
        return ResponseEntity.ok(GetFilesResponse.fromDomain(files));
    }
    
    @PostMapping("/empty/trash")
    public ResponseEntity<?> emptyTrash(@AuthenticationPrincipal CustomUserDetail user) {
        emptyTrashUseCase.execute(new EmptyTrashCommand(user.getDomainUser().getId()));
        return ResponseEntity.ok(new OnlyMessageResponse("Empty trash success."));
    }
    
    @GetMapping("/get/info/{fileId}")
    public ResponseEntity<?> getFileInfo(@AuthenticationPrincipal CustomUserDetail user, @PathVariable("fileId") String fileId) {
        File file = getFileInfoUseCase.execute(new GetFileInfoQuery(user.getDomainUser().getId(), fileId));
        return ResponseEntity.ok(GetFileResponse.fromDomain(file));
    }
    
    @PostMapping("/duplicate") 
    public ResponseEntity<?> duplicateFiles(@AuthenticationPrincipal CustomUserDetail user, @RequestBody DuplicateFilesRequest request) {
        duplicateFilesUseCase.execute(new DuplicateFilesCommand(
                user.getDomainUser().getId(), 
                request.getNewParentId(), 
                request.getOldParentId(), 
                request.getFileIds()
        ));
        return ResponseEntity.ok(new OnlyMessageResponse("Success."));
    }
}