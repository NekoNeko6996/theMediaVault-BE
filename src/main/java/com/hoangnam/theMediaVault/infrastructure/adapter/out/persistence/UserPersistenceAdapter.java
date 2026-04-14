package com.hoangnam.theMediaVault.infrastructure.adapter.out.persistence;

import com.hoangnam.theMediaVault.application.port.out.CheckUserPort;
import com.hoangnam.theMediaVault.application.port.out.SaveUserPort;
import com.hoangnam.theMediaVault.domain.model.User;
import com.hoangnam.theMediaVault.infrastructure.adapter.out.persistence.entity.UserEntity;
import com.hoangnam.theMediaVault.infrastructure.adapter.out.persistence.mapper.UserMapper;
import com.hoangnam.theMediaVault.infrastructure.adapter.out.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements SaveUserPort, CheckUserPort {
    
    // lấy repository
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    
    @Override
    public void save(User user) {
        UserEntity entity = userMapper.toEntity(user);
        userRepository.save(entity);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByUserName(String username) {
        return userRepository.existsByUsername(username);
    }

}
