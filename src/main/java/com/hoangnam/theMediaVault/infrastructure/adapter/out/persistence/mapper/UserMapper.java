package com.hoangnam.theMediaVault.infrastructure.adapter.out.persistence.mapper;

import com.hoangnam.theMediaVault.domain.model.Email;
import com.hoangnam.theMediaVault.domain.model.User;
import com.hoangnam.theMediaVault.domain.model.UserName;
import com.hoangnam.theMediaVault.infrastructure.adapter.out.persistence.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Chuyển từ Domain Model sang Persistence Entity
     * các field có cùng tên thì maptruct tự hiểu và tự map nên không cần dùng @mapping để định nghĩa
     * @param domain
     * @return 
     */
    @Mapping(target = "username",       source = "username.value")
    @Mapping(target = "email",          source = "email.value")
    @Mapping(target = "isActive",       source = "active")
    UserEntity toEntity(User domain);

    /**
     * Chuyển từ Persistence Entity sang Domain Model
     * @param entity
     * @return
     */
    @Mapping(target = "username",       source = "username", qualifiedByName = "toUserName")
    @Mapping(target = "email",          source = "email", qualifiedByName = "toEmail")
    @Mapping(target = "isActive",       source = "active")
    User toDomain(UserEntity entity);

    // --- Các hàm hỗ trợ chuyển đổi cho Value Objects ---
    @Named("toUserName")
    default UserName toUserName(String username) {
        return UserName.create(username);
    }
    
    @Named("toEmail")
    default Email toEmail(String email) {
        // Giả sử bạn đã tạo class Email với hàm static create() hoặc of() tương tự UserName
        return Email.create(email); 
    }
}