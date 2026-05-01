package com.hoangnam.theMediaVault.application.service;

import com.hoangnam.theMediaVault.application.port.in.dto.command.CheckFilesCanUploadQuery;
import com.hoangnam.theMediaVault.application.port.in.dto.objects.FilesHashAndSize;
import com.hoangnam.theMediaVault.application.port.in.dto.result.CheckFilesCanUploadResult;
import com.hoangnam.theMediaVault.application.port.out.FilePersistencePort;
import com.hoangnam.theMediaVault.domain.model.File;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import com.hoangnam.theMediaVault.application.port.in.CheckFilesCanUploadUseCase;
import com.hoangnam.theMediaVault.application.port.in.dto.objects.FileHashAndReason;
import com.hoangnam.theMediaVault.application.port.out.UserPort;
import com.hoangnam.theMediaVault.domain.exception.DomainException;
import com.hoangnam.theMediaVault.domain.model.User;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CheckFilesCanUploadService implements CheckFilesCanUploadUseCase {

    private final FilePersistencePort filePersistencePort;
    private final UserPort userPort;

    @Override
    public CheckFilesCanUploadResult execute(CheckFilesCanUploadQuery query) {
        query.validate();

        User owner = userPort.findById(query.getOwnerId()).orElseThrow(() -> new DomainException("User not found."));

        List<File> existingDbFiles = filePersistencePort.findExistingFiles(
                query.getOwnerId(),
                query.getFilesHashAndSize().stream().map(FilesHashAndSize::getFileHash).collect(Collectors.toList())
        );

        // tạo hash set để tra cứu
        Map<String, Long> existingFilesMap = existingDbFiles.stream()
                .collect(Collectors.toMap(
                        File::getFileHash, 
                        File::getSizeBytes, 
                        (size1, size2) -> size1 // Xử lý collision nếu lỡ DB có 2 file trùng hash
                ));

        List<FilesHashAndSize> canUploads = new ArrayList<>();
        List<String> existingFilesHash = new ArrayList<>();
        List<FileHashAndReason> refuseFilesHash = new ArrayList<>();

        long availableStorage = owner.getStorageLimit() - owner.getUsedStorage();
        long accumulatedSizeDelta = 0L;

        for (FilesHashAndSize reqFile : query.getFilesHashAndSize()) {
            
            // 1. Kiểm tra xem Hash đã tồn tại trong DB chưa
            if (existingFilesMap.containsKey(reqFile.getFileHash())) {
                long actualSizeInDb = existingFilesMap.get(reqFile.getFileHash());
                
                if (actualSizeInDb == reqFile.getSizeBytes()) {
                    // Trường hợp 1: File đã tồn tại và thông tin chính xác
                    existingFilesHash.add(reqFile.getFileHash());
                } else {
                    // Trường hợp 2: Sai lệch dữ liệu (Client fake size hoặc bị lỗi) -> Chặn ngay lập tức
                    refuseFilesHash.add(new FileHashAndReason(
                            reqFile.getFileHash(),
                            "Data mismatch: File hash already exists but with a different size. Real size is " + actualSizeInDb + " bytes."
                    ));
                }
                continue; // Bỏ qua, không chạy xuống check dung lượng nữa
            }

            // Trường hợp 3: Hash hoàn toàn mới -> Kiểm tra dung lượng
            if (accumulatedSizeDelta + reqFile.getSizeBytes() <= availableStorage) {
                // Đủ dung lượng -> Cấp phép upload & cộng dồn dung lượng chờ
                canUploads.add(reqFile);
                accumulatedSizeDelta += reqFile.getSizeBytes();
            } else {
                // Trường hợp 4: Vượt quá dung lượng cho phép
                refuseFilesHash.add(new FileHashAndReason(
                        reqFile.getFileHash(),
                        "Insufficient storage space. Need more " + reqFile.getSizeBytes() + " bytes but only " + (availableStorage - accumulatedSizeDelta) + " bytes."
                ));
            }
        }

        return new CheckFilesCanUploadResult(existingFilesHash, canUploads, refuseFilesHash);
    }

}
