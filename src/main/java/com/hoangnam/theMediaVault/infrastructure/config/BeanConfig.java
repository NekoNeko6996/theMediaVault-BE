package com.hoangnam.theMediaVault.infrastructure.config;

import com.hoangnam.theMediaVault.application.port.in.CreateFolderUseCase;
import com.hoangnam.theMediaVault.application.port.in.GetFilesUseCase;
import com.hoangnam.theMediaVault.application.port.in.LoginUserUseCase;
import com.hoangnam.theMediaVault.application.port.in.MoveAllToTrashUseCase;
import com.hoangnam.theMediaVault.application.port.out.CheckUserPort;
import com.hoangnam.theMediaVault.application.port.out.SaveUserPort;
import com.hoangnam.theMediaVault.application.service.RegisterUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.hoangnam.theMediaVault.application.port.in.RegisterUserUseCase;
import com.hoangnam.theMediaVault.application.port.in.UploadFilesUseCase;
import com.hoangnam.theMediaVault.application.port.out.FilePersistencePort;
import com.hoangnam.theMediaVault.application.port.out.LoadUserPort;
import com.hoangnam.theMediaVault.application.port.out.PasswordEncoderPort;
import com.hoangnam.theMediaVault.application.port.out.StoragePort;
import com.hoangnam.theMediaVault.application.service.CreateFolderService;
import com.hoangnam.theMediaVault.application.service.GetFilesService;
import com.hoangnam.theMediaVault.application.service.LoginUserService;
import com.hoangnam.theMediaVault.application.service.MoveToTrashService;
import com.hoangnam.theMediaVault.application.service.UploadFilesService;

@Configuration
public class BeanConfig {
    
    @Bean
    public RegisterUserUseCase createUserUseCase(SaveUserPort saveUserPort, CheckUserPort checkUserPort, PasswordEncoderPort passwordEncoderPort) {
        return new RegisterUserService(saveUserPort, checkUserPort, passwordEncoderPort);
    }
    
    @Bean
    public LoginUserUseCase loginUserUseCase(LoadUserPort loadUserPort, PasswordEncoderPort passwordEncoderPort, SaveUserPort saveUserPort) {
        return new  LoginUserService(loadUserPort, passwordEncoderPort, saveUserPort);
    }
    
    @Bean
    public CreateFolderUseCase createFolderUseCase(StoragePort storagePort, LoadUserPort LoadUserPort, FilePersistencePort filePersistencePort) {
        return new CreateFolderService(storagePort, LoadUserPort, filePersistencePort);
    }
    
    @Bean
    public UploadFilesUseCase uploadFilesUseCase(LoadUserPort loadUserPort, FilePersistencePort filePersistencePort, StoragePort storagePort) {
        return new UploadFilesService(loadUserPort, filePersistencePort, storagePort);
    } 
    
    @Bean 
    public MoveAllToTrashUseCase moveAllToTrashUseCase(FilePersistencePort filePresistencePort, LoadUserPort loadUserPort) {
        return new MoveToTrashService(filePresistencePort, loadUserPort);
    } 
    
    @Bean
    public GetFilesUseCase getFilesUseCase(FilePersistencePort filePersistencePort, LoadUserPort loadUserPort) {
        return new GetFilesService(filePersistencePort, loadUserPort);
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
