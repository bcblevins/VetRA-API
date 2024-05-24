package com.bcb.vetra.viewmodels;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Model class for a prescription with medication information. Will be used to display prescriptions.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionWithMedication {
    private int prescriptionId;
    @NotBlank(message = "Name is required.")
    private String name;
    @Min(value = 1, message = "Quantity must be greater than 0.")
    private int quantity;
    @NotBlank(message = "Unit is required.")
    private String unit;
    @NotBlank(message = "Instructions are required.")
    private String instructions;
    private int refills;
    private boolean isActive;
    @Min(value = 1, message = "Patient ID is required.")
    private int patientId;
    @NotBlank(message = "Doctor ID is required.")
    private String doctorUsername;
}
