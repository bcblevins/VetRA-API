package com.bcb.vetra.models;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Model class for doctors and pet owners.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    private int id;
    @NotBlank(message = "Person must have a first name.")
    private String firstName;
    @NotBlank(message = "Person must have a last name.")
    private String lastName;
    private boolean isDoctor;
}
