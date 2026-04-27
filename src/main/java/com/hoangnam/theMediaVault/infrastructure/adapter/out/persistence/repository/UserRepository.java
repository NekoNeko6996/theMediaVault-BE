package com.hoangnam.theMediaVault.infrastructure.adapter.out.persistence.repository;

import com.hoangnam.theMediaVault.infrastructure.adapter.out.persistence.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * 
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    
    Optional<UserEntity> findByUsername(String username);
    
    @Modifying
    @Query("UPDATE UserEntity u SET u.usedStorage = u.usedStorage + :sizeDelta WHERE u.id = :userId")
    void updateUsedStorage(@Param("userId") String userId, @Param("sizeDelta") long sizeDelta);
}
