package com.hoangnam.theMediaVault.application.port.out;

import com.hoangnam.theMediaVault.domain.model.Share;
import java.util.List;
import java.util.Optional;

/**
 *
 * 
 */
public interface SharePresistencePort {
    // Modifying
    void save(Share item);
    void delete(String ownerId, String shareId);
    void updateRecipients(String ownerId, String shareId, List<String> newListIdsOfRecipient);
    
    // Query
    Optional<Share> findById(String shareId);
    Optional<Share> findByIdAndOwnerId(String shareId, String ownerId);
    List<Share> findAllByOwnerId(String ownerId);
}
