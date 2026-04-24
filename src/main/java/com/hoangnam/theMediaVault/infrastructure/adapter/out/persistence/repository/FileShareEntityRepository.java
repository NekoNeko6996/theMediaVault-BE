package com.hoangnam.theMediaVault.infrastructure.adapter.out.persistence.repository;

import com.hoangnam.theMediaVault.infrastructure.adapter.out.persistence.entity.FileShareEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * 
 */
@Repository
public interface FileShareEntityRepository extends JpaRepository<FileShareEntity, String> {

}
