package com.bcb.vetra.viewmodels;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * View Model class for a request with its prescription. Will be used to display refill requests to DOCTORs so they have more contextual information.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestWithPrescription {
    private int requestId;
    private int prescriptionId;
    private int patientId;
    private String status;
    private String requestDate;
    private String name;
    private double quantity;
    private String instructions;
    private int refills;
    private boolean isActive;
    private String doctorUsername;
}
