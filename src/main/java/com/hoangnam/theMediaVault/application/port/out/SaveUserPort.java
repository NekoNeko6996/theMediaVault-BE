package com.hoangnam.theMediaVault.application.port.out;

import com.hoangnam.theMediaVault.domain.model.User;

/**
 *
 * 
 */
public interface SaveUserPort {
    void save(User user);
}
