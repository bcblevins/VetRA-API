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
    private int id;
    private String quantity;
    private String instructions;
    private boolean isActive;
    private int patientId;
    private String name;
    private int doctorId;
}
