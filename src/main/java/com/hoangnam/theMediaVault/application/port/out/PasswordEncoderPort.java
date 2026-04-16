package com.hoangnam.theMediaVault.application.port.out;

/**
 *
 * 
 */
public interface PasswordEncoderPort {
    String encode(String rawPassword);
    Boolean matches(String raw, String endcoded);
}
