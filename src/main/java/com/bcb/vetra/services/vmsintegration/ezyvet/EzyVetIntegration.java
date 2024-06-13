package com.bcb.vetra.services.vmsintegration.ezyvet;

import com.bcb.vetra.daos.*;
import com.bcb.vetra.models.Patient;
import com.bcb.vetra.models.User;
import com.bcb.vetra.services.vmsintegration.VmsIntegration;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.nimbusds.jose.shaded.gson.JsonArray;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Service
public class EzyVetIntegration implements VmsIntegration {
    private final String BASE_URL = "https://api.trial.ezyvet.com/v1";
    private final String AUTH_PATH = "/oauth/access_token";
    private final String ANIMAL_PATH = "/animal";
    private final String CONTACT_PATH = "/contact";
    private final String ADD_LIMIT = "?limit=";
    private final String CREATED_AFTER = "created_at>";
    private final String EZYVET_PATIENTS_UPDATED = "ezyvet patients updated";
    private WebClient.Builder builder;
    private ObjectMapper objectMapper;
    private Token token;
    private boolean isAuthenticated = false;
    private boolean isSetup = false;
    private boolean isDisabled = false;
    private PatientDao patientDao;
    private PrescriptionDao prescriptionDao;
    private ResultDao resultDao;
    private TestDao testDao;
    private UserDao userDao;
    private MetaDao metaDao;

    public EzyVetIntegration(ObjectMapper objectMapper, WebClient.Builder builder, PatientDao patientDao, PrescriptionDao prescriptionDao, ResultDao resultDao, TestDao testDao, UserDao userDao, MetaDao metaDao) {
        this.objectMapper = objectMapper;
        this.builder = builder;
        this.patientDao = patientDao;
        this.prescriptionDao = prescriptionDao;
        this.resultDao = resultDao;
        this.testDao = testDao;
        this.userDao = userDao;
        this.metaDao = metaDao;
    }

    @Override
    public int updateDB() {

        if (!isAuthenticated) {
            getAccessToken();
            if (isDisabled) {
                return 0;
            }
        }
        if (!isSetup) {
            setup();
        }

        getNewPatients();


        isSetup = true;
        return 1;
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
            message(e.getMessage());
            message("Fatal error: Could not authenticate with EzyVet API. Exiting.");
            isDisabled = true;
            return;
        }
        message("Successfully authenticated.");
        this.isAuthenticated = true;
    }


    // one-time setup
    private void setup() {
        List<User> users = new ArrayList<>();
        // get random owners
        String responseBody = "";
        try {
            responseBody = builder.build()
                    .get()
                    .uri(BASE_URL + CONTACT_PATH + ADD_LIMIT + 20 + "&is_customer=1")
                    .header("authorization", "Bearer " + token.getAccessToken())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientResponseException e) {
            if (e.getStatusCode().toString().contains("401")) {
                reAuthenticate(this::setup);
            }
            message(e.getMessage());
        }

        try {
            JsonNode root = objectMapper.readTree(responseBody);                         // A JsonNode is a particular place in a JSON tree. It can be an object, an array, etc.
            ArrayNode items = (ArrayNode) root.path("items");                         // An ArrayNode is a JsonNode that is an array.
            message("The following owners have been retrieved:");
            List<String> ownerNames = new ArrayList<>();                                 // Used to track duplicates.
            for (JsonNode item : items) {
                JsonNode contactNode = item.path("contact");                          // .path(key) returns a JsonNode value for the given key.
                User user = objectMapper.treeToValue(contactNode, User.class);           // treeToValue(JsonNode, Class) converts a JsonNode to a Java object.
                JsonNode vmsIdNode = contactNode.path("id");
                user.addVmsId("ezyVet", vmsIdNode.asText());
                if (!ownerNames.contains(user.getFirstName()) && !user.getFirstName().isEmpty()) {
                    ownerNames.add(user.getFirstName());
                    user.setUsername(user.getFirstName() + user.getLastName() + vmsIdNode.asText());
                    user.setEmail(user.getUsername() + "@example.com");
                    user.setPassword("password");
                    users.add(user);
                }
            }
        } catch (JsonProcessingException e) {
            message("Error parsing patient response body. \n" + e.getMessage());
        }

        for (User user : users) {
            userDao.createUser(user);
            userDao.attributeVmsIdToUser(user.getUsername(), user.getVmsIds());
            message("User created: " + user.toString());
        }


    }

    private void getNewPatients() {
        Map<String, List<Patient>> patients = new HashMap<>();

        // get list of owner ids from vms table
        List<String> userIds = userDao.getEzyVetIds();


        //TODO: Monster URL on line 167 is not returning desired result. API says cannot use expression created_at>0. Learn how to use.
        // get patients from ezyVet for each user
        for (String userId : userIds) {
            String responseBody = "";
            try {
                responseBody = builder.build()
                        .get()
                        .uri(BASE_URL + ANIMAL_PATH + "?contact_id=" + userId + "&" + CREATED_AFTER + metaDao.getTimeForAction(EZYVET_PATIENTS_UPDATED).toEpochSecond(ZoneOffset.UTC))  // toEpochSecond(ZoneOffset) converts a time to epoch second, assuming UTC timezone.
                        .header("authorization", "Bearer " + token.getAccessToken())
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();
            } catch (WebClientResponseException e) {
                if (e.getStatusCode().toString().contains("401")) {
                    reAuthenticate(this::getNewPatients);
                }
                message(e.getMessage());
            }
            try {
                JsonNode root = objectMapper.readTree(responseBody);
                ArrayNode items = (ArrayNode) root.path("items");
                for (JsonNode item : items) {
                    JsonNode patientNode = item.path("animal");
                    Patient patient = objectMapper.treeToValue(patientNode, Patient.class);
                    JsonNode vmsIdNode = patientNode.path("id");
                    patient.addVmsId("ezyVet", vmsIdNode.asText());
                    if (patients.containsKey(userId)) {
                        patients.get(userId).add(patient);
                    } else {
                        patients.put(userId, new ArrayList<>(Arrays.asList(patient)));
                    }
                }
            } catch (JsonProcessingException e) {
                message("Error parsing patient response body. \n" + e.getMessage());
            }

        }

        // add patients to database
        for (Map.Entry<String, List<Patient>> patientEntry : patients.entrySet()) {
            String username = userDao.getUsernameByVmsId(patientEntry.getKey(), "ezyvet");
            for (Patient patient : patientEntry.getValue()) {
                patient.setOwnerUsername(username);
                patient = patientDao.create(patient);
                patientDao.attributeVmsIdToPatient(patient.getPatientId(), patient.getVmsIds());

            }
        }


    }

    // get new tests

    private void getNewTests() {

    }

    // get new medications

    // update database

    private void message(String message) {
        System.out.println(LocalDateTime.now() + "  [--EzyVetIntegration--]:  " + message);
    }

    /**
     * Used after HTTP requests if response code is 4xx. Obtains a new access token and reruns the given method.
     *
     * @param method
     */
    private void reAuthenticate(Runnable method) {
        isAuthenticated = false;
        getAccessToken();
        method.run();
    }

}
