package com.bcb.vetra.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Model class for users.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    @NotBlank(message = "User must have a username.")
    private String username;
    @NotBlank(message = "User must have a password.")
    private String password;
    @NotBlank(message = "User must have a first name.")
    @JsonAlias({"first_name", "firstName"})
    private String firstName;
    @NotBlank(message = "User must have a last name.")
    @JsonAlias({"last_name", "lastName"})
    private String lastName;
    @NotBlank(message = "User must have an email.")
    private String email;
    private Map<String, String> vmsIds;

    public User(String username, String password, String firstName, String lastName, String email) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", vmsId='" + vmsIds.toString() + '\'' +
                '}';
    }

    public void addVmsId(String vmsName, String vmsId) {
        if (vmsIds == null) {
            vmsIds = new HashMap<>();
        }
        vmsIds.put(vmsName, vmsId);
    }
}
