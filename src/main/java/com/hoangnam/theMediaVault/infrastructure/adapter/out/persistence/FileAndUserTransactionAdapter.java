package com.hoangnam.theMediaVault.infrastructure.adapter.out.persistence;

import com.hoangnam.theMediaVault.application.port.out.FileAndUserTransactionPort;
import com.hoangnam.theMediaVault.domain.model.File;
import com.hoangnam.theMediaVault.infrastructure.adapter.out.persistence.mapper.FileMapper;
import com.hoangnam.theMediaVault.infrastructure.adapter.out.persistence.repository.FileEntityRepository;
import com.hoangnam.theMediaVault.infrastructure.adapter.out.persistence.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FileAndUserTransactionAdapter implements FileAndUserTransactionPort{

    private final UserRepository userRepository;
    private final FileEntityRepository fileEntityRepository;
    private final FileMapper fileMapper;
    
    @Override
    @Transactional
    public void saveFilesAndUpdateStorage(String ownerId, List<File> files, long sizeDelta) {
        userRepository.updateUsedStorage(ownerId, sizeDelta);
        fileEntityRepository.saveAll(files.stream().map(fileMapper::toEntity).collect(Collectors.toList()));
    }

}
