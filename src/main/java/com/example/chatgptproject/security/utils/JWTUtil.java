package com.example.chatgptproject.security.utils;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTUtil {
    /*private String secret=AuthenticationConfigConstants.SECRET;


    public String generateToken(String username) throws IllegalArgumentException,
            JWTCreationException {
        try {
            return JWT.create()
                    .withSubject(AuthenticationConfigConstants.SUBJECT)
                    .withClaim("username", username)
                    .withIssuedAt(new Date())
                    .withIssuer(AuthenticationConfigConstants.ISSUER_NAME)
                    .sign(Algorithm.HMAC256(secret));
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
        catch (JWTCreationException e) {
            return e.getMessage();
        }
    }
    public String validateTokenAndRetrieveSubject(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject(AuthenticationConfigConstants.SUBJECT)
                .withIssuer(AuthenticationConfigConstants.ISSUER_NAME)
                .build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("username").asString();
    }*/
}
