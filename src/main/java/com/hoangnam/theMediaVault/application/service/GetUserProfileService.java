package com.hoangnam.theMediaVault.application.service;

import com.hoangnam.theMediaVault.application.port.in.GetUserProfileUseCase;
import com.hoangnam.theMediaVault.application.port.out.LoadUserPort;
import com.hoangnam.theMediaVault.domain.model.User;
import com.hoangnam.theMediaVault.infrastructure.adapter.in.dto.LoadUserResponse;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class GetUserProfileService implements GetUserProfileUseCase {
    private final LoadUserPort loadUserPort;

    @Override
    public LoadUserResponse execute(String userId) {
        // 1. Gọi Adapter thông qua Port
        User user = loadUserPort.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Xử lý logic nghiệp vụ (Nghiệp vụ này nằm trong Model)
        // boolean isEligibleForPremium = user.getUsedStorage() > (user.getStorageLimit() * 0.9);
        
        // 3. Trả về kết quả (DTO)
        return LoadUserResponse.fromDomain(user);
    }
}