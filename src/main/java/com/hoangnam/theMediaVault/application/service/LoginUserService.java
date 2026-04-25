package com.hoangnam.theMediaVault.application.service;

import com.hoangnam.theMediaVault.application.port.in.LoginUserUseCase;
import com.hoangnam.theMediaVault.application.port.out.LoadUserPort;
import com.hoangnam.theMediaVault.application.port.out.PasswordEncoderPort;
import com.hoangnam.theMediaVault.application.port.out.SaveUserPort;
import com.hoangnam.theMediaVault.domain.exception.DomainException;
import com.hoangnam.theMediaVault.domain.model.User;
import com.hoangnam.theMediaVault.application.port.in.dto.result.AuthenticatedIdentity;
import com.hoangnam.theMediaVault.application.port.in.dto.command.LoginCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class LoginUserService implements LoginUserUseCase {

    private final LoadUserPort loadUserPort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final SaveUserPort saveUserPort;
    

    @Override
    @Transactional
    public AuthenticatedIdentity execute(LoginCommand command) {
        command.validate();
        
        User user = loadUserPort.findByUsername(command.getUsername()).orElseThrow(() -> new DomainException("Invalid username or password."));
        
        if (!passwordEncoderPort.matches(command.getPassword(), user.getPasswordHash())) {
            throw new DomainException("Invalid username or password.");
        }

        if(!user.isAvailable()) throw new DomainException("The user has been suspended or deleted.");
       
        User newUser = user.withLogin();
        saveUserPort.save(newUser);
        
        // trả về 1 domain user nếu đăng nhập thành công
        return new AuthenticatedIdentity(user, false);
    }

}
