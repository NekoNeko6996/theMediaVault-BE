package com.hoangnam.theMediaVault.application.service;

import com.hoangnam.theMediaVault.application.port.out.CheckUserPort;
import com.hoangnam.theMediaVault.application.port.out.SaveUserPort;
import com.hoangnam.theMediaVault.domain.exception.DomainException;
import com.hoangnam.theMediaVault.domain.model.User;
import com.hoangnam.theMediaVault.application.port.in.dto.command.RegisterCommand;
import lombok.RequiredArgsConstructor;
import com.hoangnam.theMediaVault.application.port.in.RegisterUserUseCase;
import com.hoangnam.theMediaVault.application.port.in.dto.result.AuthenticatedIdentity;
import com.hoangnam.theMediaVault.application.port.out.PasswordEncoderPort;
import jakarta.transaction.Transactional;

@RequiredArgsConstructor
public class RegisterUserService implements RegisterUserUseCase {
    private final SaveUserPort saveUserPort;
    private final CheckUserPort checkUserPort;
    private final PasswordEncoderPort passwordEncoderPort;
    
    private final long STORAGE_LIMIT = 5L * 1024 * 1024 * 1024; // 5GB

    @Override
    @Transactional
    public AuthenticatedIdentity execute(RegisterCommand command) {
        command.validate();
        
        // check nghiệp vụ
        if(checkUserPort.existsByEmail(command.getEmail()))
            throw new DomainException("The email already exists.");
        if(checkUserPort.existsByUserName(command.getUsername()))
            throw new DomainException("The username already exists.");

        String hash = passwordEncoderPort.encode(command.getPassword());
        User user = User.create(command.getUsername(), command.getEmail(), hash, STORAGE_LIMIT);
        saveUserPort.save(user);
        
        // trả về 1 domain user nếu đăng ký thành công
        return new AuthenticatedIdentity(user, true);
    }
    
}
