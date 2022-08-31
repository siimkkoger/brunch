package com.example.brunch.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

    @Value("${spring.security.authentication.jwt.validity}")
    public long tokenValidityInMilliSeconds;
    @Value("${spring.security.authentication.jwt.secret}")
    public String jwtSecret;

    public String createJwtToken(final String username) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + tokenValidityInMilliSeconds))
                .sign(Algorithm.HMAC512(jwtSecret.getBytes()));
    }

    public DecodedJWT decodeJwtToken(final String token) {
        return JWT.require(Algorithm.HMAC512(jwtSecret.getBytes()))
                .build()
                .verify(token);
    }
}