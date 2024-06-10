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
import java.util.List;

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
    @JsonAlias({"id", "vmsId"})
    private String vmsId;
}
