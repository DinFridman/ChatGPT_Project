package com.example.chatgptproject.security.jwt;

import com.example.chatgptproject.security.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Log4j2
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired//TODO: fix autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;
    @Autowired
    private JWTUtils jwtUtils;

    private String getJWTFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer "))
            return bearerToken.substring(7);
        else {
            log.warn("JWT Token does not begin with Bearer String");
            return null;
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = getJWTFromRequest(request);
        String username = null;

        if(StringUtils.hasText(token) && jwtUtils.validateToken(token)) {
            username = jwtUtils.getUsernameFromToken(token);
            UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails,
                            userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource()
                    .buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        filterChain.doFilter(request, response);
    }
}
