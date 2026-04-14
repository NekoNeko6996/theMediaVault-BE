package com.hoangnam.theMediaVault.domain.model;

import com.hoangnam.theMediaVault.domain.exception.DomainException;
import lombok.Value;

@Value
public class UserName {
    static final int MIN_LENGTH = 5;
    static final int MAX_LENGTH = 50;
    
    String value;
    
    private UserName(String value) {
        this.value = value;
    }
    
    public static UserName create(String username) {
        if (username == null || !isValid(username)) 
            throw new DomainException(
                String.format("Invalid username '%s', username must be long from %d to %d character.", 
                username, MIN_LENGTH, MAX_LENGTH)
            );
        return new UserName(username);
    }

    public static boolean isValid(String username) {
        if (username == null) return false;
        int length = username.length();
        return length >= MIN_LENGTH && length <= MAX_LENGTH;
    }
}
