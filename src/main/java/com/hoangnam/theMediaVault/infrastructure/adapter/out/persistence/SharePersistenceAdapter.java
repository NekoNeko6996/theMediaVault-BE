package com.hoangnam.theMediaVault.infrastructure.adapter.out.persistence;

import com.hoangnam.theMediaVault.application.port.out.SharePresistencePort;
import com.hoangnam.theMediaVault.domain.model.Share;
import com.hoangnam.theMediaVault.infrastructure.adapter.out.persistence.mapper.ShareMapper;
import com.hoangnam.theMediaVault.infrastructure.adapter.out.persistence.repository.ShareEntityRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@RequiredArgsConstructor
public class SharePersistenceAdapter implements SharePresistencePort {
    
    private final ShareEntityRepository shareEntityRepository;
    private final ShareMapper shareMapper;

    // modifying
    @Override
    @Transactional
    public void save(Share item) {
        shareEntityRepository.save(shareMapper.toEntity(item));
    }

    @Override
    @Transactional
    public void delete(String ownerId, String shareId) {
        shareEntityRepository.deleteByIdAndSharedBy_Id(shareId, ownerId);
    }

    @Override
    @Transactional
    public void updateRecipients(String ownerId, String shareId, List<String> newListIdsOfRecipient) {
        // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
    
    // query
    @Override
    public Optional<Share> findById(String shareId) {
        // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Optional<Share> findByIdAndOwnerId(String shareId, String ownerId) {
        // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Share> findAllByOwnerId(String ownerId) {
        // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
