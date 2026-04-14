package com.hoangnam.theMediaVault.application.port.out;

/**
 *
 * 
 */
public interface CheckUserPort {
    boolean existsByEmail(String email);
    boolean existsByUserName(String username);
}
