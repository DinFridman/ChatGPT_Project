package com.example.chatgptproject.filter;

import com.example.chatgptproject.model.ApplicationUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;

import static com.example.chatgptproject.security.utils.SecurityConstants.EXPIRATION_TIME;
import static com.example.chatgptproject.security.utils.SecurityConstants.KEY;

@RequiredArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;

    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest req,
            HttpServletResponse res) throws AuthenticationException {

        try {
            ApplicationUser applicationUser =
                    new ObjectMapper().readValue(req.getInputStream(), ApplicationUser.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(applicationUser.getUsername(),
                            applicationUser.getPassword(), new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest req,
            HttpServletResponse res,
            FilterChain chain,
            Authentication auth) throws IOException, ServletException {

        Date exp = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
        Key key = Keys.hmacShaKeyFor(KEY.getBytes());
        Claims claims = Jwts.claims().setSubject(((User) auth.getPrincipal()).getUsername());
        String token = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, key).setExpiration(exp).compact();
        res.addHeader("token", token);

    }
}
