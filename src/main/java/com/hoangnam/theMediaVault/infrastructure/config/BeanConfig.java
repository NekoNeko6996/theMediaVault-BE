package com.hoangnam.theMediaVault.infrastructure.config;

import com.hoangnam.theMediaVault.application.port.in.CreateFolderUseCase;
import com.hoangnam.theMediaVault.application.port.in.GetFilesUseCase;
import com.hoangnam.theMediaVault.application.port.in.LoginUserUseCase;
import com.hoangnam.theMediaVault.application.port.in.MoveAllToTrashUseCase;
import com.hoangnam.theMediaVault.application.service.RegisterUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.hoangnam.theMediaVault.application.port.in.RegisterUserUseCase;
import com.hoangnam.theMediaVault.application.port.in.RenameFileUseCase;
import com.hoangnam.theMediaVault.application.port.in.UploadFilesUseCase;
import com.hoangnam.theMediaVault.application.port.out.FileAndUserTransactionPort;
import com.hoangnam.theMediaVault.application.port.out.FilePersistencePort;
import com.hoangnam.theMediaVault.application.port.out.PasswordEncoderPort;
import com.hoangnam.theMediaVault.application.port.out.StoragePort;
import com.hoangnam.theMediaVault.application.service.CreateFolderService;
import com.hoangnam.theMediaVault.application.service.GetFilesService;
import com.hoangnam.theMediaVault.application.service.LoginUserService;
import com.hoangnam.theMediaVault.application.service.MoveToTrashService;
import com.hoangnam.theMediaVault.application.service.RenameFileService;
import com.hoangnam.theMediaVault.application.service.UploadFilesService;
import com.hoangnam.theMediaVault.application.port.out.UserPort;
import com.hoangnam.theMediaVault.application.service.CheckFilesCanUploadService;
import com.hoangnam.theMediaVault.application.port.in.CheckFilesCanUploadUseCase;
import com.hoangnam.theMediaVault.application.port.in.GetDownloadUrlUseCase;
import com.hoangnam.theMediaVault.application.port.in.GetTrashFilesUseCase;
import com.hoangnam.theMediaVault.application.port.in.MoveFilesUseCase;
import com.hoangnam.theMediaVault.application.port.in.RestoreFilesFromTrashUseCase;
import com.hoangnam.theMediaVault.application.port.in.ToggleStarredFileUseCase;
import com.hoangnam.theMediaVault.application.service.GetDownloadUrlService;
import com.hoangnam.theMediaVault.application.service.GetTrashFilesService;
import com.hoangnam.theMediaVault.application.service.MoveFilesService;
import com.hoangnam.theMediaVault.application.service.RestoreFilesFromTrashService;
import com.hoangnam.theMediaVault.application.service.ToggleStarredFileService;

@Configuration
public class BeanConfig {
    
    @Bean
    public RegisterUserUseCase createUserUseCase(UserPort userPort, PasswordEncoderPort passwordEncoderPort) {
        return new RegisterUserService(userPort, passwordEncoderPort);
    }
    
    @Bean
    public LoginUserUseCase loginUserUseCase(UserPort userPort, PasswordEncoderPort passwordEncoderPort) {
        return new LoginUserService(passwordEncoderPort, userPort);
    }
    
    @Bean
    public CreateFolderUseCase createFolderUseCase(UserPort userPort, FilePersistencePort filePersistencePort) {
        return new CreateFolderService(userPort, filePersistencePort);
    }
    
    @Bean
    public UploadFilesUseCase uploadFilesUseCase(UserPort userPort, FilePersistencePort filePersistencePort, StoragePort storagePort, FileAndUserTransactionPort fileAndUserTransactionPort) {
        return new UploadFilesService(filePersistencePort, storagePort, userPort, fileAndUserTransactionPort);
    } 
    
    @Bean 
    public MoveAllToTrashUseCase moveAllToTrashUseCase(FilePersistencePort filePresistencePort) {
        return new MoveToTrashService(filePresistencePort);
    } 
    
    @Bean
    public GetFilesUseCase getFilesUseCase(FilePersistencePort filePersistencePort, UserPort userPort) {
        return new GetFilesService(filePersistencePort);
    }
    
    @Bean 
    public RenameFileUseCase renameFileUseCase(FilePersistencePort filePersistencePort) {
        return new RenameFileService(filePersistencePort);
    }
    
    
    @Bean
    public CheckFilesCanUploadUseCase checkFilesExistsUseCase(FilePersistencePort filePersistencePort, UserPort userPort) {
        return new CheckFilesCanUploadService(filePersistencePort, userPort);
    }
    
    @Bean
    public MoveFilesUseCase moveFilesUseCase(FilePersistencePort filePersistencePort) {
        return new MoveFilesService(filePersistencePort);
    }
    
    @Bean
    public ToggleStarredFileUseCase toggleStarredFileUseCase(FilePersistencePort filePersistencePort) {
        return new ToggleStarredFileService(filePersistencePort);
    }
    
    @Bean
    public RestoreFilesFromTrashUseCase restoreFilesFromTrashUseCase(FilePersistencePort filePersistencePort) {
        return new RestoreFilesFromTrashService(filePersistencePort);
    }
    
    @Bean
    public GetDownloadUrlUseCase getDownloadUrlUseCase(FilePersistencePort filePersistencePort, StoragePort storagePort) {
        return new GetDownloadUrlService(filePersistencePort, storagePort);
    }
    
    @Bean
    public GetTrashFilesUseCase getTrashFilesUseCase(FilePersistencePort filePersistencePort) {
        return new GetTrashFilesService(filePersistencePort);
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
