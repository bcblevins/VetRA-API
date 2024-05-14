package com.bcb.vetra.models;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Model class for a prescription.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Prescription {
    private int prescriptionId;
    @NotBlank(message = "Prescription must have a medication name.")
    private String name;
    @NotBlank(message = "Prescription must have a quantity.")
    private double quantity;
    @NotBlank(message = "Prescription must have instructions.")
    private String instructions;
    @NotBlank(message = "Prescription must have a refill count.")
    private int refills;
    private boolean isActive;
    @NotBlank(message = "Prescription must have a patientId.")
    private int patientId;
    @NotBlank(message = "Prescription must have a doctorUsername.")
    private String doctorUsername;
}
