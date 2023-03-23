package com.example.chatgptproject.security.jwt;

import com.example.chatgptproject.security.SecurityConstants;
import io.jsonwebtoken.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import java.util.Date;
import java.util.function.Function;

import static com.example.chatgptproject.security.SecurityConstants.JWT_EXPIRATION;

@Log4j2
@RequiredArgsConstructor
@Component
public class JWTUtils {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.cookieName}")
    private String jwtCookie;

    public String getJwtFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtCookie);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }

    public ResponseCookie generateJwtCookie(UserDetails userPrincipal) {
        String jwt = generateToken(userPrincipal.getUsername());
        ResponseCookie cookie = ResponseCookie
                .from(jwtCookie, jwt)
                .path("/api")
                .maxAge(JWT_EXPIRATION)
                .httpOnly(true)
                .build();
        return cookie;
    }

    public String generateToken(String username) {
        Date currentDate = new Date();
        Date expiredDate = new Date(currentDate.getTime() + JWT_EXPIRATION);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(currentDate)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS512,secret)
                .compact();
    }

    public String getUsernameFromToken(String token) {return getClaimFromToken(token, Claims::getSubject);}

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public Boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}
