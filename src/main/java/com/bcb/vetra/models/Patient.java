package com.bcb.vetra.models;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Model class for patient record.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
    private int patientId;
    @NotBlank(message = "Chart number is required.")
    private String chartNumber;
    @NotBlank(message = "First name is required.")
    private String firstName;
    @NotBlank(message = "Last name is required.")
    private String lastName;
    private LocalDate birthday;
    private String species;
    private String sex;
    @NotBlank(message = "Owner username is required.")
    private String ownerUsername;
}
