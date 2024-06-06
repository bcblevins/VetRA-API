package com.bcb.vetra.services.vmsintegration.ezyvet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Token {
    private String token_type;
    private String expires_in;
    private String access_token;
}
