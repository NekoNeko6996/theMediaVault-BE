package com.hoangnam.theMediaVault.application.service;

import com.hoangnam.theMediaVault.application.port.out.*;
import com.hoangnam.theMediaVault.domain.exception.DomainException;
import com.hoangnam.theMediaVault.infrastructure.adapter.in.dto.AuthenticationResponse;
import com.hoangnam.theMediaVault.infrastructure.adapter.in.dto.CreateUserRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Kích hoạt Mockito
class RegisterUserServiceTest {

    @Mock private SaveUserPort saveUserPort;
    @Mock private CheckUserPort checkUserPort;
    @Mock private PasswordEncoderPort passwordEncodePort;
    @Mock private TokenGeneratorPort tokenGeneratorPort;
    @Mock private GetTokenExpirationPort getTokenExpiration;

    @InjectMocks
    private RegisterUserService registerUserService; // Tự động inject các Mock vào đây

    @Test
    @DisplayName("Đăng ký thành công - Luồng chính")
    void execute_ShouldReturnResponse_WhenDataIsValid() {
        // Arrange (Chuẩn bị)
        CreateUserRequest request = new CreateUserRequest("hoangnam", "test@gmail.com", "password123");
        
        when(checkUserPort.existsByEmail(anyString())).thenReturn(false);
        when(checkUserPort.existsByUserName(anyString())).thenReturn(false);
        when(passwordEncodePort.encode(anyString())).thenReturn("hashed_password");
        when(tokenGeneratorPort.generateToken(anyString())).thenReturn("mocked_jwt_token");
        when(getTokenExpiration.milisecond()).thenReturn(3600000L); // 1 giờ

        // Act (Hành động)
        AuthenticationResponse response = registerUserService.execute(request);

        // Assert (Kiểm chứng)
        assertNotNull(response);
        assertEquals("mocked_jwt_token", response.getAccessToken());
        assertEquals("Bearer", response.getTokenType());
        assertEquals(3600000L, response.getExpiresIn());

        // Verify (Xác nhận các Port được gọi đúng)
        verify(saveUserPort, times(1)).save(any()); 
        verify(passwordEncodePort).encode("password123");
    }

    @Test
    @DisplayName("Thất bại khi Email đã tồn tại")
    void execute_ShouldThrowException_WhenEmailExists() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest("hoangnam", "exist@gmail.com", "password");
        when(checkUserPort.existsByEmail("exist@gmail.com")).thenReturn(true);

        // Act & Assert
        DomainException exception = assertThrows(DomainException.class, () -> 
            registerUserService.execute(request)
        );

        assertEquals("The email already exists.", exception.getMessage());
        
        // Quan trọng: Phải verify là saveUserPort KHÔNG được gọi
        verify(saveUserPort, never()).save(any());
    }
}