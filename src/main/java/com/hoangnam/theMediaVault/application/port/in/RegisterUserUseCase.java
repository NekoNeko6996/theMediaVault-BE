package com.hoangnam.theMediaVault.application.port.in;

import com.hoangnam.theMediaVault.application.port.in.dto.result.AuthenticatedIdentity;
import com.hoangnam.theMediaVault.application.port.in.dto.command.RegisterCommand;

/**
 *
 * Hạ tần nói chung hay controller nói riêng muốn tạo user sẽ phải thông qua interface này
 */
public interface RegisterUserUseCase {
    AuthenticatedIdentity execute(RegisterCommand command);
}
