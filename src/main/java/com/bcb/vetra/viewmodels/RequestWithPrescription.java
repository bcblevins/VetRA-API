package com.bcb.vetra.viewmodels;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
