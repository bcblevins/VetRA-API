package com.bcb.vetra.services.vmsintegration.ezyvet;

import com.bcb.vetra.models.Patient;
import com.bcb.vetra.services.vmsintegration.VmsIntegration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

public class EzyVetIntegration implements VmsIntegration {
    private final String BASE_URL = "https://api.trial.ezyvet.com/v1";
    private final String AUTH_PATH = "/oauth/access_token";
    private final String ANIMAL_PATH = "/animal";
    private final String ADD_LIMIT = "?limit=";
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
        setup();
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

    /*
    TODO: implement the following method.
     - First, the existing request is not filling the list properly. I believe it is because there is a nested
     JSON array, it goes like this:
         "meta": {
             stuff I don't want
         },
         "items": {
             patients
         }
     - Need to get email, first, last name from ezyVet, check if email matches a username in VetRA Database
     - If it does, add patient to the database with the correct ownerUsername(email)
     - If it doesn't, create a new user with the email as the username, and add the patient to the database
     */
    // one-time setup
    private void setup() {
        List<Patient> patients = builder.build()
                .get()
                .uri(BASE_URL + ANIMAL_PATH + ADD_LIMIT + 50)
                .header("authorization", "Bearer " + token.getAccessToken())
                .retrieve()
                .bodyToFlux(Patient.class)
                .collectList()
                .block();

        for (Patient patient : patients) {
            System.out.println(patient.getFirstName() + " " + patient.getBirthday() + " " + patient.getSpecies() + " " + patient.getSex() + " " + patient.getOwnerUsername());
        }

    }

    // get new tests

    // get new medications

    // update database

    private void message(String message) {
        System.out.println("EzyVetIntegration: ---------------------------------");
        System.out.println("    " + message);
    }

}
