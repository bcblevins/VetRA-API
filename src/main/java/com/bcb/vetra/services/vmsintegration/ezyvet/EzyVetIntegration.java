package com.bcb.vetra.services.vmsintegration.ezyvet;

import com.bcb.vetra.daos.*;
import com.bcb.vetra.models.Patient;
import com.bcb.vetra.models.User;
import com.bcb.vetra.services.vmsintegration.VmsIntegration;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.ArrayList;
import java.util.List;

@Service
public class EzyVetIntegration implements VmsIntegration {
    private final String BASE_URL = "https://api.trial.ezyvet.com/v1";
    private final String AUTH_PATH = "/oauth/access_token";
    private final String ANIMAL_PATH = "/animal";
    private final String CONTACT_PATH = "/contact";
    private final String ADD_LIMIT = "?limit=";
    private WebClient.Builder builder;
    private ObjectMapper objectMapper;
    private Token token;
    private boolean authenticated = false;
    private MessageDao messageDao;
    private PatientDao patientDao;
    private PrescriptionDao prescriptionDao;
    private ResultDao resultDao;
    private TestDao testDao;
    private UserDao userDao;

    public EzyVetIntegration() {
        this.builder = WebClient.builder();
        this.objectMapper = new ObjectMapper();
    }
    public EzyVetIntegration(ObjectMapper objectMapper, WebClient.Builder builder, MessageDao messageDao, PatientDao patientDao, PrescriptionDao prescriptionDao, ResultDao resultDao, TestDao testDao, UserDao userDao) {
        this.objectMapper = objectMapper;
        this.builder = builder;
        this.messageDao = messageDao;
        this.patientDao = patientDao;
        this.prescriptionDao = prescriptionDao;
        this.resultDao = resultDao;
        this.testDao = testDao;
        this.userDao = userDao;
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


    // one-time setup
    private void setup() {
        // get random owners
        String responseBody = builder.build()
                .get()
                .uri(BASE_URL + CONTACT_PATH + ADD_LIMIT + 20 + "&is_customer=1")
                .header("authorization", "Bearer " + token.getAccessToken())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            JsonNode root = objectMapper.readTree(responseBody);                         // A JsonNode is a particular place in a JSON tree. It can be an object, an array, etc.
            ArrayNode items = (ArrayNode) root.path("items");                         // An ArrayNode is a JsonNode that is an array.
            List<User> users = new ArrayList<>();
            for (JsonNode item : items) {
                JsonNode patientNode = item.path("contact");                          // .path(key) returns a JsonNode value for the given key.
                User user = objectMapper.treeToValue(patientNode, User.class);           // treeToValue(JsonNode, Class) converts a JsonNode to a Java object.
                if (user.getFirstName() != null) {
                    user.setUsername(user.getFirstName() + user.getLastName());
                    user.setEmail(user.getUsername() + "@example.com");
                    user.setPassword("password");
                    users.add(user);
                }
            }

        } catch (JsonProcessingException e) {
            message("Error parsing patient response body. \n" + e.getMessage());
        }



    }



    private void getPatients() {
        String responseBody = builder.build()
                .get()
                .uri(BASE_URL + ANIMAL_PATH + ADD_LIMIT + 50)
                .header("authorization", "Bearer " + token.getAccessToken())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            JsonNode root = objectMapper.readTree(responseBody);                         // A JsonNode is a particular place in a JSON tree. It can be an object, an array, etc.
            ArrayNode items = (ArrayNode) root.path("items");                         // An ArrayNode is a JsonNode that is an array.
            List<Patient> patients = new ArrayList<>();
            for (JsonNode item : items) {
                JsonNode patientNode = item.path("animal");                           // .path(key) returns a JsonNode value for the given key.
                Patient patient = objectMapper.treeToValue(patientNode, Patient.class);  // treeToValue(JsonNode, Class) converts a JsonNode to a Java object.
                patients.add(patient);
            }

        } catch (JsonProcessingException e) {
            message("Error parsing patient response body. \n" + e.getMessage());
        }


    }

    // get new tests

    // get new medications

    // update database

    private void message(String message) {
        System.out.println("EzyVetIntegration: ---------------------------------");
        System.out.println("    " + message);
        System.out.println("----------------------------------------------------");
    }

}
