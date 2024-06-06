package com.bcb.vetra.services.vmsintegration.ezyvet;

import com.bcb.vetra.services.vmsintegration.VmsIntegration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

public class EzyVetIntegration implements VmsIntegration {
    private final String BASE_URL = "https://api.trial.ezyvet.com/v1";
    private final String AUTH_PATH = "/oauth/access_token";
    private WebClient.Builder builder;
    private Token token;
    private boolean authenticated = false;

    public EzyVetIntegration() {
        this.builder = WebClient.builder();
    }

    @Override
    public int updateDB() {
        if (!authenticated) {
            getAccessToken();
        }

        return 0;
    }

    // get access token
    private void getAccessToken() {
        AuthRequestBody authRequestBody = new AuthRequestBody(
                System.getenv("PARTNER_ID"),
                System.getenv("CLIENT_ID"),
                System.getenv("CLIENT_SECRET"),
                System.getenv("GRANT_TYPE"),
                System.getenv("SCOPE")
        );

        try {
            this.token = builder.build()
                    .post()
                    .uri(BASE_URL + AUTH_PATH)
                    .bodyValue(authRequestBody)
                    .retrieve()
                    .bodyToMono(Token.class)
                    .block();
        } catch (WebClientResponseException e) {
            System.out.println(e.getMessage());
        }
        message("Successfully authenticated.");
        this.authenticated = true;
    }

    // one-time setup
    private void setup() {

    }

    // get new tests

    // get new medications

    // update database

    private void message(String message) {
        System.out.println("EzyVetIntegration: ---------------------------------");
        System.out.println("    " + message);
    }

}
