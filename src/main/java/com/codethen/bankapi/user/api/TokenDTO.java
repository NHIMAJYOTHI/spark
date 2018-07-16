package com.codethen.bankapi.user.api;

public class TokenDTO {

    public final String jwt;

    public TokenDTO(String jwt) {
        this.jwt = jwt;
    }
}
