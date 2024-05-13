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
    @NotBlank(message = "First name is required.")
    private String firstName;
    private LocalDate birthday;
    private String species;
    private String sex;
    @NotBlank(message = "Owner username is required.")
    private String ownerUsername;
}
