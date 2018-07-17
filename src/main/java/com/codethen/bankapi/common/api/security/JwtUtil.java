package com.codethen.bankapi.common.api.security;

import com.codethen.bankapi.common.api.errors.AuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.eclipse.jetty.http.HttpHeader;
import spark.Request;

import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class JwtUtil {

    private static final long DEFAULT_TOKEN_LIFETIME_IN_MILLIS = TimeUnit.DAYS.toMillis(30);
    private static final Key apiKey = MacProvider.generateKey(); // TODO: Define in config

    public static String generateJWT(String username) {

        return Jwts.builder()
            .setSubject(username)
            .signWith(SignatureAlgorithm.HS256, apiKey)
            .setExpiration(new Date(System.currentTimeMillis() + DEFAULT_TOKEN_LIFETIME_IN_MILLIS))
            .compact();
    }

    public static final String AUTH_SCHEMA = "Bearer";

    public static AuthUser getAuthUser(Request req) {

        final String authHeader = req.headers(HttpHeader.AUTHORIZATION.toString());

        if (authHeader == null || !authHeader.startsWith(AUTH_SCHEMA)) {
            throw new AuthenticationException();
        }

        final String jwt = authHeader.substring(AUTH_SCHEMA.length() + 1);
        final Claims claims = JwtUtil.parseClaims(jwt);
        final String username = claims.getSubject();

        return new AuthUser(username);
    }

    public static Claims parseClaims(String jwt) {

        return Jwts.parser()
            .setSigningKey(DatatypeConverter.parseBase64Binary(DatatypeConverter.printBase64Binary(apiKey.getEncoded())))
            .parseClaimsJws(jwt)
            .getBody();

    }
}
