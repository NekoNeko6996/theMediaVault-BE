package com.hoangnam.theMediaVault.infrastructure.service;

import com.hoangnam.theMediaVault.application.port.out.LoadUserPort;
import com.hoangnam.theMediaVault.infrastructure.security.model.CustomUserDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final LoadUserPort loadUserPort;

    @Override
    public UserDetails loadUserByUsername(String userID) throws UsernameNotFoundException {
        return loadUserPort.findById(userID).map(CustomUserDetail::new).orElseThrow(() -> new UsernameNotFoundException("User not found with id" + userID));
    }
}
