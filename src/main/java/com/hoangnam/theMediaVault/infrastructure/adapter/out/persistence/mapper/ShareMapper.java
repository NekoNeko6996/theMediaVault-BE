package com.hoangnam.theMediaVault.infrastructure.adapter.out.persistence.mapper;

import com.hoangnam.theMediaVault.domain.model.Share;
import com.hoangnam.theMediaVault.infrastructure.adapter.out.persistence.entity.ShareEntity;
import org.mapstruct.Mapper;

/**
 *  dùng 2 mapper có sẵn trước đó để mapper tự lo việc chuyển đổi
 * 
 */
@Mapper(componentModel = "spring", uses = {FileMapper.class, UserMapper.class})
public interface ShareMapper {
    Share toDomain(ShareEntity entity);
    ShareEntity toEntity(Share domain);
}
