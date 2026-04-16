package com.hoangnam.theMediaVault.application.port.out;

import java.util.Date;

/**
 *
 * 
 */
public interface AuthTokenPort {
    String getSubject(String token);
    Date getExpiration(String token);
    String generateToken(String subject);
}
