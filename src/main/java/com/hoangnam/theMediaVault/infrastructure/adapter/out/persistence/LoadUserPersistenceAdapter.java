package com.hoangnam.theMediaVault.infrastructure.adapter.out.persistence;

import com.hoangnam.theMediaVault.application.port.out.LoadUserPort;
import com.hoangnam.theMediaVault.domain.model.User;
import com.hoangnam.theMediaVault.infrastructure.adapter.out.persistence.entity.UserEntity;
import com.hoangnam.theMediaVault.infrastructure.adapter.out.persistence.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 
 * Lấy dữ liệu người dùng từ DB thông qua repository và đảm nhận vai trò map nó sang Domain Model
 */
@Component
@RequiredArgsConstructor
public class LoadUserPersistenceAdapter implements LoadUserPort{

    private final UserRepository userRepository;

    @Override
    public Optional<User> findById(String id) {
       return userRepository.findById(id).map(this::mapToDomain);
    }
  
    
    /**
     * Map entity sang domain User model
     * @param entity
     * @return 
     */
    private User mapToDomain(UserEntity entity) {
        return User.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .passwordHash(entity.getPasswordHash())
                .isActive(entity.isActive())
                .role(entity.getRole())
                .storageLimit(entity.getStorageLimit())
                .usedStorage(entity.getUsedStorage())
                .emailVerifiedAt(entity.getEmailVerifiedAt())
                .createAt(entity.getCreateAt())
                .updateAt(entity.getUpdateAt())
                .lastLoginAt(entity.getLastLoginAt())
                .deletedAt(entity.getDeletedAt())
                .build();
    }
}
