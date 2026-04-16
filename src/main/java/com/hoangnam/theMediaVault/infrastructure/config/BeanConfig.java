package com.hoangnam.theMediaVault.infrastructure.config;

import com.hoangnam.theMediaVault.application.port.in.LoginUserUseCase;
import com.hoangnam.theMediaVault.application.port.out.CheckUserPort;
import com.hoangnam.theMediaVault.application.port.out.SaveUserPort;
import com.hoangnam.theMediaVault.application.service.RegisterUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.hoangnam.theMediaVault.application.port.in.RegisterUserUseCase;
import com.hoangnam.theMediaVault.application.port.out.AuthTokenPort;
import com.hoangnam.theMediaVault.application.port.out.LoadUserPort;
import com.hoangnam.theMediaVault.application.port.out.PasswordEncoderPort;
import com.hoangnam.theMediaVault.application.service.LoginUserService;

@Configuration
public class BeanConfig {
    
    @Bean
    public RegisterUserUseCase createUserUseCase(
            SaveUserPort saveUserPort, 
            CheckUserPort checkUserPort,
            PasswordEncoderPort passwordEncoderPort,
            AuthTokenPort authTokenPort) 
    {
        return new RegisterUserService(saveUserPort, checkUserPort, passwordEncoderPort, authTokenPort);
    }
    
    @Bean
    public LoginUserUseCase loginUserUseCase(
            LoadUserPort loadUserPort,
            AuthTokenPort authTokenPort,
            PasswordEncoderPort passwordEncoderPort,
            SaveUserPort saveUserPort) 
    {
        return new  LoginUserService(loadUserPort, authTokenPort, passwordEncoderPort, saveUserPort);
    }
    
    @Bean
    public PasswordEncoderPort passwordEncoderPort() {
        BCryptPasswordEncoder bCryptEncoder = new BCryptPasswordEncoder();
        
        /**
         * Do interface có nhiều hơn 1 hàm nên không thể dùng lambdar func được mà phải khai báo cho cả interface(cả 2 func bên trong)
         * Bản chất đây hàm dùng @Bean này vẫn là 1 adapter nhưng nằm trong BeanConfig
         * nếu 1 adapter trong @Bean mà dài quá 10 dòng code hoặc inject phức tạp, gọi db các kiểu thì nên có 1 file adapter riêng
         */
        return new PasswordEncoderPort() {
            @Override
            public String encode(String rawPassword) {
                return bCryptEncoder.encode(rawPassword);
            }

            @Override
            public Boolean matches(String raw, String endcoded) {
                return bCryptEncoder.matches(raw, endcoded);
            }
        };
    }
}
