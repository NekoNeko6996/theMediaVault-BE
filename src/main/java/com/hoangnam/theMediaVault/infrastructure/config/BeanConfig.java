package com.hoangnam.theMediaVault.infrastructure.config;

import com.hoangnam.theMediaVault.application.port.out.CheckUserPort;
import com.hoangnam.theMediaVault.application.port.out.PasswordEncoderPort;
import com.hoangnam.theMediaVault.application.port.out.SaveUserPort;
import com.hoangnam.theMediaVault.application.service.RegisterUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.hoangnam.theMediaVault.application.port.in.RegisterUserUseCase;
import com.hoangnam.theMediaVault.application.port.out.TokenGeneratorPort;
import com.hoangnam.theMediaVault.application.port.out.GetTokenExpirationPort;

@Configuration
public class BeanConfig {
    
    @Bean
    public RegisterUserUseCase createUserUseCase(
        SaveUserPort saveUserPort, 
        CheckUserPort checkUserPort,
        PasswordEncoderPort passwordEncoderPort,
        TokenGeneratorPort tokenGeneratorPort,
        GetTokenExpirationPort getTokenExpiration) 
    {
        return new RegisterUserService(saveUserPort, checkUserPort, passwordEncoderPort, tokenGeneratorPort, getTokenExpiration);
    }
    
    @Bean
    public PasswordEncoderPort passwordEncoderPort() {
        // Triển khai sử dụng BCrypt của Spring Security
        return raw -> new BCryptPasswordEncoder().encode(raw);
    }
}
