package com.hoangnam.theMediaVault.infrastructure.config;

import com.hoangnam.theMediaVault.application.port.in.CreateUserUseCase;
import com.hoangnam.theMediaVault.application.port.out.CheckUserPort;
import com.hoangnam.theMediaVault.application.port.out.PasswordEncoderPort;
import com.hoangnam.theMediaVault.application.port.out.SaveUserPort;
import com.hoangnam.theMediaVault.application.service.CreateUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class BeanConfig {
    
    @Bean
    public CreateUserUseCase createUserUseCase(
        SaveUserPort saveUserPort, 
        CheckUserPort checkUserPort,
        PasswordEncoderPort passwordEncoderPort
    ) {
        return new CreateUserService(saveUserPort, checkUserPort, passwordEncoderPort);
    }
    
    @Bean
    public PasswordEncoderPort passwordEncoderPort() {
        // Triển khai sử dụng BCrypt của Spring Security
        return raw -> new BCryptPasswordEncoder().encode(raw);
    }
}
