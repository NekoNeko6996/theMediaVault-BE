package com.hoangnam.theMediaVault.application.port.in;

import com.hoangnam.theMediaVault.infrastructure.adapter.in.dto.CreateUserRequest;

/**
 *
 * Hạ tần nói chung hay controller nói riêng muốn tạo user sẽ phải thông qua interface này
 */
public interface CreateUserUseCase {
    void execute(CreateUserRequest request);
}
