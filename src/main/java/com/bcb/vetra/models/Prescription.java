package com.bcb.vetra.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Model class for a medication prescription.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Prescription {
    private int prescriptionId;
    private String name;
    private String quantity;
    private String instructions;
    private boolean isActive;
    private int patientId;
    private int doctorId;
}
