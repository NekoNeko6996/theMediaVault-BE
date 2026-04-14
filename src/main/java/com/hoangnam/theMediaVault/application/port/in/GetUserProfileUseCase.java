package com.hoangnam.theMediaVault.application.port.in;

import com.hoangnam.theMediaVault.infrastructure.adapter.in.dto.UserResponse;

/**
 *
 * Định nghĩa USECASE (bản chất là bên hạ tần hay application cần lấy dữ liệu sẽ phải thông qua đây)
 */
public interface GetUserProfileUseCase {
    UserResponse execute(String userId); // Định nghĩa hành động lấy thông tin user
}