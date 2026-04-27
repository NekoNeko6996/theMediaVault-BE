package com.hoangnam.theMediaVault.application.port.out;

import com.hoangnam.theMediaVault.domain.model.User;
import java.util.Optional;

/**
 *
 * 
 */
public interface UserPort {
    // check
    boolean existsByEmail(String email);
    boolean existsByUserName(String username);
    
    // load
    Optional<User> findById(String id);
    Optional<User> findByUsername(String username);
    
    // save
    void save(User user);
}
