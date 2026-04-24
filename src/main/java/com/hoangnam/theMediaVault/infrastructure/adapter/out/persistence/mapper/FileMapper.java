package com.hoangnam.theMediaVault.infrastructure.adapter.out.persistence.mapper;

import com.hoangnam.theMediaVault.domain.model.Email;
import com.hoangnam.theMediaVault.domain.model.File;
import com.hoangnam.theMediaVault.domain.model.UserName;
import com.hoangnam.theMediaVault.infrastructure.adapter.out.persistence.entity.FileEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 *
 * 
 */
@Mapper(componentModel = "spring")
public interface FileMapper {
    
    @Mapping(target = "owner.username", source = "owner.username.value")
    @Mapping(target = "owner.email",    source = "owner.email.value")
    @Mapping(target = "owner.isActive", source = "owner.active")
    @Mapping(target = "isStarred",      source = "starred")
    @Mapping(target = "isTrashed",      source = "trashed")
    FileEntity toEntity(File domain);
    
    
    @Mapping(target = "owner.username", source = "owner.username", qualifiedByName = "toUserName")
    @Mapping(target = "owner.email",    source = "owner.email", qualifiedByName = "toEmail")
    @Mapping(target = "owner.isActive", source = "owner.active")
    @Mapping(target = "isStarred",      source = "starred")
    @Mapping(target = "isTrashed",      source = "trashed")
    File toDomain(FileEntity entity);
    
    // hepper
    @Named("toUserName")
    default UserName toUserName(String username) {
        return UserName.create(username);
    }
    
    @Named("toEmail")
    default Email toEmail(String email) {
        return Email.create(email);
    }
}
