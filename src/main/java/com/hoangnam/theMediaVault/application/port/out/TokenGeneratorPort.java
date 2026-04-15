package com.hoangnam.theMediaVault.application.port.out;


public interface TokenGeneratorPort {
    String generateToken(String subject);
}
