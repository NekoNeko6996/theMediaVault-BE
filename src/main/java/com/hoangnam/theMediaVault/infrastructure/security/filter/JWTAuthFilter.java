package com.hoangnam.theMediaVault.infrastructure.security.filter;

import com.hoangnam.theMediaVault.infrastructure.security.model.CustomUserDetail;
import com.hoangnam.theMediaVault.infrastructure.service.JWTService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JWTAuthFilter extends OncePerRequestFilter {
    
    private final UserDetailsService userDetailService;
    private final JWTService jwtService;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String userID = null;
        
        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                userID = jwtService.extractSubject(token);
            } 
            catch(ExpiredJwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write(e.getMessage());
                return;
            }
        }
        
        if(userID != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetail = userDetailService.loadUserByUsername(userID);
            
            if(jwtService.validateToken(token, (CustomUserDetail) userDetail)) {
                UsernamePasswordAuthenticationToken _token = new UsernamePasswordAuthenticationToken(
                        userDetail, 
                        null, 
                        userDetail.getAuthorities()
                );
                
                _token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(_token);
            }
        }
        
        filterChain.doFilter(request, response);
    }
    
}
