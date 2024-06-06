package com.bcb.vetra.services.vmsintegration.ezyvet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthRequestBody {
    private String partner_id;
    private String client_id;
    private String client_secret;
    private String grant_type;
    private String scope;

    public String toString() {
        return "partner_id: " + partner_id + ", client_id: " + client_id + ", client_secret: " + client_secret + ", grant_type: " + grant_type + ", scope: " + scope;
    }
}
