package com.hoangnam.theMediaVault.application.port.out;

import com.hoangnam.theMediaVault.domain.model.User;
import java.util.Optional;

/**
 *  Định nghĩa các phương thức liên quan đến lấy dữ liệu người dùng
 *  
 */
public interface LoadUserPort {
    Optional<User> findById(String id);
}