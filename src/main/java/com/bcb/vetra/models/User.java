package com.bcb.vetra.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class for doctors and pet owners.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @NotBlank(message = "User must have a username.")
    private String username;
    @NotBlank(message = "User must have a password.")
    private String password;
    @NotBlank(message = "User must have a first name.")
    private String firstName;
    @NotBlank(message = "User must have a last name.")
    private String lastName;
    private List<String> roles;
}
