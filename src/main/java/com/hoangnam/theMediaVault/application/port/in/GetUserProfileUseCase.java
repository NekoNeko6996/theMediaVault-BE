package com.hoangnam.theMediaVault.application.port.in;

import com.hoangnam.theMediaVault.infrastructure.adapter.in.dto.LoadUserResponse;

/**
 *
 * 
 */
public interface GetUserProfileUseCase {
    LoadUserResponse execute(String userId); // Định nghĩa hành động lấy thông tin user
}