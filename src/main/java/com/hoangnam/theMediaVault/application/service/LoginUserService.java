package com.hoangnam.theMediaVault.application.service;

import com.hoangnam.theMediaVault.application.port.in.LoginUserUseCase;
import com.hoangnam.theMediaVault.application.port.out.PasswordEncoderPort;
import com.hoangnam.theMediaVault.domain.exception.DomainException;
import com.hoangnam.theMediaVault.domain.model.User;
import com.hoangnam.theMediaVault.application.port.in.dto.result.AuthenticatedIdentity;
import com.hoangnam.theMediaVault.application.port.in.dto.command.LoginCommand;
import com.hoangnam.theMediaVault.application.port.out.UserPort;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class LoginUserService implements LoginUserUseCase {

    private final PasswordEncoderPort passwordEncoderPort;
    private final UserPort userPort;
    

    @Override
    @Transactional
    public AuthenticatedIdentity execute(LoginCommand command) {
        command.validate();
        
        User user = userPort.findByUsername(command.getUsername()).orElseThrow(() -> new DomainException("Invalid username or password."));
        
        if (!passwordEncoderPort.matches(command.getPassword(), user.getPasswordHash())) {
            throw new DomainException("Invalid username or password.");
        }

        if(!user.isAvailable()) throw new DomainException("The user has been suspended or deleted.");
       
        User newUser = user.withLogin();
        userPort.save(newUser);
        
        // trả về 1 domain user nếu đăng nhập thành công
        return new AuthenticatedIdentity(user, false);
    }

}
