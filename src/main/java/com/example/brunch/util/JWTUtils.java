package com.example.brunch.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.brunch.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class JWTUtils {

    @Value("${spring.security.authentication.jwt.validity}")
    public long tokenValidityInMilliSeconds;
    @Value("${spring.security.authentication.jwt.secret}")
    public String jwtSecret;
    private final SecurityUtils securityUtils;
    public final String AUTHENTICATION_SCHEME_BEARER = "Bearer";

    public JWTUtils(SecurityUtils securityUtils) {
        this.securityUtils = securityUtils;
    }

    public String createJwtToken(final String username) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + tokenValidityInMilliSeconds))
                .sign(Algorithm.HMAC512(jwtSecret.getBytes()));
    }

    public DecodedJWT decodeJwtToken(final String token) {
        return JWT.require(Algorithm.HMAC512(jwtSecret.getBytes()))
                .build()
                .verify(token.replace(AUTHENTICATION_SCHEME_BEARER, ""));
    }

    public String addBearerPrefixToJwtToken(final String token) {
        return AUTHENTICATION_SCHEME_BEARER + " " + token;
    }

    public UsernamePasswordAuthenticationToken convert(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null) {
            return null;
        }
        header = header.trim();
        if (!StringUtils.startsWithIgnoreCase(header, AUTHENTICATION_SCHEME_BEARER)) {
            return null;
        }
        if (header.equalsIgnoreCase(AUTHENTICATION_SCHEME_BEARER)) {
            throw new BadCredentialsException("Empty basic authentication token");
        }
        DecodedJWT jwt = decodeJwtToken(header.substring(6));

        final String username = jwt.getSubject();
        if (username != null) {
            authentication = securityUtils.getAuthentication(username);
        }

        UsernamePasswordAuthenticationToken result = UsernamePasswordAuthenticationToken
                .unauthenticated(token.substring(0, delim), token.substring(delim + 1));
        result.setDetails(this.authenticationDetailsSource.buildDetails(request));
        return result;
    }

}