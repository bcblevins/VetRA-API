package com.bcb.vetra.services.vmsintegration.ezyvet;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Token {
    @JsonAlias({"token_type", "tokenType"})
    private String tokenType;
    @JsonAlias({"expires_in", "expiresIn"})
    private String expiresIn;
    @JsonAlias({"access_token", "accessToken"})
    private String accessToken;
}
