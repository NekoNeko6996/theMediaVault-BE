package com.hoangnam.theMediaVault.domain.model;

import com.hoangnam.theMediaVault.domain.exception.DomainException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@SpringBootTest
public class EmailTest {
    
    @Test
    @DisplayName("nên tạo được email khi định dang đúng")
    void shouldCreateEmailWhenFormatIsValid() {
        
        // Arrange (tạo dữ liệu bang đầu)
        String validEmail = "hoangnam@themediavault.com";
        
        // Act (Gọi)
        Email email = Email.create(validEmail);
        
        // Assert (kết quả)
        assertEquals(validEmail, email.getValue());
    }
    
    @ParameterizedTest
    @ValueSource(strings = {
        "",                // Trống
        "  ",              // Khoảng trắng
        "test",            // Thiếu @ và domain
        "test@",           // Thiếu domain
        "@domain.com",     // Thiếu local part
        "test@domain",     // Thiếu phần mở rộng (.com, .vn)
        "test@domain..com" // Sai định dạng dấu chấm
    })
    @DisplayName("Nên ném lỗi DomainException khi email sai định dạng")
    void shouldThrowExceptionWhenEmailIsInvalid(String invalidEmail) {
        // Act & Assert
        assertThrows(DomainException.class, () -> Email.create(invalidEmail));
    }
    
    @Test
    @DisplayName("Nên ném lỗi khi email bị null")
    void shouldThrowExceptionWhenEmailIsNull() {
        assertThrows(NullPointerException.class, () -> Email.create(null));
    }
}
