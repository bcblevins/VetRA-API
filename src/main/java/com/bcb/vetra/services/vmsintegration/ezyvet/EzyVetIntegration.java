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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Service
public class EzyVetIntegration implements VmsIntegration {
    private final String BASE_URL = "https://api.trial.ezyvet.com/v1";
    private final String AUTH_PATH = "/oauth/access_token";
    private final String ANIMAL_PATH = "/animal";
    private final String CONTACT_PATH = "/contact";
    private final String TEST_PATH = "/diagnosticresult";
    private final String ADD_LIMIT = "?limit=";
    private final String CREATED_AT = "created_at=";
    private final String PATIENTS_UPDATED = "ezyvet patients updated";
    private final String TESTS_UPDATED = "ezyvet tests updated";
    private final String ANSI_BLUE = "\u001B[34m";
    private final String ANSI_RESET = "\u001B[0m";
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
        List<String> userIds = userDao.getEzyVetUserIds();

        // get time of last update in epoch seconds
        long lastUpdated = metaDao.getTimeForAction(PATIENTS_UPDATED).toEpochSecond(ZoneOffset.UTC);

        // get patients for each owner
        for (String userId : userIds) {
            String responseBody = "";
            try {
                responseBody = builder.build()
                        .get()
                        .uri(BASE_URL + ANIMAL_PATH + "?contact_id=" + userId + "&" + CREATED_AT + encodeParameterValue(">", lastUpdated))  // toEpochSecond(ZoneOffset) converts a time to epoch second, assuming UTC timezone.
                        .header("authorization", "Bearer " + token.getAccessToken())
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();
            } catch (WebClientResponseException e) {
                if (e.getStatusCode().toString().contains("401")) {
                    reAuthenticate(this::getNewPatients);
                }
                message(e.getMessage());
            } catch (UnsupportedEncodingException e) {
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
                patientDao.create(patient);
                message("Patient created: " + patient.toString());
            }
        }
        metaDao.setTimeForActionToNow(PATIENTS_UPDATED);
    }

    // get new tests

    private void getNewTests() {
        List<String> patientIds = patientDao.getEzyVetPatientIds();
        long lastUpdated = metaDao.getTimeForAction(TESTS_UPDATED).toEpochSecond(ZoneOffset.UTC);
        String response = "";
        for (String patientId : patientIds) {
            try {
                String url = BASE_URL + TEST_PATH + "?animal_id=" + patientId + "&" + CREATED_AT + encodeParameterValue(">", lastUpdated);
                response = sendGetRequest(url);
            } catch (WebClientResponseException e) {
                if (e.getStatusCode().toString().contains("401")) {
                    reAuthenticate(this::getNewTests);
                }
                message(e.getMessage());
            } catch (UnsupportedEncodingException e) {
                message(e.getMessage());
            }
            try {
                JsonNode root = objectMapper.readTree(response);
                ArrayNode items = (ArrayNode) root.path("items");

                //TODO: DO things here

            } catch (JsonProcessingException e) {
                message("Error parsing test response body. \n" + e.getMessage());
            }
        }
    }

    private void getResults() {

    }

    // get new medications

    private void getNewMedications() {

    }

    //---------------------
    // Helper methods
    //---------------------

    /**
     * Logs a message to the console.
     *
     * @param message
     */
    private void message(String message) {
        System.out.println(LocalDateTime.now() + ANSI_BLUE + "  [--EzyVetIntegration--]:  " + ANSI_RESET + message);
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

    /**
     * Encodes a comparator and value into a URL-encoded string for use in API call parameters.
     *
     * @param comparator (>, <, gt, lt)
     * @param value
     * @return
     * @throws UnsupportedEncodingException
     */
    private String encodeParameterValue(String comparator, long value) throws UnsupportedEncodingException {
        if (comparator.equals(">")) {
            comparator = "gt";
        } else if (comparator.equals("<")) {
            comparator = "lt";
        }
        String JsonString = String.format("{\"%s\":%s}", comparator, value);
        return URLEncoder.encode(JsonString, "UTF-8");
    }

    /**
     * Sends a GET request to the given URL and returns the response body as a string.
     * @param url
     * @return
     */
    private String sendGetRequest(String url) throws WebClientResponseException {
        return builder.build()
                .get()
                .uri(url)
                .header("authorization", "Bearer " + token.getAccessToken())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

}
