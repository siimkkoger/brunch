package com.example.brunch.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class JwtService {

    @Value("${spring.security.authentication.jwt.validity}")
    public long tokenValidityInMilliSeconds;
    @Value("${spring.security.authentication.jwt.secret}")
    public String jwtSecret;

    public String createJwtToken(final Authentication authentication, final HttpServletRequest request) {
        final User account = (User) authentication.getPrincipal();
        return JWT.create()
                .withSubject(account.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + tokenValidityInMilliSeconds))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", account.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(Algorithm.HMAC512(jwtSecret.getBytes()));
    }

    public DecodedJWT decodeJwtToken(final String token) {
        return JWT.require(Algorithm.HMAC512(jwtSecret.getBytes()))
                .build()
                .verify(token);
    }
}