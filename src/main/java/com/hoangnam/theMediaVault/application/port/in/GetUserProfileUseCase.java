package com.hoangnam.theMediaVault.application.port.in;

import com.hoangnam.theMediaVault.application.port.in.dto.in.GetUserProfileQuery;
import com.hoangnam.theMediaVault.application.port.in.dto.out.UserProfileResult;

/**
 *
 * Định nghĩa USECASE (bản chất là bên hạ tần hay application cần lấy dữ liệu sẽ phải thông qua đây)
 */
public interface GetUserProfileUseCase {
    UserProfileResult execute(GetUserProfileQuery request);
}