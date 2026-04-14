package com.hoangnam.theMediaVault.application.service;

import com.hoangnam.theMediaVault.application.port.in.CreateUserUseCase;
import com.hoangnam.theMediaVault.application.port.out.CheckUserPort;
import com.hoangnam.theMediaVault.application.port.out.PasswordEncoderPort;
import com.hoangnam.theMediaVault.application.port.out.SaveUserPort;
import com.hoangnam.theMediaVault.domain.exception.DomainException;
import com.hoangnam.theMediaVault.domain.model.User;
import com.hoangnam.theMediaVault.infrastructure.adapter.in.dto.CreateUserRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateUserService implements CreateUserUseCase {
    private final SaveUserPort saveUserPort;
    private final CheckUserPort checkUserPort;
    private final PasswordEncoderPort passwordEncodePort;
    
    private final long STORAGE_LIMIT = 5L * 1024 * 1024 * 1024; // 5GB

    @Override
    public void execute(CreateUserRequest request) {
        // check nghiệp vụ
        if(checkUserPort.existsByEmail(request.getEmail()))
            throw new DomainException("The email already exists.");
        if(checkUserPort.existsByUserName(request.getUsername()))
            throw new DomainException("The username already exists.");

        // mã hóa mật khẩu
        String hash = passwordEncodePort.encode(request.getPassword());
        
        // create domain user
        User user = User.create(request.getUsername(), request.getEmail(), hash, STORAGE_LIMIT);
        
        // save new user
        saveUserPort.save(user);
    }
    
}
