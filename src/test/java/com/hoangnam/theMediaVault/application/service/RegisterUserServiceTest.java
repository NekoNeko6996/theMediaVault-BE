package com.hoangnam.theMediaVault.application.service;

import com.hoangnam.theMediaVault.application.port.out.*;
import com.hoangnam.theMediaVault.domain.exception.DomainException;
import com.hoangnam.theMediaVault.infrastructure.adapter.in.dto.AuthenticationResponse;
import com.hoangnam.theMediaVault.infrastructure.adapter.in.dto.CreateUserRequest;
import java.util.Date;
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
    @Mock private AuthTokenPort authTokenPort;

    @InjectMocks
    private RegisterUserService registerUserService; // Tự động inject các Mock vào đây

    @Test
@DisplayName("Đăng ký thành công - Luồng chính")
void execute_ShouldReturnResponse_WhenDataIsValid() {
    // 1. Arrange (Chuẩn bị)
    String mockToken = "mocked_jwt_token";
    long expirationTimeMillis = 3600000L; // 1 giờ
    Date mockExpirationDate = new Date(System.currentTimeMillis() + expirationTimeMillis);
    
    CreateUserRequest request = new CreateUserRequest("hoangnam", "test@gmail.com", "password123");
    
    // Giả lập các Port cũ
    when(checkUserPort.existsByEmail(anyString())).thenReturn(false);
    when(checkUserPort.existsByUserName(anyString())).thenReturn(false);
    when(passwordEncodePort.encode(anyString())).thenReturn("hashed_password");
    
    // Cập nhật Mock cho AuthTokenPort mới
    when(authTokenPort.generateToken(anyString())).thenReturn(mockToken);
    
    // Mock hàm getExpiration nhận vào token và trả về đối tượng Date
    when(authTokenPort.getExpiration(mockToken)).thenReturn(mockExpirationDate);

    // 2. Act (Hành động)
    AuthenticationResponse response = registerUserService.execute(request);

    // 3. Assert (Kiểm chứng)
    assertNotNull(response);
    assertEquals(mockToken, response.getAccessToken());
    assertEquals("Bearer", response.getTokenType());

    // 4. Verify
    verify(saveUserPort, times(1)).save(any()); 
    verify(passwordEncodePort).encode("password123");
    verify(authTokenPort).getExpiration(mockToken); // Xác nhận hàm getExpiration đã được gọi
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