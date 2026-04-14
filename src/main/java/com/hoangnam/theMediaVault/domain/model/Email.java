package com.hoangnam.theMediaVault.domain.model;

import com.hoangnam.theMediaVault.domain.exception.DomainException;
import java.util.regex.Pattern;
import lombok.Value;

@Value
public class Email {
    
    private static final String REGEX = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    private static final Pattern PATTERN = Pattern.compile(REGEX, Pattern.CASE_INSENSITIVE);
    
    String value;
    
    private Email(String email) {
        this.value = email;
    }
    
    public static Email create(String email) throws ExceptionInInitializerError {
        if(!isValid(email)) throw new DomainException(String.format("Invalid email '%s'.", email));
        
        return new Email(email);
    } 
    
    public static boolean isValid(String email) throws ExceptionInInitializerError {
        return !(email.isBlank() || email.isEmpty() || !PATTERN.matcher(email).matches());
    } 
}
