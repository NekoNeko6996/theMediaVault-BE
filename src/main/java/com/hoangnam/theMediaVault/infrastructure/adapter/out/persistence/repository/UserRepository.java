package com.hoangnam.theMediaVault.infrastructure.adapter.out.persistence.repository;

import com.hoangnam.theMediaVault.infrastructure.adapter.out.persistence.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * 
 */
@Repository
public interface UserRepository extends CrudRepository<UserEntity, String> {
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    
    Optional<UserEntity> findByUsername(String username);
}
