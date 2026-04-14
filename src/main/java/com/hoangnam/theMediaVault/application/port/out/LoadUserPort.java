package com.hoangnam.theMediaVault.application.port.out;

import com.hoangnam.theMediaVault.domain.model.User;
import java.util.Optional;

/**
 *  Định nghĩa các phương thức liên quan đến tương tác dữ liệu ở hạ tần(từ DB thông qua apdapter)
 *  Bản chất là Domain muốn lấy dữ liệu từ hạ tần nói chung hay DB nói riêng thì phải thông qua đây
 */
public interface LoadUserPort {
    Optional<User> findById(String id);
}